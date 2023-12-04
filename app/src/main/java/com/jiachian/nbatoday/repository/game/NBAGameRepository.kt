package com.jiachian.nbatoday.repository.game

import com.jiachian.nbatoday.datasource.local.boxscore.BoxScoreLocalSource
import com.jiachian.nbatoday.datasource.local.game.GameLocalSource
import com.jiachian.nbatoday.datasource.remote.game.GameRemoteSource
import com.jiachian.nbatoday.models.local.game.GameAndBet
import com.jiachian.nbatoday.models.local.score.BoxScoreAndGame
import com.jiachian.nbatoday.models.remote.score.toBoxScore
import kotlinx.coroutines.flow.Flow

class NBAGameRepository(
    private val gameLocalSource: GameLocalSource,
    private val boxScoreLocalSource: BoxScoreLocalSource,
    private val gameRemoteSource: GameRemoteSource,
) : GameRepository() {
    override suspend fun updateBoxScore(gameId: String) {
        loading {
            gameRemoteSource
                .getBoxScore(gameId)
                .takeIf { !it.isError() }
                ?.body()
                ?.game
                ?.toBoxScore()
                ?.let { boxScore ->
                    boxScoreLocalSource.insertBoxScore(boxScore)
                }
                ?: showError()
        }
    }

    override fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<GameAndBet>> {
        return gameLocalSource.getGamesAndBetsDuring(from, to)
    }

    override fun getBoxScoreAndGame(gameId: String): Flow<BoxScoreAndGame?> {
        return boxScoreLocalSource.getBoxScoreAndGame(gameId)
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
