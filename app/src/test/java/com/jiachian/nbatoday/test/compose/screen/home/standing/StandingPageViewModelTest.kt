package com.jiachian.nbatoday.test.compose.screen.home.standing

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.compose.screen.home.standing.StandingPageViewModel
import com.jiachian.nbatoday.compose.screen.home.standing.models.StandingLabel
import com.jiachian.nbatoday.compose.screen.home.standing.models.StandingRowData
import com.jiachian.nbatoday.compose.screen.home.standing.models.StandingSorting
import com.jiachian.nbatoday.compose.screen.label.LabelHelper
import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.Team
import com.jiachian.nbatoday.repository.team.TeamRepository
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.koin.test.get

@OptIn(ExperimentalCoroutinesApi::class)
class StandingPageViewModelTest : BaseUnitTest() {
    private lateinit var viewModel: StandingPageViewModel

    private val expectedEastRowData: List<StandingRowData>
        get() {
            return get<TeamRepository>().getTeams(NBATeam.Conference.EAST).map { teams ->
                teams.toRowData().sortedWith(viewModel.eastSorting.value)
            }.stateIn(emptyList()).value
        }
    private val expectedWestRowData: List<StandingRowData>
        get() {
            return get<TeamRepository>().getTeams(NBATeam.Conference.WEST).map { teams ->
                teams.toRowData().sortedWith(viewModel.westSorting.value)
            }.stateIn(emptyList()).value
        }

    private val actualEastRowData: List<StandingRowData>?
        get() = viewModel.sortedEastRowDataState.value.getDataOrNull()
    private val actualWestRowData: List<StandingRowData>?
        get() = viewModel.sortedWestRowDataState.value.getDataOrNull()

    @Before
    fun setup() = runTest {
        get<TeamRepository>().insertTeams()
        viewModel = StandingPageViewModel(
            repository = get(),
            dispatcherProvider = dispatcherProvider,
        )
    }

    @Test
    fun `labels expects correct`() {
        assertIs(viewModel.labels, StandingLabel.values())
    }

    @Test
    fun `conferences expects correct`() {
        assertIs(viewModel.conferences, NBATeam.Conference.values())
    }

    @Test
    fun `sortedEastRowDataState expects correct`() {
        assertIsA(
            viewModel.sortedEastRowDataState.value,
            UIState.Loading::class.java
        )
        viewModel.sortedEastRowDataState.launchAndCollect()
        assertIsA(
            viewModel.sortedEastRowDataState.value,
            UIState.Loaded::class.java
        )
        assertIs(actualEastRowData, expectedEastRowData)
    }

    @Test
    fun `sortedWestRowDataState expects correct`() {
        assertIsA(
            viewModel.sortedWestRowDataState.value,
            UIState.Loading::class.java
        )
        viewModel.sortedWestRowDataState.launchAndCollect()
        assertIsA(
            viewModel.sortedWestRowDataState.value,
            UIState.Loaded::class.java
        )
        assertIs(actualWestRowData, expectedWestRowData)
    }

    @Test
    fun `updateTeamStats expects sortedRowDataState is correct`() {
        viewModel.updateTeamStats()
        viewModel.sortedEastRowDataState.launchAndCollect()
        viewModel.sortedWestRowDataState.launchAndCollect()
        assertIs(actualEastRowData, expectedEastRowData)
        assertIs(actualWestRowData, expectedWestRowData)
    }

    @Test
    fun `selectConference(East) expects selectedConference is EAST`() {
        viewModel.selectConference(NBATeam.Conference.EAST)
        assertIs(viewModel.getSelectedConference(), NBATeam.Conference.EAST)
    }

    @Test
    fun `selectConference(West) expects selectedConference is WEST`() {
        viewModel.selectConference(NBATeam.Conference.WEST)
        assertIs(viewModel.getSelectedConference(), NBATeam.Conference.WEST)
    }

