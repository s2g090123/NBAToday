package com.jiachian.nbatoday.data.local.datasource.game

import com.jiachian.nbatoday.data.local.NbaDao
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.remote.game.GameUpdateData
import kotlinx.coroutines.flow.Flow

class NbaGameLocalSource(
    private val dao: NbaDao
) : GameLocalSource() {
    override suspend fun getGamesAt(date: Long): List<NbaGame> {
        return dao.getGamesAt(date)
    }

    override fun getGamesDuring(from: Long, to: Long): Flow<List<NbaGame>> {
        return dao.getGamesDuring(from, to)
    }

    override fun getGamesAndBets(): Flow<List<NbaGameAndBet>> {
        return dao.getGamesAndBets()
    }

    override fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<NbaGameAndBet>> {
        return dao.getGamesAndBetsDuring(from, to)
    }

    override fun getGamesAndBetsBeforeByTeam(teamId: Int, from: Long): Flow<List<NbaGameAndBet>> {
        return dao.getGamesAndBetsBeforeByTeam(teamId, from)
    }

    override fun getGamesAndBetsAfterByTeam(teamId: Int, from: Long): Flow<List<NbaGameAndBet>> {
        return dao.getGamesAndBetsAfterByTeam(teamId, from)
    }

    override fun getGamesBefore(from: Long): Flow<List<NbaGame>> {
        return dao.getGamesBefore(from)
    }

    override fun getGamesAfter(from: Long): Flow<List<NbaGame>> {
        return dao.getGamesAfter(from)
    }

    override suspend fun insertGames(games: List<NbaGame>) {
        dao.insertGames(games)
    }

    override suspend fun updateGames(games: List<GameUpdateData>) {
        dao.updateGames(games)
    }

    override suspend fun existsGame(): Boolean {
        return dao.exitsGames()
    }
}
