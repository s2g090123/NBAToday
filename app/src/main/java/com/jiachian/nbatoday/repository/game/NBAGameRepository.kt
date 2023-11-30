package com.jiachian.nbatoday.repository.game

import com.jiachian.nbatoday.datasource.local.boxscore.BoxScoreLocalSource
import com.jiachian.nbatoday.datasource.local.game.GameLocalSource
import com.jiachian.nbatoday.datasource.remote.game.GameRemoteSource
import com.jiachian.nbatoday.models.local.game.GameAndBet
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.remote.score.toBoxScore
import com.jiachian.nbatoday.utils.showErrorToast
import kotlinx.coroutines.flow.Flow

class NBAGameRepository(
    private val gameLocalSource: GameLocalSource,
    private val boxScoreLocalSource: BoxScoreLocalSource,
    private val gameRemoteSource: GameRemoteSource,
) : GameRepository() {
    override suspend fun updateBoxScore(gameId: String) {
        loading {
            val response = gameRemoteSource.getBoxScore(gameId)
            if (response.isError()) {
                showErrorToast()
                return@loading
            }
            response
                .body()
                ?.game
                ?.toBoxScore()
                ?.also { boxScore ->
                    boxScoreLocalSource.insertBoxScore(boxScore)
                }
        }
    }

    override fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<GameAndBet>> {
        return gameLocalSource.getGamesAndBetsDuring(from, to)
    }

    override fun getBoxScore(gameId: String): Flow<BoxScore?> {
        return boxScoreLocalSource.getBoxScore(gameId)
    }

    override fun getGamesAndBets(): Flow<List<GameAndBet>> {
        return gameLocalSource.getGamesAndBets()
    }

    override fun getGamesAndBetsBeforeByTeam(teamId: Int, from: Long): Flow<List<GameAndBet>> {
        return gameLocalSource.getGamesAndBetsBeforeByTeam(teamId, from)
    }

    override fun getGamesAndBetsAfterByTeam(teamId: Int, from: Long): Flow<List<GameAndBet>> {
        return gameLocalSource.getGamesAndBetsAfterByTeam(teamId, from)
    }
}
