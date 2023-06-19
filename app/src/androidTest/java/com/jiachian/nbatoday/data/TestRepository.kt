package com.jiachian.nbatoday.data

import com.jiachian.nbatoday.AWAY_PLAYER_ID
import com.jiachian.nbatoday.AWAY_TEAM_ID
import com.jiachian.nbatoday.FINAL_GAME_ID
import com.jiachian.nbatoday.HOME_PLAYER_ID
import com.jiachian.nbatoday.HOME_TEAM_ID
import com.jiachian.nbatoday.PLAYING_GAME_ID
import com.jiachian.nbatoday.USER_ACCOUNT
import com.jiachian.nbatoday.USER_NAME
import com.jiachian.nbatoday.USER_PASSWORD
import com.jiachian.nbatoday.USER_POINTS
import com.jiachian.nbatoday.USER_TOKEN
import com.jiachian.nbatoday.data.local.BetAndNbaGame
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.local.TeamAndPlayers
import com.jiachian.nbatoday.data.local.bet.Bets
import com.jiachian.nbatoday.data.local.player.PlayerCareer
import com.jiachian.nbatoday.data.local.player.PlayerStats
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.local.team.DefaultTeam
import com.jiachian.nbatoday.data.local.team.TeamStats
import com.jiachian.nbatoday.data.remote.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class TestRepository : BaseRepository {

    private val games = MutableStateFlow(emptyList<NbaGame>())

    private val teams = MutableStateFlow(emptyList<TeamStats>())

    private val players = MutableStateFlow(emptyList<PlayerStats>())

    private val playersCareer = MutableStateFlow(emptyList<PlayerCareer>())

    private val bets = MutableStateFlow(emptyList<Bets>())

    private val boxScores = MutableStateFlow(emptyList<GameBoxScore>())

    override val user = MutableStateFlow<User?>(null)

    fun clear() {
        games.value = emptyList()
        teams.value = emptyList()
        players.value = emptyList()
        playersCareer.value = emptyList()
        bets.value = emptyList()
        boxScores.value = emptyList()
        user.value = null
    }

    override suspend fun refreshSchedule() {
        games.value = listOf(
            NbaGameFactory.getFinalGame(),
            NbaGameFactory.getPlayingGame(),
            NbaGameFactory.getComingSoonGame()
        )
    }

    override suspend fun refreshSchedule(year: Int, month: Int, day: Int) {
        games.value = listOf(
            NbaGameFactory.getFinalGame(),
            NbaGameFactory.getPlayingGame(),
            NbaGameFactory.getComingSoonGame()
        )
    }

    override suspend fun refreshGameBoxScore(gameId: String) {
        boxScores.value = boxScores.value
            .filterNot { it.gameId == gameId }
            .toMutableList()
            .apply {
                when (gameId) {
                    FINAL_GAME_ID -> BoxScoreFactory.getFinalGameBoxScore()
                    PLAYING_GAME_ID -> BoxScoreFactory.getPlayingGameBoxScore()
                    else -> null
                }?.let {
                    add(it)
                }
            }
    }

    override suspend fun refreshTeamStats() {
        teams.value = listOf(
            TeamStatsFactory.getHomeTeamStats(),
            TeamStatsFactory.getAwayTeamStats()
        )
    }

    override suspend fun refreshTeamStats(teamId: Int) {
        val update = teams.value
            .filterNot { it.teamId == teamId }
            .toMutableList()
            .apply {
                when (teamId) {
                    HOME_TEAM_ID -> TeamStatsFactory.getHomeTeamStats()
                    AWAY_TEAM_ID -> TeamStatsFactory.getAwayTeamStats()
                    else -> null
                }?.let {
                    add(it)
                }
            }
        teams.value = update
    }

    override suspend fun refreshTeamPlayersStats(teamId: Int) {
        val teamUpdate = teams.value
            .filterNot { it.teamId == teamId }
            .toMutableList()
            .apply {
                when (teamId) {
                    HOME_TEAM_ID -> TeamStatsFactory.getHomeTeamStats()
                    AWAY_TEAM_ID -> TeamStatsFactory.getAwayTeamStats()
                    else -> null
                }?.let {
                    add(it)
                }
            }
        val playerUpdate = players.value
            .filterNot { it.teamId == teamId }
            .toMutableList()
            .apply {
                when (teamId) {
                    HOME_TEAM_ID -> PlayerStatsFactory.getHomePlayerStats()
                    AWAY_TEAM_ID -> PlayerStatsFactory.getAwayPlayerStats()
                    else -> null
                }?.let {
                    add(it)
                }
            }
        teams.value = teamUpdate
        players.value = playerUpdate
    }

    override suspend fun refreshPlayerStats(playerId: Int) {
        players.value = players.value
            .filterNot { it.playerId == playerId }
            .toMutableList()
            .apply {
                when (playerId) {
                    HOME_PLAYER_ID -> PlayerStatsFactory.getHomePlayerStats()
                    AWAY_PLAYER_ID -> PlayerStatsFactory.getAwayPlayerStats()
                    else -> null
                }?.let {
                    add(it)
                }
            }
        playersCareer.value = playersCareer.value
            .filterNot { it.personId == playerId }
            .toMutableList()
            .apply {
                when (playerId) {
                    HOME_PLAYER_ID -> PlayerCareerFactory.createHomePlayerCareer()
                    AWAY_PLAYER_ID -> PlayerCareerFactory.createAwayPlayerCareer()
                    else -> null
                }?.let {
                    add(it)
                }
            }
    }

    override suspend fun getGamesAt(date: Long): List<NbaGame> {
        return games.value.filter {
            it.gameDate.time == date
        }
    }

    override fun getGamesDuring(from: Long, to: Long): Flow<List<NbaGame>> {
        return games.map { games ->
            games.filter {
                it.gameDate.time in from..to
            }
        }
    }

    override fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<NbaGameAndBet>> {
        return games.combine(bets) { games, bets ->
            games.filter { game ->
                game.gameDate.time in from..to
            }.map { game ->
                val bet = bets.filter {
                    it.gameId == game.gameId
                }
                NbaGameAndBet(game, bet)
            }
        }
    }

    override fun getGamesBefore(from: Long): Flow<List<NbaGame>> {
        return games.map { games ->
            games.filter { game ->
                game.gameDate.time <= from
            }
        }
    }

    override fun getGamesAfter(from: Long): Flow<List<NbaGame>> {
        return games.map { games ->
            games.filter { game ->
                game.gameDate.time > from
            }
        }
    }

    override fun getGameBoxScore(gameId: String): Flow<GameBoxScore?> {
        return boxScores.map { scores ->
            scores.find {
                it.gameId == gameId
            }
        }
    }

    override fun getTeamStats(): Flow<List<TeamStats>> {
        return teams
    }

    override fun getTeamAndPlayersStats(teamId: Int): Flow<TeamAndPlayers?> {
        return teams.map { teams ->
            teams.firstOrNull { it.teamId == teamId }
        }.combine(players) { team, players ->
            team ?: return@combine null
            val teamPlayers = players.filter { it.teamId == teamId }
            TeamAndPlayers(team, teamPlayers)
        }
    }

    override fun getTeamRank(teamId: Int, conference: DefaultTeam.Conference): Flow<Int> {
        return teams.map { teams ->
            teams.filter {
                it.teamConference == conference
            }.sortedByDescending {
                it.win
            }.indexOfFirst {
                it.teamId == teamId
            }.plus(1)
        }
    }

    override fun getTeamPointsRank(teamId: Int): Flow<Int> {
        return teams.map { teams ->
            teams.sortedByDescending {
                it.points
            }.indexOfFirst {
                it.teamId == teamId
            }.plus(1)
        }
    }

    override fun getTeamReboundsRank(teamId: Int): Flow<Int> {
        return teams.map { teams ->
            teams.sortedByDescending {
                it.reboundsTotal
            }.indexOfFirst {
                it.teamId == teamId
            }.plus(1)
        }
    }

    override fun getTeamAssistsRank(teamId: Int): Flow<Int> {
        return teams.map { teams ->
            teams.sortedByDescending {
                it.assists
            }.indexOfFirst {
                it.teamId == teamId
            }.plus(1)
        }
    }

    override fun getTeamPlusMinusRank(teamId: Int): Flow<Int> {
        return teams.map { teams ->
            teams.sortedByDescending {
                it.plusMinus
            }.indexOfFirst {
                it.teamId == teamId
            }.plus(1)
        }
    }

    override fun getPlayerCareer(playerId: Int): Flow<PlayerCareer?> {
        return playersCareer.map { players ->
            players.firstOrNull {
                it.personId == playerId
            }
        }
    }

    override suspend fun login(account: String, password: String) {
        user.value = User(
            account = USER_ACCOUNT,
            name = USER_NAME,
            points = USER_POINTS,
            password = USER_PASSWORD,
            token = USER_TOKEN
        )
    }

    override suspend fun logout() {
        user.value = null
    }

    override suspend fun register(account: String, password: String) {
        user.value = User(
            account = USER_ACCOUNT,
            name = USER_NAME,
            points = USER_POINTS,
            password = USER_PASSWORD,
            token = USER_TOKEN
        )
    }

    override suspend fun updatePassword(password: String) {
        user.value ?: return
        user.value = User(
            account = USER_ACCOUNT,
            name = USER_NAME,
            points = USER_POINTS,
            password = password,
            token = USER_TOKEN
        )
    }

    override suspend fun updatePoints(points: Long) {
        user.value ?: return
        user.value = User(
            account = USER_ACCOUNT,
            name = USER_NAME,
            points = points,
            password = USER_PASSWORD,
            token = USER_TOKEN
        )
    }

    override suspend fun addPoints(points: Long) {
        val currentPoints = user.value?.points ?: return
        user.value = User(
            account = USER_ACCOUNT,
            name = USER_NAME,
            points = currentPoints + points,
            password = USER_PASSWORD,
            token = USER_TOKEN
        )
    }

    override suspend fun bet(gameId: String, homePoints: Long, awayPoints: Long) {
        val account = user.value?.account ?: return
        bets.value = bets.value
            .toMutableList()
            .apply {
                add(
                    Bets(
                        betsId = System.currentTimeMillis(),
                        account = account,
                        gameId = gameId,
                        homePoints = homePoints,
                        awayPoints = awayPoints
                    )
                )
            }
    }

    override fun getGamesAndBets(): Flow<List<NbaGameAndBet>> {
        return games.combine(bets) { games, bets ->
            games.map { game ->
                val betList = bets.filter { it.gameId == game.gameId }
                NbaGameAndBet(game, betList)
            }
        }
    }

    override fun getBetsAndGames(): Flow<List<BetAndNbaGame>> {
        return games.combine(bets) { games, bets ->
            bets.mapNotNull { bet ->
                val game = games.find { it.gameId == bet.gameId } ?: return@mapNotNull null
                BetAndNbaGame(bet, game)
            }
        }
    }

    override fun getBetsAndGames(account: String): Flow<List<BetAndNbaGame>> {
        return games.combine(bets) { games, bets ->
            bets.filter {
                it.account == account
            }.mapNotNull { bet ->
                val game = games.find { it.gameId == bet.gameId } ?: return@mapNotNull null
                BetAndNbaGame(bet, game)
            }
        }
    }

    override suspend fun deleteBets(bets: Bets) {
        this.bets.value = this.bets.value.filterNot { it == bets }
    }
}