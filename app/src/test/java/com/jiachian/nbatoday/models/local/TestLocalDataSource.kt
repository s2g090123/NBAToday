package com.jiachian.nbatoday.models.local

import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.models.local.bet.BetAndGame
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameAndBet
import com.jiachian.nbatoday.models.local.game.GameScoreUpdateData
import com.jiachian.nbatoday.models.local.game.GameUpdateData
import com.jiachian.nbatoday.models.local.player.Player
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.Team
import com.jiachian.nbatoday.models.local.team.TeamAndPlayers
import com.jiachian.nbatoday.models.local.team.TeamPlayer
import java.util.Date
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class TestLocalDataSource : LocalDataSource() {
    override val dates = MutableStateFlow(emptyList<Date>())
    override val games = MutableStateFlow(emptyList<Game>())
    override val gamesAndBets = MutableStateFlow(emptyList<GameAndBet>())

    private val boxScores = MutableStateFlow(emptyList<BoxScore>())

    private val team = MutableStateFlow(emptyList<Team>())

    private val teamPlayer = MutableStateFlow(emptyList<TeamPlayer>())

    private val playerCareer = MutableStateFlow(emptyList<Player>())

    override suspend fun getGamesAt(date: Long): List<Game> {
        return games.value
    }

    override fun getGamesDuring(from: Long, to: Long): Flow<List<Game>> {
        return games.map {
            it.filter { game ->
                game.gameDate.time in from..to
            }
        }
    }

    override fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<GameAndBet>> {
        return gamesAndBets.map {
            it.filter { gameAndBet ->
                gameAndBet.game.gameDate.time in from..to
            }
        }
    }

    override fun getGamesBefore(from: Long): Flow<List<Game>> {
        return games.map {
            it.filter { game ->
                game.gameDate.time <= from
            }
        }
    }

    override fun getGamesAfter(from: Long): Flow<List<Game>> {
        return games.map {
            it.filter { game ->
                game.gameDate.time > from
            }
        }
    }

    override suspend fun existsGame(): Boolean {
        return games.value.isNotEmpty()
    }

    override suspend fun insertGames(games: List<Game>) {
        this.games.value = this.games.value.toMutableList().apply {
            addAll(games)
        }
    }

    override suspend fun updateGames(games: List<GameUpdateData>) {
        this.games.value = this.games.value.toMutableList().also {
            games.forEach { updateData ->
                val index = it.indexOfFirst { game ->
                    game.gameId == updateData.gameId
                }.takeIf { index ->
                    index != -1
                } ?: return@forEach
                it[index] = it[index].copy(
                    gameStatus = updateData.gameStatus,
                    gameStatusText = updateData.gameStatusText,
                    homeTeam = updateData.homeTeam,
                    awayTeam = updateData.awayTeam,
                    gameLeaders = updateData.gameLeaders,
                    teamLeaders = updateData.teamLeaders
                )
            }
        }
    }

    override suspend fun updateGamesScore(games: List<GameScoreUpdateData>) {
        this.games.value = this.games.value.toMutableList().also {
            games.forEach { updateData ->
                val index = it.indexOfFirst { game ->
                    game.gameId == updateData.gameId
                }.takeIf { index ->
                    index != -1
                } ?: return@forEach
                it[index] = it[index].copy(
                    gameStatus = updateData.gameStatus,
                    gameStatusText = updateData.gameStatusText,
                    homeTeam = updateData.homeTeam,
                    awayTeam = updateData.awayTeam,
                    pointsLeaders = updateData.pointsLeaders
                )
            }
        }
    }

    override fun getGameBoxScore(gameId: String): Flow<BoxScore?> {
        return boxScores.map {
            it.firstOrNull { game ->
                game.gameId == gameId
            }
        }
    }

    override suspend fun insertGameBoxScore(boxScore: BoxScore) {
        boxScores.value = boxScores.value.toMutableList().apply {
            add(boxScore)
        }
    }

    override fun getTeamStats(): Flow<List<Team>> {
        return team
    }

    override fun getTeamAndPlayersStats(teamId: Int): Flow<TeamAndPlayers?> {
        return combine(team, teamPlayer) { teams, players ->
            val team = teams.find { it.teamId == teamId }
            TeamAndPlayers(team, players)
        }
    }

    override fun getTeamRank(teamId: Int, conference: NBATeam.Conference): Flow<Int> {
        return team.map {
            it.filter { team ->
                team.teamConference == conference
            }.sortedByDescending { team ->
                team.winPercentage
            }.indexOfFirst { team ->
                team.teamId == teamId
            } + 1
        }
    }

    override fun getTeamPointsRank(teamId: Int): Flow<Int> {
        return team.map {
            it.sortedByDescending { team ->
                team.points
            }.indexOfFirst { team ->
                team.teamId == teamId
            } + 1
        }
    }

    override fun getTeamReboundsRank(teamId: Int): Flow<Int> {
        return team.map {
            it.sortedByDescending { team ->
                team.reboundsTotal
            }.indexOfFirst { team ->
                team.teamId == teamId
            } + 1
        }
    }

    override fun getTeamAssistsRank(teamId: Int): Flow<Int> {
        return team.map {
            it.sortedByDescending { team ->
                team.assists
            }.indexOfFirst { team ->
                team.teamId == teamId
            } + 1
        }
    }

    override fun getTeamPlusMinusRank(teamId: Int): Flow<Int> {
        return team.map {
            it.sortedByDescending { team ->
                team.plusMinus
            }.indexOfFirst { team ->
                team.teamId == teamId
            } + 1
        }
    }

    override suspend fun updateTeamStats(stats: Team) {
        val update = team.value.toMutableList().apply {
            removeIf { it.teamId == stats.teamId }
            add(stats)
        }
        team.value = update
    }

    override suspend fun updateTeamStats(stats: List<Team>) {
        val update = team.value.toMutableList().apply {
            stats.forEach { team ->
                removeIf { it.teamId == team.teamId }
            }
            addAll(stats)
        }
        team.value = update
    }

    override suspend fun updatePlayerStats(stats: List<TeamPlayer>) {
        val update = teamPlayer.value.toMutableList().apply {
            stats.forEach { player ->
                removeIf { it.playerId == player.playerId }
            }
            addAll(stats)
        }
        teamPlayer.value = update
    }

    override suspend fun updatePlayerCareerStats(stats: PlayerCareerStatsUpdate) {
        playerCareer.value = playerCareer.value.toMutableList().apply {
            val index = indexOfFirst {
                it.playerId == stats.playerId
            }.takeIf { index ->
                index != -1
            } ?: return@apply
            this[index] = this[index].copy(
                stats = stats.stats
            )
        }
    }

    override suspend fun deletePlayerStats(teamId: Int, playerIds: List<Int>) {
        teamPlayer.value = teamPlayer.value.toMutableList().apply {
            removeIf { it.teamId == teamId && it.playerId in playerIds }
        }
    }

    override suspend fun existPlayer(playerId: Int): Boolean {
        return playerCareer.value.firstOrNull { it.playerId == playerId } != null
    }

    override suspend fun insertPlayerCareer(stats: Player) {
        playerCareer.value = playerCareer.value.toMutableList().apply {
            removeIf { it.playerId == stats.playerId }
            add(stats)
        }
    }

    override suspend fun updatePlayerCareerInfo(info: PlayerCareerInfoUpdate) {
        playerCareer.value = playerCareer.value.toMutableList().apply {
            val index = indexOfFirst {
                it.playerId == info.playerId
            }.takeIf { index ->
                index != -1
            } ?: return@apply
            this[index] = this[index].copy(
                info = info.info
            )
        }
    }

    override fun getPlayerCareer(playerId: Int): Flow<Player?> {
        return playerCareer.map {
            it.firstOrNull { player -> player.playerId == playerId }
        }
    }

    override suspend fun insertBet(
        account: String,
        gameId: String,
        homePoints: Long,
        awayPoints: Long
    ) {
        if (gamesAndBets.value.any { it.game.gameId == gameId }) return
        val game = games.value.firstOrNull { it.gameId == gameId } ?: return
        val bet = Bet(
            account = account,
            gameId = gameId,
            homePoints = homePoints,
            awayPoints = awayPoints
        )
        gamesAndBets.value = gamesAndBets.value.toMutableList().apply {
            add(
                GameAndBet(
                    game = game,
                    bets = listOf(bet)
                )
            )
        }
    }

    override fun getBetsAndGames(): Flow<List<BetAndGame>> {
        return gamesAndBets.map {
            it.flatMap { gameAndBet ->
                gameAndBet.bets.map { bets ->
                    BetAndGame(bets, gameAndBet.game)
                }
            }
        }
    }

    override fun getBetsAndGamesByUser(account: String): Flow<List<BetAndGame>> {
        return getBetsAndGames().map {
            it.filter { betAndGame ->
                betAndGame.bet.account == account
            }
        }
    }

    override suspend fun deleteBets(bet: Bet) {
        gamesAndBets.value = gamesAndBets.value.map {
            if (it.bets.contains(bet)) {
                GameAndBet(
                    game = it.game,
                    bets = it.bets.toMutableList().apply {
                        remove(bet)
                    }
                )
            } else {
                it
            }
        }
    }
}
