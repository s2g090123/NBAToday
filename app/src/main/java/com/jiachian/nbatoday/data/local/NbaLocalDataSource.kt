package com.jiachian.nbatoday.data.local

import kotlinx.coroutines.flow.Flow

class NbaLocalDataSource(
    private val dao: NbaDao
) : LocalDataSource() {

    override val dates = dao.getDates()

    override val games = dao.getGames()

    override fun getGamesDuring(from: Long, to: Long): Flow<List<NbaGame>> {
        return dao.getGamesDuring(from, to)
    }

    override suspend fun insertGames(games: List<NbaGame>) {
        dao.insertGames(games)
    }
}