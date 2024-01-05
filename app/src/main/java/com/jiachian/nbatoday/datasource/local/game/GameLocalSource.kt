package com.jiachian.nbatoday.datasource.local.game

import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.models.local.game.GameScoreUpdateData
import com.jiachian.nbatoday.models.local.game.GameUpdateData
import java.util.Date
import kotlinx.coroutines.flow.Flow

abstract class GameLocalSource {
    abstract fun getGamesAndBets(): Flow<List<GameAndBets>>
    abstract fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<GameAndBets>>
    abstract fun getGamesAndBetsBefore(teamId: Int, from: Long): Flow<List<GameAndBets>>
    abstract fun getGamesAndBetsAfter(teamId: Int, from: Long): Flow<List<GameAndBets>>
    abstract fun getLastGameDateTime(): Flow<Date?>
    abstract fun getFirstGameDateTime(): Flow<Date?>

    abstract suspend fun insertGames(games: List<Game>)
    abstract suspend fun updateGames(games: List<GameUpdateData>)
    abstract suspend fun updateGameScores(games: List<GameScoreUpdateData>)

    abstract suspend fun gameExists(): Boolean
}
