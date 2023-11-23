package com.jiachian.nbatoday.data.repository.game

import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.local.datasource.boxscore.BoxScoreLocalSource
import com.jiachian.nbatoday.data.local.datasource.game.GameLocalSource
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.remote.datasource.game.GameRemoteSource
import kotlinx.coroutines.flow.Flow

class NbaGameRepository(
    private val gameLocalSource: GameLocalSource,
    private val boxScoreLocalSource: BoxScoreLocalSource,
    private val gameRemoteSource: GameRemoteSource,
) : GameRepository() {
    override suspend fun refreshGameBoxScore(gameId: String) {
        val boxScore = gameRemoteSource.getGameBoxScore(gameId)
        boxScore?.also {
            val game = boxScore.game?.toLocal()
            game?.also {
                boxScoreLocalSource.insertGameBoxScore(game)
            }
        }
    }

    override fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<NbaGameAndBet>> {
        return gameLocalSource.getGamesAndBetsDuring(from, to)
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
