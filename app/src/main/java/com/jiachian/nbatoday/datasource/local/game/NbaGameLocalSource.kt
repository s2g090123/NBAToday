package com.jiachian.nbatoday.datasource.local.game

import com.jiachian.nbatoday.database.dao.GameDao
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameScoreUpdateData
import com.jiachian.nbatoday.models.local.game.GameUpdateData
import com.jiachian.nbatoday.models.local.game.NbaGameAndBet
import kotlinx.coroutines.flow.Flow

class NbaGameLocalSource(
    private val gameDao: GameDao
) : GameLocalSource() {
    override fun getGamesAndBets(): Flow<List<NbaGameAndBet>> {
        return gameDao.getGamesAndBets()
    }

    override fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<NbaGameAndBet>> {
        return gameDao.getGamesAndBetsDuring(from, to)
    }

    override fun getGamesAndBetsBeforeByTeam(teamId: Int, from: Long): Flow<List<NbaGameAndBet>> {
        return gameDao.getGamesAndBetsBeforeByTeam(teamId, from)
    }

    override fun getGamesAndBetsAfterByTeam(teamId: Int, from: Long): Flow<List<NbaGameAndBet>> {
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
