package com.jiachian.nbatoday.datasource.local.data

import com.jiachian.nbatoday.datasource.local.game.GameLocalSource
import com.jiachian.nbatoday.game.data.GameDao
import com.jiachian.nbatoday.game.data.model.local.Game
import com.jiachian.nbatoday.game.data.model.local.GameAndBets
import com.jiachian.nbatoday.game.data.model.local.GameScoreUpdateData
import com.jiachian.nbatoday.game.data.model.local.GameUpdateData
import kotlinx.coroutines.flow.Flow
import java.util.Date

class TestGameLocalSource(
    private val gameDao: GameDao
) : GameLocalSource() {
    override fun getGamesAndBets(): Flow<List<GameAndBets>> {
        return gameDao.getGamesAndBets()
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