    @Test
    fun `updateSorting(GP) expects sorting is updated`() {
        viewModel.sortedEastRowDataState.launchAndCollect()
        viewModel.sortedWestRowDataState.launchAndCollect()
        viewModel.selectConference(NBATeam.Conference.EAST)
        viewModel.updateSorting(StandingSorting.GP)
        viewModel.selectConference(NBATeam.Conference.WEST)
        viewModel.updateSorting(StandingSorting.GP)
        assertIs(viewModel.eastSorting.value, StandingSorting.GP)
        assertIs(viewModel.westSorting.value, StandingSorting.GP)
        assertIs(actualEastRowData, expectedEastRowData)
        assertIs(actualWestRowData, expectedWestRowData)
    }

    @Test
    fun `updateSorting(W) expects sorting is updated`() {
        viewModel.sortedEastRowDataState.launchAndCollect()
        viewModel.sortedWestRowDataState.launchAndCollect()
        viewModel.selectConference(NBATeam.Conference.EAST)
        viewModel.updateSorting(StandingSorting.W)
        viewModel.selectConference(NBATeam.Conference.WEST)
        viewModel.updateSorting(StandingSorting.W)
        assertIs(viewModel.eastSorting.value, StandingSorting.W)
        assertIs(viewModel.westSorting.value, StandingSorting.W)
        assertIs(actualEastRowData, expectedEastRowData)
        assertIs(actualWestRowData, expectedWestRowData)
    }

    @Test
    fun `updateSorting(L) expects sorting is updated`() {
        viewModel.sortedEastRowDataState.launchAndCollect()
        viewModel.sortedWestRowDataState.launchAndCollect()
        viewModel.selectConference(NBATeam.Conference.EAST)
        viewModel.updateSorting(StandingSorting.L)
        viewModel.selectConference(NBATeam.Conference.WEST)
        viewModel.updateSorting(StandingSorting.L)
        assertIs(viewModel.eastSorting.value, StandingSorting.L)
        assertIs(viewModel.westSorting.value, StandingSorting.L)
        assertIs(actualEastRowData, expectedEastRowData)
        assertIs(actualWestRowData, expectedWestRowData)
    }

    @Test
    fun `updateSorting(WINP) expects sorting is updated`() {
        viewModel.sortedEastRowDataState.launchAndCollect()
        viewModel.sortedWestRowDataState.launchAndCollect()
        viewModel.selectConference(NBATeam.Conference.EAST)
        viewModel.updateSorting(StandingSorting.WINP)
        viewModel.selectConference(NBATeam.Conference.WEST)
        viewModel.updateSorting(StandingSorting.WINP)
        assertIs(viewModel.eastSorting.value, StandingSorting.WINP)
        assertIs(viewModel.westSorting.value, StandingSorting.WINP)
        assertIs(actualEastRowData, expectedEastRowData)
        assertIs(actualWestRowData, expectedWestRowData)
    }

    @Test
    fun `updateSorting(PTS) expects sorting is updated`() {
        viewModel.sortedEastRowDataState.launchAndCollect()
        viewModel.sortedWestRowDataState.launchAndCollect()
        viewModel.selectConference(NBATeam.Conference.EAST)
        viewModel.updateSorting(StandingSorting.PTS)
        viewModel.selectConference(NBATeam.Conference.WEST)
        viewModel.updateSorting(StandingSorting.PTS)
        assertIs(viewModel.eastSorting.value, StandingSorting.PTS)
        assertIs(viewModel.westSorting.value, StandingSorting.PTS)
        assertIs(actualEastRowData, expectedEastRowData)
        assertIs(actualWestRowData, expectedWestRowData)
    }

    @Test
    fun `updateSorting(FGP) expects sorting is updated`() {
        viewModel.sortedEastRowDataState.launchAndCollect()
        viewModel.sortedWestRowDataState.launchAndCollect()
        viewModel.selectConference(NBATeam.Conference.EAST)
        viewModel.updateSorting(StandingSorting.FGP)
        viewModel.selectConference(NBATeam.Conference.WEST)
        viewModel.updateSorting(StandingSorting.FGP)
        assertIs(viewModel.eastSorting.value, StandingSorting.FGP)
        assertIs(viewModel.westSorting.value, StandingSorting.FGP)
        assertIs(actualEastRowData, expectedEastRowData)
        assertIs(actualWestRowData, expectedWestRowData)
    }

