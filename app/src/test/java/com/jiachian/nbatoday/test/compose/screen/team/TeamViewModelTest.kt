package com.jiachian.nbatoday.test.compose.screen.team

import androidx.lifecycle.SavedStateHandle
import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.ComingSoonGameId
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.compose.screen.label.LabelHelper
import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.compose.screen.team.TeamViewModel
import com.jiachian.nbatoday.compose.screen.team.models.TeamPlayerLabel
import com.jiachian.nbatoday.compose.screen.team.models.TeamPlayerRowData
import com.jiachian.nbatoday.compose.screen.team.models.TeamPlayerSorting
import com.jiachian.nbatoday.data.local.TeamGenerator
import com.jiachian.nbatoday.data.local.TeamPlayerGenerator
import com.jiachian.nbatoday.models.local.game.toGameCardState
import com.jiachian.nbatoday.models.local.team.Team
import com.jiachian.nbatoday.models.local.team.TeamPlayer
import com.jiachian.nbatoday.models.local.team.TeamRank
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.rule.SetMainDispatcherRule
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.get

@OptIn(ExperimentalCoroutinesApi::class)
class TeamViewModelTest : BaseUnitTest() {
    @get:Rule
    val mainDispatcherRule = SetMainDispatcherRule(dispatcherProvider)

    private lateinit var viewModel: TeamViewModel

    private val team: Team
        get() = TeamGenerator.getHome()
    private val player: TeamPlayer
        get() = TeamPlayerGenerator.getHome()

    private val playerRowData: List<TeamPlayerRowData>
        get() {
            return listOf(
                TeamPlayerRowData(
                    player = player,
                    data = TeamPlayerLabel.values().map { label ->
                        TeamPlayerRowData.Data(
                            value = LabelHelper.getValueByLabel(label, player),
                            width = label.width,
                            sorting = label.sorting,
                        )
                    }
                )
            ).sortedWith(viewModel.playerSorting.value)
        }

    private val teamRank: TeamRank
        get() {
            return repositoryProvider
                .team
                .getTeamRank(team.teamId, team.teamConference)
                .stateIn(TeamRank.default())
                .value
        }

    @Before
    fun setup() = runTest {
        repositoryProvider.schedule.updateSchedule()
        repositoryProvider.team.addTeams()
        viewModel = TeamViewModel(
            savedStateHandle = SavedStateHandle(mapOf(MainRoute.Team.param to "${team.teamId}")),
            teamRepository = get(),
            gameRepository = get(),
            dispatcherProvider = dispatcherProvider
        )
    }

    @Test
    fun `colors expects correct`() {
        assertIs(viewModel.colors, team.team.colors)
    }

    @Test
    fun `labels expects correct`() {
        assertIs(viewModel.labels, TeamPlayerLabel.values())
    }

    @Test
    fun `gamesBefore expects correct`() {
        assertIsA(
            viewModel.gamesBefore.value,
            UIState.Loading::class.java
        )
        viewModel.gamesBefore.launchAndCollect()
        assertIsA(
            viewModel.gamesBefore.value,
            UIState.Loaded::class.java
        )
        val actual = viewModel.gamesBefore.value.getDataOrNull()
        val expected = dataHolder
            .gamesAndBets
            .map { games -> games.filter { it.game.gameId == FinalGameId } }
            .stateIn(emptyList())
            .value
            .toGameCardState()
        assertIs(actual, expected)
    }

    @Test
    fun `gamesAfter expects correct`() {
        assertIsA(
            viewModel.gamesAfter.value,
            UIState.Loading::class.java
        )
        viewModel.gamesAfter.launchAndCollect()
        assertIsA(
            viewModel.gamesAfter.value,
            UIState.Loaded::class.java
        )
        val actual = viewModel.gamesAfter.value.getDataOrNull()
        val expected = dataHolder
            .gamesAndBets
            .map { games -> games.filter { it.game.gameId in listOf(PlayingGameId, ComingSoonGameId) } }
            .stateIn(emptyList())
            .value
            .toGameCardState()
        assertIs(actual, expected)
    }

    @Test
    fun `teamUIState expects correct`() {
        assertIsA(
            viewModel.teamUIState.value,
            UIState.Loading::class.java
        )
        viewModel.teamUIState.launchAndCollect()
        assertIsA(
            viewModel.teamUIState.value,
            UIState.Loaded::class.java
        )
        val actual = viewModel.teamUIState.value.getDataOrNull()
        val expected = TeamUI(
            team = team,
            rank = teamRank,
            players = playerRowData,
        )
        assertIs(actual, expected)
    }

    @Test
    fun `updatePlayerSorting expects playerSorting and uiState are updated`() {
        viewModel.teamUIState.launchAndCollect()
        TeamPlayerSorting.values().forEach { sorting ->
            viewModel.updatePlayerSorting(sorting)
            assertIs(viewModel.playerSorting.value, sorting)
            val actual = viewModel.teamUIState.value.getDataOrNull()
            val expected = TeamUI(
                team = team,
                rank = teamRank,
                players = playerRowData,
            )
            assertIs(actual, expected)
        }
    }

    private fun List<TeamPlayerRowData>.sortedWith(sorting: TeamPlayerSorting): List<TeamPlayerRowData> {
        val comparator = when (sorting) {
            TeamPlayerSorting.GP -> compareByDescending { it.player.gamePlayed }
            TeamPlayerSorting.W -> compareByDescending { it.player.win }
            TeamPlayerSorting.L -> compareBy { it.player.lose }
            TeamPlayerSorting.WINP -> compareByDescending { it.player.winPercentage }
            TeamPlayerSorting.PTS -> compareByDescending { it.player.pointsAverage }
            TeamPlayerSorting.FGM -> compareByDescending { it.player.fieldGoalsMadeAverage }
            TeamPlayerSorting.FGA -> compareByDescending { it.player.fieldGoalsAttemptedAverage }
            TeamPlayerSorting.FGP -> compareByDescending { it.player.fieldGoalsPercentage }
            TeamPlayerSorting.PM3 -> compareByDescending { it.player.threePointersMadeAverage }
            TeamPlayerSorting.PA3 -> compareByDescending { it.player.threePointersAttemptedAverage }
            TeamPlayerSorting.PP3 -> compareByDescending { it.player.threePointersPercentage }
            TeamPlayerSorting.FTM -> compareByDescending { it.player.freeThrowsMadeAverage }
            TeamPlayerSorting.FTA -> compareByDescending { it.player.freeThrowsAttemptedAverage }
            TeamPlayerSorting.FTP -> compareByDescending { it.player.freeThrowsPercentage }
            TeamPlayerSorting.OREB -> compareByDescending { it.player.reboundsOffensiveAverage }
            TeamPlayerSorting.DREB -> compareByDescending { it.player.reboundsDefensiveAverage }
            TeamPlayerSorting.REB -> compareByDescending { it.player.reboundsTotalAverage }
            TeamPlayerSorting.AST -> compareByDescending { it.player.assistsAverage }
            TeamPlayerSorting.TOV -> compareBy { it.player.turnoversAverage }
            TeamPlayerSorting.STL -> compareByDescending { it.player.stealsAverage }
            TeamPlayerSorting.BLK -> compareByDescending { it.player.blocksAverage }
            TeamPlayerSorting.PF -> compareBy { it.player.foulsPersonalAverage }
            TeamPlayerSorting.PLUSMINUS -> compareByDescending<TeamPlayerRowData> { it.player.plusMinus }
        }.thenByDescending { it.player.winPercentage }
        return sortedWith(comparator)
    }
}
