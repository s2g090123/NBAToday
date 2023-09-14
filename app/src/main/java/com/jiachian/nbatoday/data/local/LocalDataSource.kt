package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.data.local.bet.Bets
import com.jiachian.nbatoday.data.local.player.PlayerCareer
import com.jiachian.nbatoday.data.local.player.PlayerCareerInfoUpdate
import com.jiachian.nbatoday.data.local.player.PlayerCareerStatsUpdate
import com.jiachian.nbatoday.data.local.player.PlayerStats
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.local.team.TeamStats
import com.jiachian.nbatoday.data.remote.game.GameScoreUpdateData
import com.jiachian.nbatoday.data.remote.game.GameUpdateData
import java.util.Date
import kotlinx.coroutines.flow.Flow

abstract class LocalDataSource {

    abstract val dates: Flow<List<Date>>

    abstract val games: Flow<List<NbaGame>>

    abstract val gamesAndBets: Flow<List<NbaGameAndBet>>

    abstract suspend fun getGamesAt(date: Long): List<NbaGame>
    abstract fun getGamesDuring(from: Long, to: Long): Flow<List<NbaGame>>
    abstract fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<NbaGameAndBet>>
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
    abstract fun getTeamRank(teamId: Int, conference: NBATeam.Conference): Flow<Int>
    abstract fun getTeamPointsRank(teamId: Int): Flow<Int>
    abstract fun getTeamReboundsRank(teamId: Int): Flow<Int>
    abstract fun getTeamAssistsRank(teamId: Int): Flow<Int>
    abstract fun getTeamPlusMinusRank(teamId: Int): Flow<Int>
    abstract suspend fun updateTeamStats(stats: TeamStats)
    abstract suspend fun updateTeamStats(stats: List<TeamStats>)
    abstract suspend fun updatePlayerStats(stats: List<PlayerStats>)
    abstract suspend fun deletePlayerStats(teamId: Int, playerIds: List<Int>)

    abstract suspend fun existPlayer(playerId: Int): Boolean
    abstract suspend fun insertPlayerCareer(stats: PlayerCareer)
    abstract suspend fun updatePlayerCareerInfo(info: PlayerCareerInfoUpdate)
    abstract suspend fun updatePlayerCareerStats(stats: PlayerCareerStatsUpdate)

    abstract fun getPlayerCareer(playerId: Int): Flow<PlayerCareer?>

    abstract suspend fun insertBet(
        account: String,
        gameId: String,
        homePoints: Long,
        awayPoints: Long
    )

    abstract fun getBetsAndGames(): Flow<List<BetAndNbaGame>>
    abstract fun getBetsAndGamesByUser(account: String): Flow<List<BetAndNbaGame>>
    abstract suspend fun deleteBets(bets: Bets)
}
