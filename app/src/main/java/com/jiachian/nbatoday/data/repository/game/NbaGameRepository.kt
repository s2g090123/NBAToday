package com.jiachian.nbatoday.data.repository.game

import com.jiachian.nbatoday.data.local.LocalDataSource
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class NbaGameRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : GameRepository() {
    override suspend fun refreshGameBoxScore(gameId: String) {
        val boxScore = remoteDataSource.getGameBoxScore(gameId)
        boxScore?.also {
            val game = boxScore.game?.toLocal()
            game?.also {
                localDataSource.insertGameBoxScore(game)
            }
        }
    }

    override suspend fun getGamesAt(date: Long): List<NbaGame> {
        return localDataSource.getGamesAt(date)
    }

    override fun getGamesDuring(from: Long, to: Long): Flow<List<NbaGame>> {
        return localDataSource.getGamesDuring(from, to)
    }

    override fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<NbaGameAndBet>> {
        return localDataSource.getGamesAndBetsDuring(from, to)
    }

    override fun getGamesBefore(from: Long): Flow<List<NbaGame>> {
        return localDataSource.getGamesBefore(from)
    }

    override fun getGamesAfter(from: Long): Flow<List<NbaGame>> {
        return localDataSource.getGamesAfter(from)
    }

    override fun getGameBoxScore(gameId: String): Flow<GameBoxScore?> {
        return localDataSource.getGameBoxScore(gameId)
    }

    override fun getGamesAndBets(): Flow<List<NbaGameAndBet>> {
        return localDataSource.gamesAndBets
    }

    override fun getGamesAndBetsBeforeByTeam(teamId: Int, from: Long): Flow<List<NbaGameAndBet>> {
        return localDataSource.getGamesAndBetsBeforeByTeam(teamId, from)
    }

    override fun getGamesAndBetsAfterByTeam(teamId: Int, from: Long): Flow<List<NbaGameAndBet>> {
        return localDataSource.getGamesAndBetsAfterByTeam(teamId, from)
    }
}
