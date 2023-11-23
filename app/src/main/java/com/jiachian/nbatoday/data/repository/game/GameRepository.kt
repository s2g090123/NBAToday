package com.jiachian.nbatoday.data.repository.game

import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.repository.BaseRepository
import kotlinx.coroutines.flow.Flow

abstract class GameRepository : BaseRepository() {
    abstract suspend fun refreshGameBoxScore(gameId: String)

    abstract fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<NbaGameAndBet>>
    abstract fun getGameBoxScore(gameId: String): Flow<GameBoxScore?>
    abstract fun getGamesAndBets(): Flow<List<NbaGameAndBet>>
    abstract fun getGamesAndBetsBeforeByTeam(teamId: Int, from: Long): Flow<List<NbaGameAndBet>>
    abstract fun getGamesAndBetsAfterByTeam(teamId: Int, from: Long): Flow<List<NbaGameAndBet>>
}
