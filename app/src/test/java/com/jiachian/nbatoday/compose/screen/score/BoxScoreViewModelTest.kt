package com.jiachian.nbatoday.compose.screen.score

import com.jiachian.nbatoday.compose.screen.score.tab.BoxScoreTab
import com.jiachian.nbatoday.models.BoxScoreFactory
import com.jiachian.nbatoday.models.NbaGameFactory
import com.jiachian.nbatoday.models.TestRepository
import com.jiachian.nbatoday.rule.TestCoroutineEnvironment
import com.jiachian.nbatoday.utils.launchAndCollect
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BoxScoreViewModelTest {

    private lateinit var viewModel: BoxScoreViewModel
    private val repository = TestRepository()
    private val coroutineEnvironment = TestCoroutineEnvironment()
    private val game = NbaGameFactory.getFinalGame()

    @Before
    fun setup() {
        viewModel = BoxScoreViewModel(
            game = game,
            repository = repository,
            showPlayerCareer = { },
            dispatcherProvider = coroutineEnvironment.testDispatcherProvider
        )
    }

    @After
    fun teardown() {
        repository.clear()
    }

    @Test
    fun boxScore_checksBoxScore() {
        viewModel.boxScore.launchAndCollect(coroutineEnvironment)
        val expected = BoxScoreFactory.getFinalGameBoxScore()
        val boxScore = viewModel.boxScore.value
        assertThat(boxScore?.gameId, `is`(expected.gameId))
        assertThat(boxScore?.homeTeam, `is`(expected.homeTeam))
        assertThat(boxScore?.awayTeam, `is`(expected.awayTeam))
        assertThat(boxScore?.gameStatus, `is`(expected.gameStatus))
        assertThat(boxScore?.gameStatusText, `is`(expected.gameStatusText))
        assertThat(boxScore?.gameCode, `is`(expected.gameCode))
        assertThat(boxScore?.gameDate, `is`(expected.gameDate))
    }

    @Test
    fun boxScore_checksHomeLeader() {
        viewModel.homeLeader.launchAndCollect(coroutineEnvironment)
        val homeTeam = BoxScoreFactory.getFinalGameBoxScore().homeTeam
        val expected = homeTeam?.players?.first {
            it.playerId == game.gameLeaders?.homeLeader?.playerId
        }
        val homeLeader = viewModel.homeLeader.value
        assertThat(homeLeader, `is`(notNullValue()))
        assertThat(homeLeader?.playerId, `is`(expected?.playerId))
        assertThat(homeLeader?.name, `is`(expected?.name))
        assertThat(homeLeader?.nameAbbr, `is`(expected?.nameAbbr))
        assertThat(homeLeader?.position, `is`(expected?.position))
        assertThat(homeLeader?.status, `is`(expected?.status))
        assertThat(homeLeader?.statistics, `is`(expected?.statistics))
    }

    @Test
    fun boxScore_checksAwayLeader() {
        viewModel.awayLeader.launchAndCollect(coroutineEnvironment)
        val awayTeam = BoxScoreFactory.getFinalGameBoxScore().awayTeam
        val expected = awayTeam?.players?.first {
            it.playerId == game.gameLeaders?.awayLeader?.playerId
        }
        val awayLeader = viewModel.awayLeader.value
        assertThat(awayLeader, `is`(notNullValue()))
        assertThat(awayLeader?.playerId, `is`(expected?.playerId))
        assertThat(awayLeader?.name, `is`(expected?.name))
        assertThat(awayLeader?.nameAbbr, `is`(expected?.nameAbbr))
        assertThat(awayLeader?.position, `is`(expected?.position))
        assertThat(awayLeader?.status, `is`(expected?.status))
        assertThat(awayLeader?.statistics, `is`(expected?.statistics))
    }

    @Test
    fun boxScore_selectHomeTab_checksSelectPage() {
        viewModel.selectedTab.launchAndCollect(coroutineEnvironment)
        viewModel.selectTab(BoxScoreTab.HOME)
        assertThat(viewModel.selectedTab.value, `is`(BoxScoreTab.HOME))
    }

    @Test
    fun boxScore_selectAwayTab_checksSelectPage() {
        viewModel.selectedTab.launchAndCollect(coroutineEnvironment)
        viewModel.selectTab(BoxScoreTab.AWAY)
        assertThat(viewModel.selectedTab.value, `is`(BoxScoreTab.AWAY))
    }

    @Test
    fun boxScore_selectStatsTab_checksSelectPage() {
        viewModel.selectedTab.launchAndCollect(coroutineEnvironment)
        viewModel.selectTab(BoxScoreTab.STATS)
        assertThat(viewModel.selectedTab.value, `is`(BoxScoreTab.STATS))
    }

    @Test
    fun boxScore_selectLeaderTab_checksSelectPage() {
        viewModel.selectedTab.launchAndCollect(coroutineEnvironment)
        viewModel.selectTab(BoxScoreTab.LEADER)
        assertThat(viewModel.selectedTab.value, `is`(BoxScoreTab.LEADER))
    }

    @Test
    fun boxScore_refreshScore_checksBoxScore() {
        viewModel.boxScore.launchAndCollect(coroutineEnvironment)
        viewModel.refreshScore()
        val expected = BoxScoreFactory.getFinalGameBoxScore()
        val boxScore = viewModel.boxScore.value
        assertThat(boxScore?.gameId, `is`(expected.gameId))
        assertThat(boxScore?.homeTeam, `is`(expected.homeTeam))
        assertThat(boxScore?.awayTeam, `is`(expected.awayTeam))
        assertThat(boxScore?.gameStatus, `is`(expected.gameStatus))
        assertThat(boxScore?.gameStatusText, `is`(expected.gameStatusText))
        assertThat(boxScore?.gameCode, `is`(expected.gameCode))
        assertThat(boxScore?.gameDate, `is`(expected.gameDate))
    }
}
