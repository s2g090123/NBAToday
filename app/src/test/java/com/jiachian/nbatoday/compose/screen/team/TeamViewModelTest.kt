package com.jiachian.nbatoday.compose.screen.team

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.USER_ACCOUNT
import com.jiachian.nbatoday.USER_PASSWORD
import com.jiachian.nbatoday.USER_POINTS
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.data.NbaGameFactory
import com.jiachian.nbatoday.data.PlayerStatsFactory
import com.jiachian.nbatoday.data.TeamStatsFactory
import com.jiachian.nbatoday.data.TestRepository
import com.jiachian.nbatoday.data.local.team.DefaultTeam
import com.jiachian.nbatoday.rule.CalendarRule
import com.jiachian.nbatoday.rule.TestCoroutineEnvironment
import com.jiachian.nbatoday.utils.launchAndCollect
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TeamViewModelTest {

    private lateinit var viewModel: TeamViewModel
    private val repository = TestRepository()
    private val coroutineEnvironment = TestCoroutineEnvironment()
    private val homeTeamStats = TeamStatsFactory.getHomeTeamStats()
    private var currentState: NbaState? = null

    @get:Rule
    val calendarRule = CalendarRule()

    @Before
    fun setup() = runTest {
        repository.refreshSchedule()
        viewModel = TeamViewModel(
            teamId = homeTeamStats.teamId,
            repository = repository,
            openScreen = {
                currentState = it
            },
            dispatcherProvider = coroutineEnvironment.testDispatcherProvider,
            coroutineScope = coroutineEnvironment.testScope
        )
    }

    @After
    fun teardown() {
        currentState = null
        repository.clear()
    }

    @Test
    fun team_noLogin_checksUserNull() {
        viewModel.user.launchAndCollect(coroutineEnvironment)
        assertThat(viewModel.user.value, `is`(nullValue()))
    }

    @Test
    fun team_onLogin_checksUsersData() = runTest {
        viewModel.user.launchAndCollect(coroutineEnvironment)
        repository.login(USER_ACCOUNT, USER_PASSWORD)
        assertThat(viewModel.user.value?.account, `is`(USER_ACCOUNT))
        assertThat(viewModel.user.value?.password, `is`(USER_PASSWORD))
    }

    @Test
    fun team_checksTeamColors() {
        val expected = DefaultTeam.getColorsById(homeTeamStats.teamId)
        assertThat(viewModel.colors, `is`(expected))
    }

    @Test
    fun team_checksGamesBefore() {
        viewModel.gamesBefore.launchAndCollect(coroutineEnvironment)
        val finalGame = NbaGameFactory.getFinalGame()
        val playingGame = NbaGameFactory.getPlayingGame()
        val games = viewModel.gamesBefore.value.map { it.game }
        assertThat(games.size, `is`(2))
        assertThat(games.getOrNull(0)?.gameId, `is`(finalGame.gameId))
        assertThat(games.getOrNull(1)?.gameId, `is`(playingGame.gameId))
        assertThat(games.getOrNull(0)?.gameStatus, `is`(finalGame.gameStatus))
        assertThat(games.getOrNull(1)?.gameStatus, `is`(playingGame.gameStatus))
        assertThat(games.getOrNull(0)?.gameStatusText, `is`(finalGame.gameStatusText))
        assertThat(games.getOrNull(1)?.gameStatusText, `is`(playingGame.gameStatusText))
        assertThat(games.getOrNull(0)?.homeTeam, `is`(finalGame.homeTeam))
        assertThat(games.getOrNull(1)?.homeTeam, `is`(playingGame.homeTeam))
        assertThat(games.getOrNull(0)?.awayTeam, `is`(finalGame.awayTeam))
        assertThat(games.getOrNull(1)?.awayTeam, `is`(playingGame.awayTeam))
        assertThat(games.getOrNull(0)?.gameLeaders, `is`(finalGame.gameLeaders))
        assertThat(games.getOrNull(1)?.gameLeaders, `is`(playingGame.gameLeaders))
    }

    @Test
    fun team_checksGamesAfter() {
        viewModel.gamesAfter.launchAndCollect(coroutineEnvironment)
        val comingSoonGame = NbaGameFactory.getComingSoonGame()
        val games = viewModel.gamesAfter.value.map { it.game }
        assertThat(games.size, `is`(1))
        assertThat(games.getOrNull(0)?.gameId, `is`(comingSoonGame.gameId))
        assertThat(games.getOrNull(0)?.gameStatus, `is`(comingSoonGame.gameStatus))
        assertThat(games.getOrNull(0)?.gameStatusText, `is`(comingSoonGame.gameStatusText))
        assertThat(games.getOrNull(0)?.homeTeam, `is`(comingSoonGame.homeTeam))
        assertThat(games.getOrNull(0)?.awayTeam, `is`(comingSoonGame.awayTeam))
        assertThat(games.getOrNull(0)?.gameLeaders, `is`(comingSoonGame.gameLeaders))
    }

    @Test
    fun team_checksTeamStats() {
        viewModel.teamStats.launchAndCollect(coroutineEnvironment)
        val expected = TeamStatsFactory.getHomeTeamStats()
        assertThat(viewModel.teamStats.value, `is`(expected))
    }

    @Test
    fun team_checksTeamRank() {
        viewModel.teamRank.launchAndCollect(coroutineEnvironment)
        val expected = 1
        assertThat(viewModel.teamRank.value, `is`(expected))
    }

    @Test
    fun team_checksTeamPointsRank() {
        viewModel.teamPointsRank.launchAndCollect(coroutineEnvironment)
        val expected = 2
        assertThat(viewModel.teamPointsRank.value, `is`(expected))
    }

    @Test
    fun team_checksTeamReboundRank() {
        viewModel.teamReboundsRank.launchAndCollect(coroutineEnvironment)
        val expected = 2
        assertThat(viewModel.teamReboundsRank.value, `is`(expected))
    }

    @Test
    fun team_checksTeamAssistsRank() {
        viewModel.teamAssistsRank.launchAndCollect(coroutineEnvironment)
        val expected = 2
        assertThat(viewModel.teamAssistsRank.value, `is`(expected))
    }

    @Test
    fun team_checksTeamPlusMinusRank() {
        viewModel.teamRank.launchAndCollect(coroutineEnvironment)
        val expected = 2
        assertThat(viewModel.teamPlusMinusRank.value, `is`(expected))
    }

    @Test
    fun team_updateStats_checksPlayerStats() {
        viewModel.playersStats.launchAndCollect(coroutineEnvironment)
        viewModel.updateStats()
        val homePlayerStats = PlayerStatsFactory.getHomePlayerStats()
        assertThat(viewModel.playersStats.value.size, `is`(1))
        val stats = viewModel.playersStats.value.first()
        assertThat(stats.playerId, `is`(homePlayerStats.playerId))
        assertThat(stats.win, `is`(homePlayerStats.win))
        assertThat(stats.lose, `is`(homePlayerStats.lose))
        assertThat(stats.points, `is`(homePlayerStats.points))
        assertThat(stats.reboundsTotal, `is`(homePlayerStats.reboundsTotal))
        assertThat(stats.assists, `is`(homePlayerStats.assists))
        assertThat(stats.steals, `is`(homePlayerStats.steals))
        assertThat(stats.plusMinus, `is`(homePlayerStats.plusMinus))
    }

    @Test
    fun team_selectPlayersTab_checksPlayersSelect() {
        viewModel.selectPage.launchAndCollect(coroutineEnvironment)
        viewModel.updateSelectPage(TeamPageTab.PLAYERS)
        assertThat(viewModel.selectPage.value, `is`(TeamPageTab.PLAYERS))
    }

    @Test
    fun team_selectPreviousTab_checksPreviousSelect() {
        viewModel.selectPage.launchAndCollect(coroutineEnvironment)
        viewModel.updateSelectPage(TeamPageTab.PREVIOUS)
        assertThat(viewModel.selectPage.value, `is`(TeamPageTab.PREVIOUS))
    }

    @Test
    fun team_selectNextTab_checksNextSelect() {
        viewModel.selectPage.launchAndCollect(coroutineEnvironment)
        viewModel.updateSelectPage(TeamPageTab.NEXT)
        assertThat(viewModel.selectPage.value, `is`(TeamPageTab.NEXT))
    }

    @Test
    fun team_selectGamePlayedSort_checksGamePlaySelect() {
        viewModel.playerSort.launchAndCollect(coroutineEnvironment)
        val expected = PlayerLabel(1.dp, "TEST", TextAlign.Center, PlayerSort.GP)
        viewModel.updatePlayerSort(expected)
        assertThat(viewModel.playerSort.value, `is`(expected.sort))
    }

    @Test
    fun team_openGameBoxScore_boxScoreState() {
        viewModel.openGameBoxScore(NbaGameFactory.getComingSoonGame())
        assertThat(currentState, instanceOf(NbaState.BoxScore::class.java))
    }

    @Test
    fun team_openPlayerInfo_playerState() {
        viewModel.openPlayerInfo(PlayerStatsFactory.getHomePlayerStats().playerId)
        assertThat(currentState, instanceOf(NbaState.Player::class.java))
    }

    @Test
    fun team_login_checksUserInfo() {
        viewModel.user.launchAndCollect(coroutineEnvironment)
        viewModel.login(USER_ACCOUNT, USER_PASSWORD)
        assertThat(viewModel.user.value?.account, `is`(USER_ACCOUNT))
        assertThat(viewModel.user.value?.password, `is`(USER_PASSWORD))
    }

    @Test
    fun team_register_checksUserInfo() {
        viewModel.user.launchAndCollect(coroutineEnvironment)
        viewModel.register(USER_ACCOUNT, USER_PASSWORD)
        assertThat(viewModel.user.value?.account, `is`(USER_ACCOUNT))
        assertThat(viewModel.user.value?.password, `is`(USER_PASSWORD))
    }

    @Test
    fun team_betComingSoonGame_checkGamesAfter() {
        viewModel.gamesAfter.launchAndCollect(coroutineEnvironment)
        val gameId = NbaGameFactory.getComingSoonGame().gameId
        viewModel.login(USER_ACCOUNT, USER_PASSWORD)
        viewModel.bet(gameId, USER_POINTS, 0)
        val game = viewModel.gamesAfter.value.first()
        val bet = game.bets.first()
        assertThat(bet.gameId, `is`(gameId))
        assertThat(bet.account, `is`(USER_ACCOUNT))
        assertThat(bet.homePoints, `is`(USER_POINTS))
        assertThat(bet.awayPoints, `is`(0))
    }
}