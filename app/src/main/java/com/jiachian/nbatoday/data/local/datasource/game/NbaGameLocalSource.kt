package com.jiachian.nbatoday.data.local.datasource.game

import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.local.dao.GameDao
import com.jiachian.nbatoday.data.remote.game.GameUpdateData
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

    override suspend fun insertGames(games: List<NbaGame>) {
        gameDao.insertGames(games)
    }

    override suspend fun updateGames(games: List<GameUpdateData>) {
        gameDao.updateGames(games)
    }

    override suspend fun existsGame(): Boolean {
        return gameDao.exitsGames()
    }
}
