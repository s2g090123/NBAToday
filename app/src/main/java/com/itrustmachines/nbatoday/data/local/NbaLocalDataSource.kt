package com.itrustmachines.nbatoday.data.local

class NbaLocalDataSource(
    private val dao: NbaDao
) : LocalDataSource() {

    override val dates = dao.getDates()

    override val games = dao.getGames()

    override fun insertGames(games: List<NbaGame>) {
        dao.insertGames(games)
    }
}