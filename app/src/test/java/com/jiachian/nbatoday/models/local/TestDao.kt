package com.jiachian.nbatoday.models.local

import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.models.local.bet.BetAndNbaGame
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameScoreUpdateData
import com.jiachian.nbatoday.models.local.game.GameUpdateData
import com.jiachian.nbatoday.models.local.game.NbaGameAndBet
import com.jiachian.nbatoday.models.local.player.PlayerCareer
import com.jiachian.nbatoday.models.local.player.PlayerCareerInfoUpdate
import com.jiachian.nbatoday.models.local.player.PlayerCareerStatsUpdate
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.TeamAndPlayers
import com.jiachian.nbatoday.models.local.team.TeamPlayerStats
import com.jiachian.nbatoday.models.local.team.TeamStats
import java.util.Date
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class TestDao : NbaDao {

    private val games = MutableStateFlow(emptyList<Game>())
    private val bet = MutableStateFlow(emptyList<Bet>())
    private val boxScores = MutableStateFlow(emptyList<BoxScore>())
    private val teamStats = MutableStateFlow(emptyList<TeamStats>())
    private val teamPlayerStats = MutableStateFlow(emptyList<TeamPlayerStats>())
    private val playerCareer = MutableStateFlow(emptyList<PlayerCareer>())

    override fun getGames(): Flow<List<Game>> {
        return games
    }

    override fun getGamesAndBets(): Flow<List<NbaGameAndBet>> {
        return combine(games, bet) { games, bets ->
            games.map { game ->
                NbaGameAndBet(
                    game = game,
                    bets = bets.filter { it.gameId == game.gameId }
                )
            }
        }
    }

    override fun getBetsAndGames(): Flow<List<BetAndNbaGame>> {
        return combine(games, bet) { games, bets ->
            bets.map { bet ->
                BetAndNbaGame(
                    bet = bet,
                    game = games.first { it.gameId == bet.gameId }
                )
            }
        }
    }

    override fun getBetsAndGamesByUser(account: String): Flow<List<BetAndNbaGame>> {
        return combine(games, bet) { games, bets ->
            bets.filter {
                it.account == account
            }.map { bet ->
                BetAndNbaGame(
                    bet = bet,
                    game = games.first { it.gameId == bet.gameId }
                )
            }
        }
    }

    override suspend fun getGamesAt(date: Long): List<Game> {
        return games.value.filter {
            it.gameDate.time == date
        }
    }

    override fun getGamesDuring(from: Long, to: Long): Flow<List<Game>> {
        return games.map {
            it.filter { game ->
                game.gameDate.time in from..to
            }
        }
    }

    override fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<NbaGameAndBet>> {
        return combine(games, bet) { games, bets ->
            games.filter {
                it.gameDate.time in from..to
            }.map { game ->
                NbaGameAndBet(
                    game = game,
                    bets = bets.filter { it.gameId == game.gameId }
                )
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

    override fun getDates(): Flow<List<Date>> {
        return games.map {
            it.distinctBy { game ->
                game.gameDate
            }.map { game ->
                game.gameDate
            }
        }
    }

    override fun exitsGames(): Boolean {
        return games.value.isNotEmpty()
    }

    override suspend fun insertGames(games: List<Game>) {
        this.games.value = this.games.value.toMutableList().apply {
            addAll(games)
            distinctBy { it.gameId }
        }
    }

    override suspend fun updateGames(games: List<GameUpdateData>) {
        this.games.value = this.games.value.toMutableList().apply {
            games.forEach { update ->
                indexOfFirst { game ->
                    game.gameId == update.gameId
                }.takeIf {
                    it != -1
                }?.also { index ->
                    this[index] = this[index].copy(
                        gameStatus = update.gameStatus,
                        gameStatusText = update.gameStatusText,
                        homeTeam = update.homeTeam,
                        awayTeam = update.awayTeam,
                        gameLeaders = update.gameLeaders,
                        teamLeaders = update.teamLeaders
                    )
                }
            }
        }
    }

    override suspend fun updateGamesScore(games: List<GameScoreUpdateData>) {
        this.games.value = this.games.value.toMutableList().apply {
            games.forEach { update ->
                indexOfFirst { game ->
                    game.gameId == update.gameId
                }.takeIf {
                    it != -1
                }?.also { index ->
                    this[index] = this[index].copy(
                        gameStatus = update.gameStatus,
                        gameStatusText = update.gameStatusText,
                        homeTeam = update.homeTeam,
                        awayTeam = update.awayTeam,
                        pointsLeaders = update.pointsLeaders
                    )
                }
            }
        }
    }

    override fun getGameBoxScore(gameId: String): Flow<BoxScore?> {
        return boxScores.map {
            it.find { game ->
                game.gameId == gameId
            }
        }
    }

    override suspend fun insertGameBoxScore(boxScore: BoxScore) {
        boxScores.value = boxScores.value.toMutableList().apply {
            add(boxScore)
            distinctBy { it.gameId }
        }
    }

    override fun getTeamStats(): Flow<List<TeamStats>> {
        return teamStats
    }

    override fun getTeamAndPlayerStats(teamId: Int): Flow<TeamAndPlayers?> {
        return combine(teamStats, teamPlayerStats) { teams, players ->
            teams.firstOrNull {
                it.teamId == teamId
            }?.let { team ->
                TeamAndPlayers(
                    teamStats = team,
                    playersStats = players.filter { it.teamId == team.teamId }
                )
            }
        }
    }

    override fun getTeamRank(teamId: Int, conference: NBATeam.Conference): Flow<Int> {
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

    override fun getPointsRank(teamId: Int): Flow<Int> {
        return teamStats.map {
            it.sortedByDescending { team ->
                team.points
            }.indexOfFirst { team ->
                team.teamId == teamId
            } + 1
        }
    }

    override fun getReboundsRank(teamId: Int): Flow<Int> {
        return teamStats.map {
            it.sortedByDescending { team ->
                team.reboundsTotal
            }.indexOfFirst { team ->
                team.teamId == teamId
            } + 1
        }
    }

    override fun getAssistsRank(teamId: Int): Flow<Int> {
        return teamStats.map {
            it.sortedByDescending { team ->
                team.assists
            }.indexOfFirst { team ->
                team.teamId == teamId
            } + 1
        }
    }

    override fun getPlusMinusRank(teamId: Int): Flow<Int> {
        return teamStats.map {
            it.sortedByDescending { team ->
                team.plusMinus
            }.indexOfFirst { team ->
                team.teamId == teamId
            } + 1
        }
    }

    override suspend fun insertTeamStats(stats: List<TeamStats>) {
        teamStats.value = teamStats.value.toMutableList().apply {
            addAll(stats)
            distinctBy { it.teamId }
        }
    }

    override suspend fun insertTeamStats(stats: TeamStats) {
        teamStats.value = teamStats.value.toMutableList().apply {
            add(stats)
            distinctBy { it.teamId }
        }
    }

    override suspend fun insertPlayerStats(stats: List<TeamPlayerStats>) {
        teamPlayerStats.value = teamPlayerStats.value.toMutableList().apply {
            addAll(stats)
            distinctBy { it.playerId }
        }
    }

    override suspend fun deleteTeamPlayersStats(teamId: Int, playerIds: List<Int>) {
        teamPlayerStats.value = teamPlayerStats.value.toMutableList().apply {
            removeIf { it.teamId == teamId && it.playerId in playerIds }
        }
    }

    override fun exitsPlayer(playerId: Int): Boolean {
        return teamPlayerStats.value.firstOrNull { it.playerId == playerId } != null
    }

    override fun getPlayerCareer(playerId: Int): Flow<PlayerCareer?> {
        return playerCareer.map {
            it.firstOrNull { player ->
                player.playerId == playerId
            }
        }
    }

    override suspend fun insertPlayerCareer(stats: PlayerCareer) {
        playerCareer.value = playerCareer.value.toMutableList().apply {
            add(stats)
            distinctBy { it.playerId }
        }
    }

    override suspend fun updatePlayerCareerInfo(info: PlayerCareerInfoUpdate) {
        playerCareer.value = playerCareer.value.toMutableList().apply {
            indexOfFirst { player ->
                player.playerId == info.playerId
            }.takeIf {
                it != -1
            }?.also { index ->
                this[index] = this[index].copy(
                    info = info.info
                )
            }
        }
    }

    override suspend fun updatePlayerCareerStats(stats: PlayerCareerStatsUpdate) {
        playerCareer.value = playerCareer.value.toMutableList().apply {
            indexOfFirst { player ->
                player.playerId == stats.playerId
            }.takeIf {
                it != -1
            }?.also { index ->
                this[index] = this[index].copy(
                    stats = stats.stats
                )
            }
        }
    }

    override suspend fun insertBet(bet: Bet) {
        this.bet.value = this.bet.value.toMutableList().apply {
            add(bet)
            distinctBy { it.betId }
        }
    }

    override suspend fun deleteBet(bet: Bet) {
        this.bet.value = this.bet.value.toMutableList().apply {
            removeIf { it.betId == bet.betId }
        }
    }
}
