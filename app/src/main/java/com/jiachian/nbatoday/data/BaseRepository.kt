package com.jiachian.nbatoday.data

import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.local.TeamAndPlayers
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.local.team.DefaultTeam
import com.jiachian.nbatoday.data.local.team.TeamStats
import kotlinx.coroutines.flow.Flow
import java.util.*

interface BaseRepository {

    val dates: Flow<List<Date>>

    val games: Flow<List<NbaGame>>

    suspend fun refreshSchedule()
    suspend fun refreshSchedule(year: Int, month: Int, day: Int)
    suspend fun refreshGameBoxScore(gameId: String)
    suspend fun refreshTeamStats()
    suspend fun refreshTeamStats(teamId: Int)
    suspend fun refreshPlayerStats(teamId: Int)

    fun getGamesDuring(from: Long, to: Long): Flow<List<NbaGame>>
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
}