package com.jiachian.nbatoday.repository.game

import com.jiachian.nbatoday.datasource.local.boxscore.BoxScoreLocalSource
import com.jiachian.nbatoday.datasource.local.game.GameLocalSource
import com.jiachian.nbatoday.datasource.remote.game.GameRemoteSource
import com.jiachian.nbatoday.models.local.game.GameAndBet
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.remote.score.toBoxScore
import com.jiachian.nbatoday.utils.showErrorToast
import kotlinx.coroutines.flow.Flow

class NbaGameRepository(
    private val gameLocalSource: GameLocalSource,
    private val boxScoreLocalSource: BoxScoreLocalSource,
    private val gameRemoteSource: GameRemoteSource,
) : GameRepository() {
    override suspend fun refreshGameBoxScore(gameId: String) {
        val response = gameRemoteSource.getBoxScore(gameId)
        if (!response.isSuccessful) {
            showErrorToast()
            return
        }
        val boxScore = response.body()
        boxScore?.also {
            val game = boxScore.game?.toBoxScore()
            game?.also {
                boxScoreLocalSource.insertBoxScore(game)
            }
        }
    }

    override fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<GameAndBet>> {
        return gameLocalSource.getGamesAndBetsDuring(from, to)
    }

    override fun getGameBoxScore(gameId: String): Flow<BoxScore?> {
        return boxScoreLocalSource.getBoxScore(gameId)
    }

    override fun getGamesAndBets(): Flow<List<GameAndBet>> {
        return gameLocalSource.getGamesAndBets()
    }

    override fun getGamesAndBetsBeforeByTeam(teamId: Int, from: Long): Flow<List<GameAndBet>> {
        return gameLocalSource.getGamesAndBetsBeforeByTeam(teamId, from)
    }

    override fun getGamesAndBetsAfterByTeam(teamId: Int, from: Long): Flow<List<GameAndBet>> {
        return gameLocalSource.getGamesAndBetsAfterByTeam(teamId, from)
    }
}
