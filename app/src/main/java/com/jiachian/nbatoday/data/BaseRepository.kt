package com.jiachian.nbatoday.data

import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.local.score.GameBoxScore
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

    fun getGamesDuring(from: Long, to: Long): Flow<List<NbaGame>>

    fun getGameBoxScore(gameId: String): Flow<GameBoxScore?>

    fun getTeamStats(): Flow<List<TeamStats>>
}