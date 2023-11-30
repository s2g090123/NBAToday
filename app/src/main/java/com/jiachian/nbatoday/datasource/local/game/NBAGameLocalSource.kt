package com.jiachian.nbatoday.datasource.local.game

import com.jiachian.nbatoday.database.dao.GameDao
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameAndBet
import com.jiachian.nbatoday.models.local.game.GameScoreUpdateData
import com.jiachian.nbatoday.models.local.game.GameUpdateData
import kotlinx.coroutines.flow.Flow

class NBAGameLocalSource(
    private val gameDao: GameDao
) : GameLocalSource() {
    override fun getGamesAndBets(): Flow<List<GameAndBet>> {
        return gameDao.getGamesAndBets()
    }

    override fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<GameAndBet>> {
        return gameDao.getGamesAndBetsDuring(from, to)
    }

    override fun getGamesAndBetsBeforeByTeam(teamId: Int, from: Long): Flow<List<GameAndBet>> {
        return gameDao.getGamesAndBetsBeforeByTeam(teamId, from)
    }

    override fun getGamesAndBetsAfterByTeam(teamId: Int, from: Long): Flow<List<GameAndBet>> {
        return gameDao.getGamesAndBetsAfterByTeam(teamId, from)
    }

    override suspend fun insertGames(games: List<Game>) {
        gameDao.insertGames(games)
    }

    override suspend fun updateGames(games: List<GameUpdateData>) {
        gameDao.updateGames(games)
    }

    override suspend fun updateGamesScore(games: List<GameScoreUpdateData>) {
        gameDao.updateGameScores(games)
    }

    override suspend fun existsGame(): Boolean {
        return gameDao.exitsGame()
    }
}
