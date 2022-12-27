package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.local.team.TeamStats
import com.jiachian.nbatoday.data.remote.game.GameUpdateData
import kotlinx.coroutines.flow.Flow

class NbaLocalDataSource(
    private val dao: NbaDao
) : LocalDataSource() {

    override val dates = dao.getDates()

    override val games = dao.getGames()

    override fun getGamesDuring(from: Long, to: Long): Flow<List<NbaGame>> {
        return dao.getGamesDuring(from, to)
    }

    override suspend fun existsData(): Boolean {
        return dao.exitsData()
    }

    override suspend fun insertGames(games: List<NbaGame>) {
        dao.insertGames(games)
    }

    override suspend fun updateGame(status: List<GameUpdateData>) {
        dao.updateGame(status)
    }

    override fun getGameBoxScore(gameId: String): Flow<GameBoxScore?> {
        return dao.getGameBoxScore(gameId)
    }

    override suspend fun insertGameBoxScore(boxScore: GameBoxScore) {
        dao.insertGameBoxScore(boxScore)
    }

    override fun getTeamStats(): Flow<List<TeamStats>> {
        return dao.getTeamStats()
    }

    override suspend fun updateTeamStats(stats: TeamStats) {
        dao.updateTeamStats(stats)
    }

    override suspend fun updateTeamStats(stats: List<TeamStats>) {
        dao.updateTeamStats(stats)
    }
}