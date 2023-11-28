package com.jiachian.nbatoday.repository.game

import com.jiachian.nbatoday.datasource.local.boxscore.BoxScoreLocalSource
import com.jiachian.nbatoday.datasource.local.game.GameLocalSource
import com.jiachian.nbatoday.datasource.remote.game.GameRemoteSource
import com.jiachian.nbatoday.models.local.game.NbaGameAndBet
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.remote.score.toBoxScore
import kotlinx.coroutines.flow.Flow

class NbaGameRepository(
    private val gameLocalSource: GameLocalSource,
    private val boxScoreLocalSource: BoxScoreLocalSource,
    private val gameRemoteSource: GameRemoteSource,
) : GameRepository() {
    override suspend fun refreshGameBoxScore(gameId: String) {
        val boxScore = gameRemoteSource.getGameBoxScore(gameId)
        boxScore?.also {
            val game = boxScore.game?.toBoxScore()
            game?.also {
                boxScoreLocalSource.insertGameBoxScore(game)
            }
        }
    }

    override fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<NbaGameAndBet>> {
        return gameLocalSource.getGamesAndBetsDuring(from, to)
    }

    override fun getGameBoxScore(gameId: String): Flow<BoxScore?> {
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
