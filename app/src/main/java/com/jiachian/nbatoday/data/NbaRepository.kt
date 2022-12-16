package com.jiachian.nbatoday.data

import com.jiachian.nbatoday.NBA_LEAGUE_ID
import com.jiachian.nbatoday.SCHEDULE_DATE_RANGE
import com.jiachian.nbatoday.data.datastore.NbaDataStore
import com.jiachian.nbatoday.data.local.LocalDataSource
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.local.score.GameBoxScore
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
        if (!localDataSource.existsData()) {
            val nbaGames = leagueSchedule.toNbaGames()
            localDataSource.insertGames(nbaGames)
        }
        val cal = NbaUtils.getCalendar()
        val todayYear = cal.get(Calendar.YEAR)
        val todayMonth = cal.get(Calendar.MONTH)
        val todayDay = cal.get(Calendar.DAY_OF_MONTH)
        val today = NbaUtils.parseDate(todayYear, todayMonth, todayDay)?.time ?: 0L
        val record = NbaUtils.parseDate(dataStore.recordScheduleToday.first())?.time ?: 0L
        val betweenDays = TimeUnit.DAYS.convert(today - record, TimeUnit.MILLISECONDS)
        val offset = betweenDays.toInt().coerceAtMost(SCHEDULE_DATE_RANGE)
        cal.add(Calendar.DAY_OF_MONTH, -offset)
        repeat(offset + SCHEDULE_DATE_RANGE + 1) {
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH) + 1
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val gameDate = NbaUtils.formatScoreboardGameDate(year, month, day)
            val scoreboard = remoteDataSource.getScoreboard(NBA_LEAGUE_ID, gameDate)
            val updateData = scoreboard?.toGameUpdateData()
            if (updateData != null) {
                localDataSource.updateGame(updateData)
            }
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        dataStore.updateRecordScheduleToday(todayYear, todayMonth, todayDay)
    }

    override suspend fun refreshSchedule(year: Int, month: Int, day: Int) {
        val gameDate = NbaUtils.formatScoreboardGameDate(year, month, day)
        val scoreboard = remoteDataSource.getScoreboard(NBA_LEAGUE_ID, gameDate)
        val updateData = scoreboard?.toGameUpdateData()
        if (updateData != null) {
            localDataSource.updateGame(updateData)
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

    override fun getGamesDuring(from: Long, to: Long): Flow<List<NbaGame>> {
        return localDataSource.getGamesDuring(from, to)
    }

    override fun getGameBoxScore(gameId: String): Flow<GameBoxScore?> {
        return localDataSource.getGameBoxScore(gameId)
    }
}