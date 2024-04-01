package com.jiachian.nbatoday.game.data

import com.jiachian.nbatoday.boxscore.data.BoxScoreDao
import com.jiachian.nbatoday.boxscore.data.model.local.BoxScoreAndGame
import com.jiachian.nbatoday.boxscore.data.model.toBoxScore
import com.jiachian.nbatoday.common.data.Response
import com.jiachian.nbatoday.game.data.model.local.GameAndBets
import com.jiachian.nbatoday.utils.isError
import kotlinx.coroutines.flow.Flow

class NBAGameRepository(
    private val gameDao: GameDao,
    private val boxScoreDao: BoxScoreDao,
    private val gameService: GameService,
) : GameRepository {
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

    override suspend fun getGameAndBets(gameId: String): GameAndBets? {
        return gameDao.getGameAndBets(gameId)
    }

    override fun getGamesAndBetsBefore(teamId: Int, from: Long): Flow<List<GameAndBets>> {
        return gameDao.getGamesAndBetsBefore(teamId, from)
    }

    override fun getGamesAndBetsAfter(teamId: Int, from: Long): Flow<List<GameAndBets>> {
        return gameDao.getGamesAndBetsAfter(teamId, from)
    }
}
