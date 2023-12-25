package com.jiachian.nbatoday.test.datasource.local.team

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.data.local.TeamGenerator
import com.jiachian.nbatoday.data.local.TeamPlayerGenerator
import com.jiachian.nbatoday.datasource.local.team.NBATeamLocalSource
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.TeamRank
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.koin.core.component.get

@OptIn(ExperimentalCoroutinesApi::class)
class NBATeamLocalSourceTest : BaseUnitTest() {
    private lateinit var localSource: NBATeamLocalSource

    @Before
    fun setup() {
        localSource = NBATeamLocalSource(get())
    }

    @Test
    fun `getTeams(EAST) expects empty`() {
        val actual =
            localSource.getTeams(NBATeam.Conference.EAST).stateIn(emptyList()).value
        assertIs(actual, emptyList())
    }

    @Test
    fun `getTeams(WEST) expects empty`() {
        val actual =
            localSource.getTeams(NBATeam.Conference.WEST).stateIn(emptyList()).value
        assertIs(actual, emptyList())
    }

    @Test
    fun `getTeams(EAST) with insertTeams expects empty`() = launch {
        val expected = listOf(TeamGenerator.getHome())
        localSource.insertTeams(expected)
        val actual =
            localSource.getTeams(NBATeam.Conference.EAST).stateIn(emptyList()).value
        assertIs(actual, expected)
    }

    @Test
    fun `getTeams(WEST) with insertTeams expects empty`() = launch {
        val expected = listOf(TeamGenerator.getAway())
        localSource.insertTeams(expected)
        val actual =
            localSource.getTeams(NBATeam.Conference.WEST).stateIn(emptyList()).value
        assertIs(actual, expected)
    }

    @Test
    fun `getTeamAndPlayers(home) expects teamPlayers are empty`() = launch {
        localSource.insertTeams(listOf(TeamGenerator.getHome()))
        val actual =
            localSource.getTeamAndPlayers(HomeTeamId).stateIn(null).value
        assertIs(actual?.teamPlayers, emptyList())
    }

    @Test
    fun `getTeamRank(home) expects correct`() = launch {
        val team = TeamGenerator.getHome()
        localSource.insertTeams(listOf(team))
        val actual =
            localSource.getTeamRank(team.teamId, team.teamConference).stateIn(TeamRank.default()).value
        val expected = TeamRank(
            standing = 1,
            pointsRank = 1,
            reboundsRank = 1,
            assistsRank = 1,
            plusMinusRank = 1
        )
        assertIs(actual, expected)
    }

    @Test
    fun `insertTeams(home) expects teams are inserted`() = launch {
        val team = TeamGenerator.getHome()
        val expected = listOf(team)
        localSource.insertTeams(expected)
        val actual =
            localSource.getTeams(team.teamConference).stateIn(emptyList()).value
        assertIs(actual, expected)
    }

    @Test
    fun `insertTeamPlayers(home) expects teamPlayers are inserted`() = launch {
        val team = TeamGenerator.getHome()
        val teamPlayer = TeamPlayerGenerator.getHome()
        val expected = listOf(teamPlayer)
        localSource.insertTeams(listOf(team))
        localSource.insertTeamPlayers(expected)
        val actual =
            localSource.getTeamAndPlayers(team.teamId).stateIn(null).value
        assertIs(actual?.teamPlayers, expected)
    }

    @Test
    fun `deleteTeamPlayers(home) expects teamPlayers are deleted`() = launch {
        val team = TeamGenerator.getHome()
        val teamPlayer = TeamPlayerGenerator.getHome()
        localSource.insertTeams(listOf(team))
        localSource.insertTeamPlayers(listOf(teamPlayer))
        localSource.deleteTeamPlayers(team.teamId, listOf(teamPlayer.playerId))
        val actual =
            localSource.getTeamAndPlayers(team.teamId).stateIn(null).value
        assertIsFalse(actual?.teamPlayers?.contains(teamPlayer))
    }
}
