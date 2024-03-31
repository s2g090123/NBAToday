package com.jiachian.nbatoday.repository.data

import com.jiachian.nbatoday.boxscore.data.model.local.BoxScoreAndGame
import com.jiachian.nbatoday.datasource.local.boxscore.BoxScoreLocalSource
import com.jiachian.nbatoday.datasource.local.game.GameLocalSource
import com.jiachian.nbatoday.datasource.remote.game.GameRemoteSource
import com.jiachian.nbatoday.game.data.GameRepository
import com.jiachian.nbatoday.game.data.model.local.GameAndBets
import com.jiachian.nbatoday.models.remote.score.extensions.toBoxScore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

class TestGameRepository(
    private val gameLocalSource: GameLocalSource,
    private val boxScoreLocalSource: BoxScoreLocalSource,
    private val gameRemoteSource: GameRemoteSource,
) : GameRepository() {
    override suspend fun addBoxScore(gameId: String) {
        gameRemoteSource
            .getBoxScore(gameId)
            .takeIf { !it.isError() }
            ?.body()
            ?.game
            ?.toBoxScore()
            ?.let { boxScore ->
                boxScoreLocalSource.insertBoxScore(boxScore)
            }
    }

    override fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<GameAndBets>> {
        return gameLocalSource.getGamesAndBetsDuring(from, to)
    }

    override fun getBoxScoreAndGame(gameId: String): Flow<BoxScoreAndGame?> {
        return boxScoreLocalSource.getBoxScoreAndGame(gameId)
    }

    override fun getGamesAndBets(): Flow<List<GameAndBets>> {
        return gameLocalSource.getGamesAndBets()
    }

    override fun getGamesAndBetsBefore(teamId: Int, from: Long): Flow<List<GameAndBets>> {
        return gameLocalSource.getGamesAndBetsBefore(teamId, from)
    }

    override fun getGamesAndBetsAfter(teamId: Int, from: Long): Flow<List<GameAndBets>> {
        return gameLocalSource.getGamesAndBetsAfter(teamId, from)
    }

    override fun getLastGame(): Flow<Date> {
        return gameLocalSource.getLastGameDateTime().map {
            it ?: Date()
        }
    }

    override fun getFirstGameDateTime(): Flow<Date> {
        return gameLocalSource.getFirstGameDateTime().map {
            it ?: Date()
        }
    }
}
