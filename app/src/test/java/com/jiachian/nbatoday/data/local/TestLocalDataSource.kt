package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.data.local.bet.Bets
import com.jiachian.nbatoday.data.local.player.PlayerCareer
import com.jiachian.nbatoday.data.local.player.PlayerCareerInfoUpdate
import com.jiachian.nbatoday.data.local.player.PlayerCareerStatsUpdate
import com.jiachian.nbatoday.data.local.player.PlayerStats
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.local.team.DefaultTeam
import com.jiachian.nbatoday.data.local.team.TeamStats
import com.jiachian.nbatoday.data.remote.game.GameScoreUpdateData
import com.jiachian.nbatoday.data.remote.game.GameUpdateData
import java.util.Date
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class TestLocalDataSource : LocalDataSource() {
    override val dates = MutableStateFlow(emptyList<Date>())
    override val games = MutableStateFlow(emptyList<NbaGame>())
    override val gamesAndBets = MutableStateFlow(emptyList<NbaGameAndBet>())

    private val gameBoxScores = MutableStateFlow(emptyList<GameBoxScore>())

    private val teamStats = MutableStateFlow(emptyList<TeamStats>())

    private val playerStats = MutableStateFlow(emptyList<PlayerStats>())

    private val playerCareer = MutableStateFlow(emptyList<PlayerCareer>())

    override suspend fun getGamesAt(date: Long): List<NbaGame> {
        return games.value
    }

    override fun getGamesDuring(from: Long, to: Long): Flow<List<NbaGame>> {
        return games.map {
            it.filter { game ->
                game.gameDate.time in from..to
            }
        }
    }

    override fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<NbaGameAndBet>> {
        return gamesAndBets.map {
            it.filter { gameAndBet ->
                gameAndBet.game.gameDate.time in from..to
            }
        }
    }

    override fun getGamesBefore(from: Long): Flow<List<NbaGame>> {
        return games.map {
            it.filter { game ->
                game.gameDate.time <= from
            }
        }
    }

    override fun getGamesAfter(from: Long): Flow<List<NbaGame>> {
        return games.map {
            it.filter { game ->
                game.gameDate.time > from
            }
        }
    }

    override suspend fun existsGame(): Boolean {
        return games.value.isNotEmpty()
    }

    override suspend fun insertGames(games: List<NbaGame>) {
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

    override fun getGameBoxScore(gameId: String): Flow<GameBoxScore?> {
        return gameBoxScores.map {
            it.firstOrNull { game ->
                game.gameId == gameId
            }
        }
    }

    override suspend fun insertGameBoxScore(boxScore: GameBoxScore) {
        gameBoxScores.value = gameBoxScores.value.toMutableList().apply {
            add(boxScore)
        }
    }

    override fun getTeamStats(): Flow<List<TeamStats>> {
        return teamStats
    }

    override fun getTeamAndPlayersStats(teamId: Int): Flow<TeamAndPlayers?> {
        return combine(teamStats, playerStats) { teams, players ->
            val team = teams.find { it.teamId == teamId }
            TeamAndPlayers(team, players)
        }
    }

    override fun getTeamRank(teamId: Int, conference: DefaultTeam.Conference): Flow<Int> {
        return teamStats.map {
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
        return teamStats.map {
            it.sortedByDescending { team ->
                team.points
            }.indexOfFirst { team ->
                team.teamId == teamId
            } + 1
        }
    }

    override fun getTeamReboundsRank(teamId: Int): Flow<Int> {
        return teamStats.map {
            it.sortedByDescending { team ->
                team.reboundsTotal
            }.indexOfFirst { team ->
                team.teamId == teamId
            } + 1
        }
    }

    override fun getTeamAssistsRank(teamId: Int): Flow<Int> {
        return teamStats.map {
            it.sortedByDescending { team ->
                team.assists
            }.indexOfFirst { team ->
                team.teamId == teamId
            } + 1
        }
    }

    override fun getTeamPlusMinusRank(teamId: Int): Flow<Int> {
        return teamStats.map {
            it.sortedByDescending { team ->
                team.plusMinus
            }.indexOfFirst { team ->
                team.teamId == teamId
            } + 1
        }
    }

    override suspend fun updateTeamStats(stats: TeamStats) {
        val update = teamStats.value.toMutableList().apply {
            removeIf { it.teamId == stats.teamId }
            add(stats)
        }
        teamStats.value = update
    }

    override suspend fun updateTeamStats(stats: List<TeamStats>) {
        val update = teamStats.value.toMutableList().apply {
            stats.forEach { team ->
                removeIf { it.teamId == team.teamId }
            }
            addAll(stats)
        }
        teamStats.value = update
    }

    override suspend fun updatePlayerStats(stats: List<PlayerStats>) {
        val update = playerStats.value.toMutableList().apply {
            stats.forEach { player ->
                removeIf { it.playerId == player.playerId }
            }
            addAll(stats)
        }
        playerStats.value = update
    }

    override suspend fun updatePlayerCareerStats(stats: PlayerCareerStatsUpdate) {
        playerCareer.value = playerCareer.value.toMutableList().apply {
            val index = indexOfFirst {
                it.personId == stats.personId
            }.takeIf { index ->
                index != -1
            } ?: return@apply
            this[index] = this[index].copy(
                stats = stats.stats
            )
        }
    }

    override suspend fun deletePlayerStats(teamId: Int, playerIds: List<Int>) {
        playerStats.value = playerStats.value.toMutableList().apply {
            removeIf { it.teamId == teamId && it.playerId in playerIds }
        }
    }

    override suspend fun existPlayer(playerId: Int): Boolean {
        return playerCareer.value.firstOrNull { it.personId == playerId } != null
    }

    override suspend fun insertPlayerCareer(stats: PlayerCareer) {
        playerCareer.value = playerCareer.value.toMutableList().apply {
            removeIf { it.personId == stats.personId }
            add(stats)
        }
    }

    override suspend fun updatePlayerCareerInfo(info: PlayerCareerInfoUpdate) {
        playerCareer.value = playerCareer.value.toMutableList().apply {
            val index = indexOfFirst {
                it.personId == info.personId
            }.takeIf { index ->
                index != -1
            } ?: return@apply
            this[index] = this[index].copy(
                info = info.info
            )
        }
    }

    override fun getPlayerCareer(playerId: Int): Flow<PlayerCareer?> {
        return playerCareer.map {
            it.firstOrNull { player -> player.personId == playerId }
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
        val bets = Bets(
            account = account,
            gameId = gameId,
            homePoints = homePoints,
            awayPoints = awayPoints
        )
        gamesAndBets.value = gamesAndBets.value.toMutableList().apply {
            add(
                NbaGameAndBet(
                    game = game,
                    bets = listOf(bets)
                )
            )
        }
    }

    override fun getBetsAndGames(): Flow<List<BetAndNbaGame>> {
        return gamesAndBets.map {
            it.flatMap { gameAndBet ->
                gameAndBet.bets.map { bets ->
                    BetAndNbaGame(bets, gameAndBet.game)
                }
            }
        }
    }

    override fun getBetsAndGamesByUser(account: String): Flow<List<BetAndNbaGame>> {
        return getBetsAndGames().map {
            it.filter { betAndGame ->
                betAndGame.bets.account == account
            }
        }
    }

    override suspend fun deleteBets(bets: Bets) {
        gamesAndBets.value = gamesAndBets.value.map {
            if (it.bets.contains(bets)) {
                NbaGameAndBet(
                    game = it.game,
                    bets = it.bets.toMutableList().apply {
                        remove(bets)
                    }
                )
            } else {
                it
            }
        }
    }
}
