package com.jiachian.nbatoday.data

import com.jiachian.nbatoday.NBA_LEAGUE_ID
import com.jiachian.nbatoday.SCHEDULE_DATE_RANGE
import com.jiachian.nbatoday.data.datastore.BaseDataStore
import com.jiachian.nbatoday.data.local.BetAndNbaGame
import com.jiachian.nbatoday.data.local.LocalDataSource
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.local.TeamAndPlayers
import com.jiachian.nbatoday.data.local.bet.Bets
import com.jiachian.nbatoday.data.local.player.PlayerCareer
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.local.team.TeamStats
import com.jiachian.nbatoday.data.remote.RemoteDataSource
import com.jiachian.nbatoday.data.remote.user.User
import com.jiachian.nbatoday.utils.NbaUtils
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class NbaRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val dataStore: BaseDataStore
) : BaseRepository {

    override val user: Flow<User?> = dataStore.userData

    private val isProgressingImp = MutableStateFlow(false)
    override val isProgressing: StateFlow<Boolean> = isProgressingImp

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
        val betweenDays = TimeUnit.DAYS.convert(today - record, TimeUnit.MILLISECONDS) + 1
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
        scoreboards?.also {
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
        updateData?.also {
            localDataSource.updateGames(updateData)
        }
    }

    override suspend fun refreshGameBoxScore(gameId: String) {
        val boxScore = remoteDataSource.getGameBoxScore(gameId)
        boxScore?.also {
            val game = boxScore.game?.toLocal()
            game?.also {
                localDataSource.insertGameBoxScore(game)
            }
        }
    }

    override suspend fun refreshTeamStats() {
        val stats = remoteDataSource.getTeamStats()
        stats?.also {
            val teamStats = stats.toLocal()
            localDataSource.updateTeamStats(teamStats)
        }
    }

    override suspend fun refreshTeamStats(teamId: Int) {
        val stats = remoteDataSource.getTeamStats(teamId = teamId)
        stats?.also {
            val teamStats = stats.toLocal()
            localDataSource.updateTeamStats(teamStats)
        }
    }

    override suspend fun refreshTeamPlayersStats(teamId: Int) {
        val stats = remoteDataSource.getTeamPlayersStats(teamId = teamId)
        stats?.also {
            val localStats = localDataSource.getTeamAndPlayersStats(teamId).firstOrNull()
            val playersStats = stats.toLocal()
            val oldPlayerIds = localStats?.playersStats?.map { it.playerId }
            val newPlayerIds = playersStats.map { it.playerId }
            oldPlayerIds?.filterNot { it in newPlayerIds }?.also {
                localDataSource.deletePlayerStats(teamId, it)
            }
            localDataSource.updatePlayerStats(playersStats)
        }
    }

    override suspend fun refreshPlayerStats(playerId: Int) {
        val detail = remoteDataSource.getPlayerDetail(playerId)
        val info = detail?.info
        val stats = detail?.stats
        if (localDataSource.existPlayer(playerId)) {
            info?.toUpdateData()?.also {
                localDataSource.updatePlayerCareerInfo(it)
            }
            stats?.toLocal()?.also {
                localDataSource.updatePlayerCareerStats(it)
            }
        } else if (info != null && stats != null) {
            val infoData = info.toUpdateData()?.info
            val statsData = stats.toLocal()?.stats
            if (infoData != null && statsData != null) {
                localDataSource.insertPlayerCareer(
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

    override fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<NbaGameAndBet>> {
        return localDataSource.getGamesAndBetsDuring(from, to)
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

    override fun getTeamRank(teamId: Int, conference: NBATeam.Conference): Flow<Int> {
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

    override suspend fun login(account: String, password: String) {
        isProgressingImp.value = true
        val userData = remoteDataSource.login(account, password) ?: return
        dataStore.updateUser(userData)
        isProgressingImp.value = false
    }

    override suspend fun logout() {
        isProgressingImp.value = true
        dataStore.updateUser(null)
        isProgressingImp.value = false
    }

    override suspend fun register(account: String, password: String) {
        isProgressingImp.value = true
        val userData = remoteDataSource.register(account, password) ?: return
        dataStore.updateUser(userData)
        isProgressingImp.value = false
    }

    override suspend fun updatePassword(password: String) {
        val user = user.firstOrNull() ?: return
        val account = user.account ?: return
        val token = user.token ?: return
        remoteDataSource.updatePassword(account, password, token)
        dataStore.updateUser(
            User(
                account = account,
                name = user.name,
                points = user.points,
                password = password,
                token = token
            )
        )
    }

    override suspend fun updatePoints(points: Long) {
        val user = user.firstOrNull() ?: return
        val account = user.account ?: return
        val token = user.token ?: return
        remoteDataSource.updatePoints(account, points, token)
        dataStore.updateUser(
            User(
                account = account,
                name = user.name,
                points = points,
                password = user.password,
                token = token
            )
        )
    }

    override suspend fun addPoints(points: Long) {
        val user = user.firstOrNull() ?: return
        val account = user.account ?: return
        val token = user.token ?: return
        val currentPoint = user.points ?: return
        val updatePoints = currentPoint + points
        remoteDataSource.updatePoints(account, updatePoints, token)
        dataStore.updateUser(
            User(
                account = account,
                name = user.name,
                points = updatePoints,
                password = user.password,
                token = token
            )
        )
    }

    override suspend fun bet(gameId: String, homePoints: Long, awayPoints: Long) {
        val user = user.firstOrNull() ?: return
        val account = user.account ?: return
        val remainPoints = (user.points ?: 0) - homePoints - awayPoints
        if (remainPoints < 0) return
        isProgressingImp.value = true
        localDataSource.insertBet(account, gameId, homePoints, awayPoints)
        updatePoints(remainPoints)
        isProgressingImp.value = false
    }

    override fun getGamesAndBets(): Flow<List<NbaGameAndBet>> {
        return localDataSource.gamesAndBets
    }

    override fun getBetsAndGames(): Flow<List<BetAndNbaGame>> {
        return localDataSource.getBetsAndGames()
    }

    override fun getBetsAndGames(account: String): Flow<List<BetAndNbaGame>> {
        return localDataSource.getBetsAndGamesByUser(account)
    }

    override suspend fun deleteBets(bets: Bets) {
        localDataSource.deleteBets(bets)
    }
}
