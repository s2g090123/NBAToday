package com.jiachian.nbatoday.data.repository.game

import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.local.datasource.boxscore.BoxScoreLocalSource
import com.jiachian.nbatoday.data.local.datasource.game.GameLocalSource
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class NbaGameRepository(
    private val gameLocalSource: GameLocalSource,
    private val boxScoreLocalSource: BoxScoreLocalSource,
    private val remoteDataSource: RemoteDataSource,
) : GameRepository() {
    override suspend fun refreshGameBoxScore(gameId: String) {
        val boxScore = remoteDataSource.getGameBoxScore(gameId)
        boxScore?.also {
            val game = boxScore.game?.toLocal()
            game?.also {
                boxScoreLocalSource.insertGameBoxScore(game)
            }
        }
    }

    override suspend fun getGamesAt(date: Long): List<NbaGame> {
        return gameLocalSource.getGamesAt(date)
    }

    override fun getGamesDuring(from: Long, to: Long): Flow<List<NbaGame>> {
        return gameLocalSource.getGamesDuring(from, to)
    }

    override fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<NbaGameAndBet>> {
        return gameLocalSource.getGamesAndBetsDuring(from, to)
    }

    override fun getGamesBefore(from: Long): Flow<List<NbaGame>> {
        return gameLocalSource.getGamesBefore(from)
    }

    override fun getGamesAfter(from: Long): Flow<List<NbaGame>> {
        return gameLocalSource.getGamesAfter(from)
    }

    override fun getGameBoxScore(gameId: String): Flow<GameBoxScore?> {
        return boxScoreLocalSource.getGameBoxScore(gameId)
    }

    override fun getGamesAndBets(): Flow<List<NbaGameAndBet>> {
        return gameLocalSource.getGamesAndBets()
    }

    override fun getGamesAndBetsBeforeByTeam(teamId: Int, from: Long): Flow<List<NbaGameAndBet>> {
        return gameLocalSource.getGamesAndBetsBeforeByTeam(teamId, from)
    }

    override fun getGamesAndBetsAfterByTeam(teamId: Int, from: Long): Flow<List<NbaGameAndBet>> {
        return gameLocalSource.getGamesAndBetsAfterByTeam(teamId, from)
    }
}
