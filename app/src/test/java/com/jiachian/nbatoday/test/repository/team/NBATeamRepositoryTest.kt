package com.jiachian.nbatoday.test.repository.team

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.data.local.TeamGenerator
import com.jiachian.nbatoday.data.local.TeamPlayerGenerator
import com.jiachian.nbatoday.datasource.local.team.TeamLocalSource
import com.jiachian.nbatoday.datasource.remote.team.TeamRemoteSource
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.TeamAndPlayers
import com.jiachian.nbatoday.models.local.team.TeamRank
import com.jiachian.nbatoday.repository.team.NBATeamRepository
import com.jiachian.nbatoday.utils.assertIs
import io.mockk.coEvery
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.component.get
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class NBATeamRepositoryTest : BaseUnitTest() {
    private lateinit var localSource: TeamLocalSource
    private lateinit var remotesource: TeamRemoteSource
    private lateinit var repository: NBATeamRepository

    @Before
    fun setup() {
        localSource = get()
        remotesource = spyk(get<TeamRemoteSource>())
        repository = NBATeamRepository(
            teamLocalSource = localSource,
            teamRemoteSource = remotesource
        )
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `insertTeams() expects teams are inserted`() = launch {
        repository.insertTeams()
        dataHolder
            .teams
            .value
            .assertIs(
                listOf(
                    TeamGenerator.getHome(),
                    TeamGenerator.getAway()
                )
            )
    }

    @Test
    fun `insertTeams() with error response expects onError is triggered`() = launch {
        coEvery {
            remotesource.getTeam()
        } returns Response.error(404, "".toResponseBody())
        repository.insertTeams()
        dataHolder
            .teams
            .value
            .assertIs(emptyList())
    }

    @Test
    fun `insertTeam(home) expects the team is inserted`() = launch {
        repository.insertTeam(HomeTeamId)
        dataHolder
            .teams
            .value
            .assertIs(listOf(TeamGenerator.getHome()))
    }

    @Test
    fun `insertTeam(home) with error response expects onError is triggered`() = launch {
        coEvery {
            remotesource.getTeam(any())
        } returns Response.error(404, "".toResponseBody())
        repository.insertTeam(HomeTeamId)
        dataHolder
            .teams
            .value
            .assertIs(emptyList())
    }

    @Test
    fun `updateTeamPlayers(home) expects players are updated`() = launch {
        repository.updateTeamPlayers(HomeTeamId)
        dataHolder
            .teamPlayers
            .value
            .assertIs(listOf(TeamPlayerGenerator.getHome()))
    }

    @Test
    fun `updateTeamPlayers(home) with error response expects players are updated`() = launch {
        coEvery {
            remotesource.getTeamPlayer(any())
        } returns Response.error(404, "".toResponseBody())
        repository.updateTeamPlayers(HomeTeamId)
        dataHolder
            .teamPlayers
            .value
            .assertIs(emptyList())
    }

    @Test
    fun `updateTeamPlayers(home) with deleting traded players expects players are updated`() = launch {
        localSource.insertTeams(listOf(TeamGenerator.getHome()))
        localSource.insertTeamPlayers(
            listOf(
                TeamPlayerGenerator.getHome(),
                TeamPlayerGenerator.getAway().copy(teamId = HomeTeamId)
            )
        )
        repository.updateTeamPlayers(HomeTeamId)
        dataHolder
            .teamPlayers
            .value
            .assertIs(listOf(TeamPlayerGenerator.getHome()))
    }

    @Test
    fun `getTeams(EAST) expects correct`() = launch {
        localSource.insertTeams(listOf(TeamGenerator.getHome()))
        repository
            .getTeams(NBATeam.Conference.EAST)
            .stateIn(emptyList())
            .value
            .assertIs(listOf(TeamGenerator.getHome()))
    }

    @Test
    fun `getTeamAndPlayers(home) expects correct`() = launch {
        localSource.insertTeams(listOf(TeamGenerator.getHome()))
        localSource.insertTeamPlayers(listOf(TeamPlayerGenerator.getHome()))
        repository
            .getTeamAndPlayers(HomeTeamId)
            .stateIn(null)
            .value
            .assertIs(
                TeamAndPlayers(
                    team = TeamGenerator.getHome(),
                    teamPlayers = listOf(TeamPlayerGenerator.getHome())
                )
            )
    }

    @Test
    fun `getTeamRank(home, EAST) expects correct`() = launch {
        localSource.insertTeams(listOf(TeamGenerator.getHome()))
        repository
            .getTeamRank(HomeTeamId, NBATeam.Conference.EAST)
            .stateIn(TeamRank.default())
            .value
            .assertIs(
                TeamRank(
                    standing = 1,
                    pointsRank = 1,
                    reboundsRank = 1,
                    assistsRank = 1,
                    plusMinusRank = 1
                )
            )
    }
}
