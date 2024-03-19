package com.jiachian.nbatoday.datasource.local.game

import com.jiachian.nbatoday.database.dao.GameDao
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.models.local.game.GameScoreUpdateData
import com.jiachian.nbatoday.models.local.game.GameUpdateData
import java.util.Date
import kotlinx.coroutines.flow.Flow

class NBAGameLocalSource(
    private val gameDao: GameDao
) : GameLocalSource() {
    override fun getGamesAndBets(): Flow<List<GameAndBets>> {
        return gameDao.getGamesAndBets()
    }

    override suspend fun getGameAndBet(gameId: String): GameAndBets {
        return gameDao.getGameAndBet(gameId)
    }

    override fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<GameAndBets>> {
        return gameDao.getGamesAndBetsDuring(from, to)
    }

    override fun getGamesAndBetsBefore(teamId: Int, from: Long): Flow<List<GameAndBets>> {
        return gameDao.getGamesAndBetsBefore(teamId, from)
    }

    override fun getGamesAndBetsAfter(teamId: Int, from: Long): Flow<List<GameAndBets>> {
        return gameDao.getGamesAndBetsAfter(teamId, from)
    }

    override fun getLastGameDateTime(): Flow<Date?> {
        return gameDao.getLastGameDateTime()
    }

    override fun getFirstGameDateTime(): Flow<Date?> {
        return gameDao.getFirstGameDateTime()
    }

    override suspend fun insertGames(games: List<Game>) {
        gameDao.addGames(games)
    }

    override suspend fun updateGames(games: List<GameUpdateData>) {
        gameDao.updateGames(games)
    }

    override suspend fun updateGameScores(games: List<GameScoreUpdateData>) {
        gameDao.updateGameScores(games)
    }

    override suspend fun gameExists(): Boolean {
        return gameDao.gameExists()
    }
}
