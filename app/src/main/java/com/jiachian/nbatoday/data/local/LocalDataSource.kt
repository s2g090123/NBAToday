package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.data.local.player.PlayerCareer
import com.jiachian.nbatoday.data.local.player.PlayerCareerInfoUpdate
import com.jiachian.nbatoday.data.local.player.PlayerCareerStatsUpdate
import com.jiachian.nbatoday.data.local.player.PlayerStats
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.local.team.DefaultTeam
import com.jiachian.nbatoday.data.local.team.TeamStats
import com.jiachian.nbatoday.data.remote.game.GameScoreUpdateData
import com.jiachian.nbatoday.data.remote.game.GameUpdateData
import kotlinx.coroutines.flow.Flow
import java.util.*

abstract class LocalDataSource {

    abstract val dates: Flow<List<Date>>

    abstract val games: Flow<List<NbaGame>>

    abstract fun getGamesDuring(from: Long, to: Long): Flow<List<NbaGame>>
    abstract fun getGamesBefore(from: Long): Flow<List<NbaGame>>
    abstract fun getGamesAfter(from: Long): Flow<List<NbaGame>>

    abstract suspend fun existsGame(): Boolean

    abstract suspend fun insertGames(games: List<NbaGame>)

    abstract suspend fun updateGames(games: List<GameUpdateData>)
    abstract suspend fun updateGamesScore(games: List<GameScoreUpdateData>)

    abstract fun getGameBoxScore(gameId: String): Flow<GameBoxScore?>
    abstract suspend fun insertGameBoxScore(boxScore: GameBoxScore)

    abstract fun getTeamStats(): Flow<List<TeamStats>>
    abstract fun getTeamAndPlayersStats(teamId: Int): Flow<TeamAndPlayers?>
    abstract fun getTeamRank(teamId: Int, conference: DefaultTeam.Conference): Flow<Int>
    abstract fun getTeamPointsRank(teamId: Int): Flow<Int>
    abstract fun getTeamReboundsRank(teamId: Int): Flow<Int>
    abstract fun getTeamAssistsRank(teamId: Int): Flow<Int>
    abstract fun getTeamPlusMinusRank(teamId: Int): Flow<Int>
    abstract suspend fun updateTeamStats(stats: TeamStats)
    abstract suspend fun updateTeamStats(stats: List<TeamStats>)
    abstract suspend fun updatePlayerStats(stats: List<PlayerStats>)

    abstract suspend fun existPlayer(playerId: Int): Boolean
    abstract suspend fun insertPlayerStats(stats: PlayerCareer)
    abstract suspend fun updatePlayerInfo(info: PlayerCareerInfoUpdate)
    abstract suspend fun updatePlayerStats(stats: PlayerCareerStatsUpdate)

    abstract fun getPlayerCareer(playerId: Int): Flow<PlayerCareer?>
}