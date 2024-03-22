package com.jiachian.nbatoday.repository.game

import com.jiachian.nbatoday.common.Response
import com.jiachian.nbatoday.database.dao.BoxScoreDao
import com.jiachian.nbatoday.database.dao.GameDao
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.models.local.score.BoxScoreAndGame
import com.jiachian.nbatoday.models.remote.score.extensions.toBoxScore
import com.jiachian.nbatoday.service.GameService
import kotlinx.coroutines.flow.Flow

class NBAGameRepository(
    private val gameDao: GameDao,
    private val boxScoreDao: BoxScoreDao,
    private val gameService: GameService,
) : GameRepository() {
    override suspend fun addBoxScore(gameId: String): Response<Unit> {
        return gameService
            .getBoxScore(gameId)
            .takeIf { !it.isError() }
            ?.body()
            ?.game
            ?.toBoxScore()
            ?.also { boxScore ->
                boxScoreDao.insertBoxScore(boxScore)
            }
            ?.let { Response.Success(Unit) }
            ?: Response.Error()
    }

    override fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<GameAndBets>> {
        return gameDao.getGamesAndBetsDuring(from, to)
    }

    override fun getBoxScoreAndGame(gameId: String): Flow<BoxScoreAndGame?> {
        return boxScoreDao.getBoxScoreAndGame(gameId)
    }

    override fun getGamesAndBets(): Flow<List<GameAndBets>> {
        return gameDao.getGamesAndBets()
    }

    override suspend fun getGameAndBets(gameId: String): GameAndBets {
        return gameDao.getGameAndBets(gameId)
    }

    override fun getGamesAndBetsBefore(teamId: Int, from: Long): Flow<List<GameAndBets>> {
        return gameDao.getGamesAndBetsBefore(teamId, from)
    }

    override fun getGamesAndBetsAfter(teamId: Int, from: Long): Flow<List<GameAndBets>> {
        return gameDao.getGamesAndBetsAfter(teamId, from)
    }
}
