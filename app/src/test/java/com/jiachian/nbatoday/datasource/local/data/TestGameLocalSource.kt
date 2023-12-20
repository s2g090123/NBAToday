package com.jiachian.nbatoday.datasource.local.data

import com.jiachian.nbatoday.DataHolder
import com.jiachian.nbatoday.datasource.local.game.GameLocalSource
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.models.local.game.GameScoreUpdateData
import com.jiachian.nbatoday.models.local.game.GameUpdateData
import java.util.Date
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TestGameLocalSource(
    dataHolder: DataHolder,
) : GameLocalSource() {
    private val games = dataHolder.games
    private val gamesAndBets = dataHolder.gamesAndBets

    override fun getGamesAndBets(): Flow<List<GameAndBets>> {
        return gamesAndBets
    }

    override fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<GameAndBets>> {
        return gamesAndBets.map { gamesAndBets ->
            gamesAndBets.filter { gameAndBets ->
                gameAndBets.game.gameDate.time in from..to
            }
        }
    }

    override fun getGamesAndBetsBefore(teamId: Int, from: Long): Flow<List<GameAndBets>> {
        return gamesAndBets.map { gamesAndBets ->
            gamesAndBets.filter { gameAndBets ->
                gameAndBets.game.gameDate.time <= from &&
                    (gameAndBets.game.homeTeam.team.teamId == teamId || gameAndBets.game.awayTeam.team.teamId == teamId)
            }
        }
    }

    override fun getGamesAndBetsAfter(teamId: Int, from: Long): Flow<List<GameAndBets>> {
        return gamesAndBets.map { gamesAndBets ->
            gamesAndBets.filter { gameAndBets ->
                gameAndBets.game.gameDate.time >= from &&
                    (gameAndBets.game.homeTeam.team.teamId == teamId || gameAndBets.game.awayTeam.team.teamId == teamId)
            }
        }
    }

    override fun getLastGameDateTime(): Flow<Date?> {
        return gamesAndBets.map { gamesAndBets ->
            gamesAndBets.maxByOrNull { gameAndBets ->
                gameAndBets.game.gameDateTime.time
            }?.game?.gameDateTime
        }
    }

    override fun getFirstGameDateTime(): Flow<Date?> {
        return gamesAndBets.map { gamesAndBets ->
            gamesAndBets.minByOrNull { gameAndBets ->
                gameAndBets.game.gameDateTime.time
            }?.game?.gameDateTime
        }
    }

    override suspend fun insertGames(games: List<Game>) {
        val id = games.map { game -> game.gameId }
        this.games.value = games.toMutableList().apply {
            removeIf { game -> game.gameId in id }
            addAll(games)
        }
    }

    override suspend fun updateGames(games: List<GameUpdateData>) {
        this.games.value = this.games.value.toMutableList().apply {
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
        this.games.value = this.games.value.toMutableList().apply {
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

    override suspend fun gameExists(): Boolean {
        return games.value.isNotEmpty()
    }
}
