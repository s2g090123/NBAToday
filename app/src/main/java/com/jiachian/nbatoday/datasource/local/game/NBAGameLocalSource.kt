package com.jiachian.nbatoday.datasource.local.game

import com.jiachian.nbatoday.database.dao.GameDao
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

    override fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<GameAndBets>> {
        return gameDao.getGamesAndBetsDuring(from, to)
    }

    override fun getGamesAndBetsBeforeByTeam(teamId: Int, from: Long): Flow<List<GameAndBets>> {
        return gameDao.getGamesAndBetsBeforeByTeam(teamId, from)
    }

    override fun getGamesAndBetsAfterByTeam(teamId: Int, from: Long): Flow<List<GameAndBets>> {
        return gameDao.getGamesAndBetsAfterByTeam(teamId, from)
    }

    override fun getLastGameDateTime(): Flow<Date> {
        return gameDao.getLastGameDateTime()
    }

    override fun getFirstGameDateTime(): Flow<Date> {
        return gameDao.getFirstGameDateTime()
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