    @Test
    fun `updateSorting(PP3) expects sorting is updated`() {
        viewModel.sortedEastRowDataState.launchAndCollect()
        viewModel.sortedWestRowDataState.launchAndCollect()
        viewModel.selectConference(NBATeam.Conference.EAST)
        viewModel.updateSorting(StandingSorting.PP3)
        viewModel.selectConference(NBATeam.Conference.WEST)
        viewModel.updateSorting(StandingSorting.PP3)
        assertIs(viewModel.eastSorting.value, StandingSorting.PP3)
        assertIs(viewModel.westSorting.value, StandingSorting.PP3)
        assertIs(actualEastRowData, expectedEastRowData)
        assertIs(actualWestRowData, expectedWestRowData)
    }

    @Test
    fun `updateSorting(FTP) expects sorting is updated`() {
        viewModel.sortedEastRowDataState.launchAndCollect()
        viewModel.sortedWestRowDataState.launchAndCollect()
        viewModel.selectConference(NBATeam.Conference.EAST)
        viewModel.updateSorting(StandingSorting.FTP)
        viewModel.selectConference(NBATeam.Conference.WEST)
        viewModel.updateSorting(StandingSorting.FTP)
        assertIs(viewModel.eastSorting.value, StandingSorting.FTP)
        assertIs(viewModel.westSorting.value, StandingSorting.FTP)
        assertIs(actualEastRowData, expectedEastRowData)
        assertIs(actualWestRowData, expectedWestRowData)
    }

    @Test
    fun `updateSorting(OREB) expects sorting is updated`() {
        viewModel.sortedEastRowDataState.launchAndCollect()
        viewModel.sortedWestRowDataState.launchAndCollect()
        viewModel.selectConference(NBATeam.Conference.EAST)
        viewModel.updateSorting(StandingSorting.OREB)
        viewModel.selectConference(NBATeam.Conference.WEST)
        viewModel.updateSorting(StandingSorting.OREB)
        assertIs(viewModel.eastSorting.value, StandingSorting.OREB)
        assertIs(viewModel.westSorting.value, StandingSorting.OREB)
        assertIs(actualEastRowData, expectedEastRowData)
        assertIs(actualWestRowData, expectedWestRowData)
    }

    @Test
    fun `updateSorting(DREB) expects sorting is updated`() {
        viewModel.sortedEastRowDataState.launchAndCollect()
        viewModel.sortedWestRowDataState.launchAndCollect()
        viewModel.selectConference(NBATeam.Conference.EAST)
        viewModel.updateSorting(StandingSorting.DREB)
        viewModel.selectConference(NBATeam.Conference.WEST)
        viewModel.updateSorting(StandingSorting.DREB)
        assertIs(viewModel.eastSorting.value, StandingSorting.DREB)
        assertIs(viewModel.westSorting.value, StandingSorting.DREB)
        assertIs(actualEastRowData, expectedEastRowData)
        assertIs(actualWestRowData, expectedWestRowData)
    }

    @Test
    fun `updateSorting(AST) expects sorting is updated`() {
        viewModel.sortedEastRowDataState.launchAndCollect()
        viewModel.sortedWestRowDataState.launchAndCollect()
        viewModel.selectConference(NBATeam.Conference.EAST)
        viewModel.updateSorting(StandingSorting.AST)
        viewModel.selectConference(NBATeam.Conference.WEST)
        viewModel.updateSorting(StandingSorting.AST)
        assertIs(viewModel.eastSorting.value, StandingSorting.AST)
        assertIs(viewModel.westSorting.value, StandingSorting.AST)
        assertIs(actualEastRowData, expectedEastRowData)
        assertIs(actualWestRowData, expectedWestRowData)
    }

