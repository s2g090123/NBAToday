package com.jiachian.nbatoday.database.dao

import com.jiachian.nbatoday.DataHolder
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.models.local.game.GameScoreUpdateData
import com.jiachian.nbatoday.models.local.game.GameUpdateData
import java.util.Date
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TestGameDao(
    private val dataHolder: DataHolder
) : GameDao {
    override fun getGamesAndBets(): Flow<List<GameAndBets>> {
        return dataHolder.gamesAndBets
    }

    override fun getGamesAndBetsBefore(teamId: Int, from: Long): Flow<List<GameAndBets>> {
        return dataHolder.gamesAndBets.map { gamesAndBets ->
            gamesAndBets.filter { gameAndBets ->
                gameAndBets.game.gameDate.time <= from &&
                    (gameAndBets.game.homeTeam.team.teamId == teamId || gameAndBets.game.awayTeam.team.teamId == teamId)
            }
        }
    }

    override fun getGamesAndBetsAfter(teamId: Int, from: Long): Flow<List<GameAndBets>> {
        return dataHolder.gamesAndBets.map { gamesAndBets ->
            gamesAndBets.filter { gameAndBets ->
                gameAndBets.game.gameDate.time >= from &&
                    (gameAndBets.game.homeTeam.team.teamId == teamId || gameAndBets.game.awayTeam.team.teamId == teamId)
            }
        }
    }

    override fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<GameAndBets>> {
        return dataHolder.gamesAndBets.map { gamesAndBets ->
            gamesAndBets.filter { gameAndBets ->
                gameAndBets.game.gameDate.time in from..to
            }
        }
    }

    override fun getLastGameDateTime(): Flow<Date?> {
        return dataHolder.gamesAndBets.map { gamesAndBets ->
            gamesAndBets.maxByOrNull { gameAndBets ->
                gameAndBets.game.gameDateTime.time
            }?.game?.gameDateTime
        }
    }

    override fun getFirstGameDateTime(): Flow<Date?> {
        return dataHolder.gamesAndBets.map { gamesAndBets ->
            gamesAndBets.minByOrNull { gameAndBets ->
                gameAndBets.game.gameDateTime.time
            }?.game?.gameDateTime
        }
    }

    override fun gameExists(): Boolean {
        return dataHolder.games.value.isNotEmpty()
    }

    override suspend fun insertGames(games: List<Game>) {
        dataHolder.games.value = dataHolder.games.value.toMutableList().apply {
            val id = games.map { it.gameId }
            removeAll { game -> game.gameId in id }
            addAll(games)
        }
    }

    override suspend fun updateGames(games: List<GameUpdateData>) {
        dataHolder.games.value = dataHolder.games.value.toMutableList().apply {
            games.forEach { gameUpdate ->
                val index = indexOfFirst { game ->
                    game.gameId == gameUpdate.gameId
                }.takeIf {
                    it != -1
                } ?: return@forEach
                val game = get(index)
                set(
                    index,
                    game.copy(
                        gameStatus = gameUpdate.gameStatus,
                        gameStatusText = gameUpdate.gameStatusText,
                        homeTeam = gameUpdate.homeTeam,
                        awayTeam = gameUpdate.awayTeam,
                        gameLeaders = gameUpdate.gameLeaders,
                        teamLeaders = gameUpdate.teamLeaders,
                    )
                )
            }
        }
    }

    override suspend fun updateGameScores(games: List<GameScoreUpdateData>) {
        dataHolder.games.value = dataHolder.games.value.toMutableList().apply {
            games.forEach { gameUpdate ->
                val index = indexOfFirst { game ->
                    game.gameId == gameUpdate.gameId
                }.takeIf {
                    it != -1
                } ?: return@forEach
                val game = get(index)
                set(
                    index,
                    game.copy(
                        gameStatus = gameUpdate.gameStatus,
                        gameStatusText = gameUpdate.gameStatusText,
                        homeTeam = gameUpdate.homeTeam,
                        awayTeam = gameUpdate.awayTeam,
                        pointsLeaders = gameUpdate.pointsLeaders,
                    )
                )
            }
        }
    }
}
