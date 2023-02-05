package com.jiachian.nbatoday.data

import com.jiachian.nbatoday.NBA_LEAGUE_ID
import com.jiachian.nbatoday.SCHEDULE_DATE_RANGE
import com.jiachian.nbatoday.data.datastore.NbaDataStore
import com.jiachian.nbatoday.data.local.LocalDataSource
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.local.TeamAndPlayers
import com.jiachian.nbatoday.data.local.player.PlayerCareer
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.local.team.DefaultTeam
import com.jiachian.nbatoday.data.local.team.TeamStats
import com.jiachian.nbatoday.data.remote.RemoteDataSource
import com.jiachian.nbatoday.utils.NbaUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.*
import java.util.concurrent.TimeUnit

class NbaRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val dataStore: NbaDataStore
) : BaseRepository {

    override val dates: Flow<List<Date>> = localDataSource.dates
    override val games: Flow<List<NbaGame>> = localDataSource.games

    override suspend fun refreshSchedule() {
        val schedule = remoteDataSource.getSchedule() ?: return
        val leagueSchedule = schedule.leagueSchedule ?: return
        val nbaGames = leagueSchedule.toNbaGames()

        val cal = NbaUtils.getCalendar()
        val todayYear = cal.get(Calendar.YEAR)
        val todayMonth = cal.get(Calendar.MONTH) + 1
        val todayDay = cal.get(Calendar.DAY_OF_MONTH)
        val today = NbaUtils.parseDate(todayYear, todayMonth, todayDay)?.time ?: 0L
        val recordDay = NbaUtils.parseDate(dataStore.recordScheduleToday.first())
        val record = recordDay?.time ?: 0L
        val betweenDays = TimeUnit.DAYS.convert(today - record, TimeUnit.MILLISECONDS)
        val offset = betweenDays.toInt().coerceAtMost(SCHEDULE_DATE_RANGE)
        cal.add(Calendar.DAY_OF_MONTH, -offset)

        if (!localDataSource.existsGame()) {
            localDataSource.insertGames(nbaGames)
        } else {
            val filterGames = if (recordDay == null) {
                nbaGames
            } else {
                cal.add(Calendar.DAY_OF_MONTH, -1)
                val current = cal.time
                cal.add(Calendar.DAY_OF_MONTH, 1)
                nbaGames.filter {
                    val gameDate = it.gameDateTime
                    gameDate.after(recordDay) && gameDate.before(current)
                }
            }
            val updateGames = filterGames.map { it.toGameScoreUpdateData() }
            localDataSource.updateGamesScore(updateGames)
        }

        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val scoreboards = remoteDataSource.getScoreboard(
            NBA_LEAGUE_ID,
            year,
            month,
            day,
            offset + SCHEDULE_DATE_RANGE + 1
        )
        if (scoreboards != null) {
            val update = scoreboards.map { it.toGameUpdateData() }
            update.forEach {
                localDataSource.updateGames(it)
            }
        }
        dataStore.updateRecordScheduleToday(todayYear, todayMonth, todayDay)
    }

    override suspend fun refreshSchedule(year: Int, month: Int, day: Int) {
        val gameDate = NbaUtils.formatScoreboardGameDate(year, month, day)
        val scoreboard = remoteDataSource.getScoreboard(NBA_LEAGUE_ID, gameDate)
        val updateData = scoreboard?.toGameUpdateData()
        if (updateData != null) {
            localDataSource.updateGames(updateData)
        }
    }

    override suspend fun refreshGameBoxScore(gameId: String) {
        val boxScore = remoteDataSource.getGameBoxScore(gameId)
        if (boxScore != null) {
            val game = boxScore.game?.toLocal()
            if (game != null) {
                localDataSource.insertGameBoxScore(game)
            }
        }
    }

    override suspend fun refreshTeamStats() {
        val stats = remoteDataSource.getTeamStats()
        if (stats != null) {
            val teamStats = stats.toLocal()
            localDataSource.updateTeamStats(teamStats)
        }
    }

    override suspend fun refreshTeamStats(teamId: Int) {
        val stats = remoteDataSource.getTeamStats(teamId = teamId)
        if (stats != null) {
            val teamStats = stats.toLocal()
            localDataSource.updateTeamStats(teamStats)
        }
    }

    override suspend fun refreshTeamPlayersStats(teamId: Int) {
        val stats = remoteDataSource.getTeamPlayersStats(teamId = teamId)
        if (stats != null) {
            val playersStats = stats.toLocal()
            localDataSource.updatePlayerStats(playersStats)
        }
    }

    override suspend fun refreshPlayerStats(playerId: Int) {
        val info = remoteDataSource.getPlayerInfo(playerId)
        val stats = remoteDataSource.getPlayerCareerStats(playerId)
        if (localDataSource.existPlayer(playerId)) {
            if (info != null) {
                info.toUpdateData()?.also {
                    localDataSource.updatePlayerInfo(it)
                }
            }
            if (stats != null) {
                stats.toLocal()?.also {
                    localDataSource.updatePlayerStats(it)
                }
            }
        } else if (info != null && stats != null) {
            val infoData = info.toUpdateData()?.info
            val statsData = stats.toLocal()?.stats
            if (infoData != null && statsData != null) {
                localDataSource.insertPlayerStats(
                    PlayerCareer(playerId, infoData, statsData)
                )
            }
        }
    }

    override suspend fun getGamesAt(date: Long): List<NbaGame> {
        return localDataSource.getGamesAt(date)
    }

    override fun getGamesDuring(from: Long, to: Long): Flow<List<NbaGame>> {
        return localDataSource.getGamesDuring(from, to)
    }

    override fun getGamesBefore(from: Long): Flow<List<NbaGame>> {
        return localDataSource.getGamesBefore(from)
    }

    override fun getGamesAfter(from: Long): Flow<List<NbaGame>> {
        return localDataSource.getGamesAfter(from)
    }

    override fun getGameBoxScore(gameId: String): Flow<GameBoxScore?> {
        return localDataSource.getGameBoxScore(gameId)
    }

    override fun getTeamStats(): Flow<List<TeamStats>> {
        return localDataSource.getTeamStats()
    }

    override fun getTeamAndPlayersStats(teamId: Int): Flow<TeamAndPlayers?> {
        return localDataSource.getTeamAndPlayersStats(teamId)
    }

    override fun getTeamRank(teamId: Int, conference: DefaultTeam.Conference): Flow<Int> {
        return localDataSource.getTeamRank(teamId, conference)
    }

    override fun getTeamPointsRank(teamId: Int): Flow<Int> {
        return localDataSource.getTeamPointsRank(teamId)
    }

    override fun getTeamReboundsRank(teamId: Int): Flow<Int> {
        return localDataSource.getTeamReboundsRank(teamId)
    }

    override fun getTeamAssistsRank(teamId: Int): Flow<Int> {
        return localDataSource.getTeamAssistsRank(teamId)
    }

    override fun getTeamPlusMinusRank(teamId: Int): Flow<Int> {
        return localDataSource.getTeamPlusMinusRank(teamId)
    }

    override fun getPlayerCareer(playerId: Int): Flow<PlayerCareer?> {
        return localDataSource.getPlayerCareer(playerId)
    }
}