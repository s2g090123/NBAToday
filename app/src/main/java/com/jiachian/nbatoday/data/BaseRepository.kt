package com.jiachian.nbatoday.data

import com.jiachian.nbatoday.data.local.BetAndNbaGame
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.local.TeamAndPlayers
import com.jiachian.nbatoday.data.local.bet.Bets
import com.jiachian.nbatoday.data.local.player.PlayerCareer
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.local.team.DefaultTeam
import com.jiachian.nbatoday.data.local.team.TeamStats
import com.jiachian.nbatoday.data.remote.user.User
import kotlinx.coroutines.flow.Flow
import java.util.*

interface BaseRepository {

    val dates: Flow<List<Date>>

    val games: Flow<List<NbaGame>>

    val gamesAndBets: Flow<List<NbaGameAndBet>>

    val user: Flow<User?>

    suspend fun refreshSchedule()
    suspend fun refreshSchedule(year: Int, month: Int, day: Int)
    suspend fun refreshGameBoxScore(gameId: String)
    suspend fun refreshTeamStats()
    suspend fun refreshTeamStats(teamId: Int)
    suspend fun refreshTeamPlayersStats(teamId: Int)
    suspend fun refreshPlayerStats(playerId: Int)

    suspend fun getGamesAt(date: Long): List<NbaGame>
    fun getGamesDuring(from: Long, to: Long): Flow<List<NbaGame>>
    fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<NbaGameAndBet>>
    fun getGamesBefore(from: Long): Flow<List<NbaGame>>
    fun getGamesAfter(from: Long): Flow<List<NbaGame>>

    fun getGameBoxScore(gameId: String): Flow<GameBoxScore?>

    fun getTeamStats(): Flow<List<TeamStats>>

    fun getTeamAndPlayersStats(teamId: Int): Flow<TeamAndPlayers?>

    fun getTeamRank(teamId: Int, conference: DefaultTeam.Conference): Flow<Int>
    fun getTeamPointsRank(teamId: Int): Flow<Int>
    fun getTeamReboundsRank(teamId: Int): Flow<Int>
    fun getTeamAssistsRank(teamId: Int): Flow<Int>
    fun getTeamPlusMinusRank(teamId: Int): Flow<Int>

    fun getPlayerCareer(playerId: Int): Flow<PlayerCareer?>

    /** User */
    suspend fun login(account: String, password: String)
    suspend fun logout()
    suspend fun register(account: String, password: String)
    suspend fun updatePassword(password: String)
    suspend fun updatePoints(points: Long)
    suspend fun addPoints(points: Long)
    suspend fun bet(gameId: String, homePoints: Long, awayPoints: Long)

    /** Bets */
    fun getBetsAndGames(): Flow<List<BetAndNbaGame>>
    fun getBetsAndGames(account: String): Flow<List<BetAndNbaGame>>
    suspend fun deleteBets(bets: Bets)
}