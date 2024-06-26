package com.jiachian.nbatoday.test.database.dao

import androidx.room.Room
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.common.data.database.NBADatabase
import com.jiachian.nbatoday.data.local.TeamGenerator
import com.jiachian.nbatoday.data.local.TeamPlayerGenerator
import com.jiachian.nbatoday.team.data.TeamDao
import com.jiachian.nbatoday.team.data.model.local.TeamAndPlayers
import com.jiachian.nbatoday.team.data.model.local.teams.NBATeam
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.collectOnce
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TeamDaoTest : BaseAndroidTest() {
    private lateinit var database: NBADatabase
    private lateinit var dao: TeamDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            context,
            NBADatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        dao = database.getTeamDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun teamDao_getTeams() = runTest {
        dao.addTeams(listOf(TeamGenerator.getHome()))
        dao.getTeams(NBATeam.Conference.EAST).collectOnce(this) {
            it.assertIs(listOf(TeamGenerator.getHome()))
        }
    }

    @Test
    fun teamDao_getTeamAndPlayers() = runTest {
        dao.addTeams(listOf(TeamGenerator.getHome()))
        dao.addTeamPlayers(listOf(TeamPlayerGenerator.getHome()))
        dao.getTeamAndPlayers(HomeTeamId).collectOnce(this) {
            it.assertIs(
                TeamAndPlayers(
                    team = TeamGenerator.getHome(),
                    teamPlayers = listOf(TeamPlayerGenerator.getHome())
                )
            )
        }
    }

    @Test
    fun teamDao_getTeamStanding() = runTest {
        insertTeams()
        dao.getTeamStanding(HomeTeamId, NBATeam.Conference.EAST).collectOnce(this) {
            it.assertIs(1)
        }
    }

    @Test
    fun teamDao_getPointsRank() = runTest {
        insertTeams()
        dao.getPointsRank(HomeTeamId).collectOnce(this) {
            it.assertIs(1)
        }
    }

    @Test
    fun teamDao_getReboundsRank() = runTest {
        insertTeams()
        dao.getReboundsRank(HomeTeamId).collectOnce(this) {
            it.assertIs(1)
        }
    }

    @Test
    fun teamDao_deleteTeamPlayers() = runTest {
        insertTeams()
        dao.addTeamPlayers(listOf(TeamPlayerGenerator.getHome()))
        dao.deleteTeamPlayers(HomeTeamId, listOf(HomePlayerId))
        dao.getTeamAndPlayers(HomeTeamId).collectOnce(this) {
            it.assertIs(
                TeamAndPlayers(
                    team = TeamGenerator.getHome(),
                    teamPlayers = emptyList()
                )
            )
        }
    }

    @Test
    fun teamDao_getAssistsRank() = runTest {
        insertTeams()
        dao.getAssistsRank(HomeTeamId).collectOnce(this) {
            it.assertIs(1)
        }
    }

    @Test
    fun teamDao_getPlusMinusRank() = runTest {
        insertTeams()
        dao.getPlusMinusRank(HomeTeamId).collectOnce(this) {
            it.assertIs(1)
        }
    }

    private suspend fun insertTeams() {
        dao.addTeams(
            listOf(
                TeamGenerator.getHome(),
                TeamGenerator.getAway()
            )
        )
    }
}