    @Test
    fun `updateSorting(TOV) expects sorting is updated`() {
        viewModel.sortedEastRowDataState.launchAndCollect()
        viewModel.sortedWestRowDataState.launchAndCollect()
        viewModel.selectConference(NBATeam.Conference.EAST)
        viewModel.updateSorting(StandingSorting.TOV)
        viewModel.selectConference(NBATeam.Conference.WEST)
        viewModel.updateSorting(StandingSorting.TOV)
        assertIs(viewModel.eastSorting.value, StandingSorting.TOV)
        assertIs(viewModel.westSorting.value, StandingSorting.TOV)
        assertIs(actualEastRowData, expectedEastRowData)
        assertIs(actualWestRowData, expectedWestRowData)
    }

    @Test
    fun `updateSorting(STL) expects sorting is updated`() {
        viewModel.sortedEastRowDataState.launchAndCollect()
        viewModel.sortedWestRowDataState.launchAndCollect()
        viewModel.selectConference(NBATeam.Conference.EAST)
        viewModel.updateSorting(StandingSorting.STL)
        viewModel.selectConference(NBATeam.Conference.WEST)
        viewModel.updateSorting(StandingSorting.STL)
        assertIs(viewModel.eastSorting.value, StandingSorting.STL)
        assertIs(viewModel.westSorting.value, StandingSorting.STL)
        assertIs(actualEastRowData, expectedEastRowData)
        assertIs(actualWestRowData, expectedWestRowData)
    }

    @Test
    fun `updateSorting(BLK) expects sorting is updated`() {
        viewModel.sortedEastRowDataState.launchAndCollect()
        viewModel.sortedWestRowDataState.launchAndCollect()
        viewModel.selectConference(NBATeam.Conference.EAST)
        viewModel.updateSorting(StandingSorting.BLK)
        viewModel.selectConference(NBATeam.Conference.WEST)
        viewModel.updateSorting(StandingSorting.BLK)
        assertIs(viewModel.eastSorting.value, StandingSorting.BLK)
        assertIs(viewModel.westSorting.value, StandingSorting.BLK)
        assertIs(actualEastRowData, expectedEastRowData)
        assertIs(actualWestRowData, expectedWestRowData)
    }

    private fun List<StandingRowData>.sortedWith(sorting: StandingSorting): List<StandingRowData> {
        val comparator = when (sorting) {
            StandingSorting.GP -> compareByDescending { it.team.gamePlayed }
            StandingSorting.W -> compareByDescending { it.team.win }
            StandingSorting.L -> compareBy { it.team.lose }
            StandingSorting.WINP -> compareByDescending { it.team.winPercentage }
            StandingSorting.PTS -> compareByDescending { it.team.pointsAverage }
            StandingSorting.FGP -> compareByDescending { it.team.fieldGoalsPercentage }
            StandingSorting.PP3 -> compareByDescending { it.team.threePointersPercentage }
            StandingSorting.FTP -> compareByDescending { it.team.freeThrowsPercentage }
            StandingSorting.OREB -> compareByDescending { it.team.reboundsOffensiveAverage }
            StandingSorting.DREB -> compareByDescending { it.team.reboundsDefensiveAverage }
            StandingSorting.AST -> compareByDescending { it.team.assistsAverage }
            StandingSorting.TOV -> compareBy { it.team.turnoversAverage }
            StandingSorting.STL -> compareByDescending { it.team.stealsAverage }
            StandingSorting.BLK -> compareByDescending<StandingRowData> { it.team.blocksAverage }
        }.thenByDescending { it.team.winPercentage }
        return sortedWith(comparator)
    }

    private fun List<Team>.toRowData(): List<StandingRowData> {
        return map { team ->
            StandingRowData(
                team = team,
                data = StandingLabel.values().map { label ->
                    StandingRowData.Data(
                        value = LabelHelper.getValueByLabel(label, team),
                        width = label.width,
                        align = label.align,
                        sorting = label.sorting,
                    )
                }
            )
        }
    }
}
