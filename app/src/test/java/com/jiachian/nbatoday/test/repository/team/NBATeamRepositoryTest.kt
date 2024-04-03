package com.jiachian.nbatoday.test.repository.team

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.data.local.TeamGenerator
import com.jiachian.nbatoday.data.local.TeamPlayerGenerator
import com.jiachian.nbatoday.database.dao.TestTeamDao
import com.jiachian.nbatoday.service.TestTeamService
import com.jiachian.nbatoday.team.data.NBATeamRepository
import com.jiachian.nbatoday.team.data.model.local.TeamAndPlayers
import com.jiachian.nbatoday.team.data.model.local.TeamRank
import com.jiachian.nbatoday.team.data.model.local.teams.NBATeam
import com.jiachian.nbatoday.utils.assertIs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NBATeamRepositoryTest : BaseUnitTest() {
    private lateinit var repository: NBATeamRepository

    @Before
    fun setup() {
        repository = NBATeamRepository(
            dao = TestTeamDao(dataHolder),
            service = TestTeamService(),
        )
    }

    @Test
    fun `addTeams and check teams are added`() = runTest {
        repository.addTeams()
        dataHolder.teams.value.assertIs(
            listOf(
                TeamGenerator.getHome(),
                TeamGenerator.getAway(),
            )
        )
    }

    @Test
    fun `updateTeamPlayers and check players are added`() = runTest {
        repository.updateTeamPlayers(HomeTeamId)
        dataHolder.teamPlayers.value.assertIs(TeamPlayerGenerator.getHome())
    }

    @Test
    fun `getTeams and check list is correct`() = runTest {
        repository.addTeams()
        repository.getTeams(NBATeam.Conference.EAST)
            .stateIn(emptyList())
            .value
            .assertIs(listOf(TeamGenerator.getHome()))
    }

    @Test
    fun `getTeamAndPlayers and check team and players are correct`() = runTest {
        repository.addTeams()
        repository.updateTeamPlayers(HomeTeamId)
        repository.getTeamAndPlayers(HomeTeamId)
            .stateIn(null)
            .value
            .assertIs(
                TeamAndPlayers(
                    team = TeamGenerator.getHome(),
                    teamPlayers = TeamPlayerGenerator.getHome(),
                )
            )
    }

    @Test
    fun `getTeamRank and check teamRank is correct`() = runTest {
        repository.addTeams()
        repository.getTeamRank(HomeTeamId, NBATeam.Conference.EAST)
            .stateIn(TeamRank())
            .value
            .assertIs(TeamRank(1, 1, 1, 1, 1))
    }
}
