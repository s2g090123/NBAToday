package com.jiachian.nbatoday.data.remote

import com.jiachian.nbatoday.FINAL_GAME_ID
import com.jiachian.nbatoday.GAME_DATE
import com.jiachian.nbatoday.HOME_PLAYER_ID
import com.jiachian.nbatoday.HOME_TEAM_ID
import com.jiachian.nbatoday.NBA_LEAGUE_ID
import com.jiachian.nbatoday.USER_ACCOUNT
import com.jiachian.nbatoday.USER_NAME
import com.jiachian.nbatoday.USER_PASSWORD
import com.jiachian.nbatoday.USER_POINTS
import com.jiachian.nbatoday.data.TestDataStore
import com.jiachian.nbatoday.data.remote.game.GameScoreboard
import com.jiachian.nbatoday.data.remote.game.Schedule
import com.jiachian.nbatoday.data.remote.player.RemotePlayerDetail
import com.jiachian.nbatoday.data.remote.player.RemotePlayerInfo
import com.jiachian.nbatoday.data.remote.player.RemotePlayerStats
import com.jiachian.nbatoday.data.remote.player.RemoteTeamPlayerStats
import com.jiachian.nbatoday.data.remote.score.RemoteGameBoxScore
import com.jiachian.nbatoday.data.remote.team.RemoteTeamStats
import com.jiachian.nbatoday.data.remote.user.LoginBody
import com.jiachian.nbatoday.data.remote.user.UpdatePasswordBody
import com.jiachian.nbatoday.data.remote.user.UpdatePointBody
import com.jiachian.nbatoday.data.remote.user.User
import com.jiachian.nbatoday.service.CdnNbaService
import com.jiachian.nbatoday.service.NbaService
import com.jiachian.nbatoday.service.StatsNbaService
import io.mockk.every
import io.mockk.mockkConstructor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit

@OptIn(ExperimentalCoroutinesApi::class)
class NbaRemoteDataSourceTest {
    private lateinit var remoteDataSource: NbaRemoteDataSource

    private val testNbaService = object : NbaService {
        override suspend fun getPlayerDetail(
            season: String,
            playerId: Int
        ): Response<RemotePlayerDetail> {
            return Response.success(RemotePlayerFactory.getRemotePlayerDetail())
        }

        override suspend fun getPlayerStats(
            season: String,
            playerId: Int
        ): Response<RemotePlayerStats> {
            return Response.success(RemotePlayerFactory.getRemotePlayerStats())
        }

        override suspend fun getPlayerInfo(playerId: Int): Response<RemotePlayerInfo> {
            return Response.success(RemotePlayerFactory.getRemotePlayerInfo())
        }

        override suspend fun getTeamPlayersStats(
            teamId: Int,
            season: String
        ): Response<RemoteTeamPlayerStats> {
            return Response.success(RemoteTeamFactory.getRemoteTeamPlayerStats())
        }

        override suspend fun getTeamStats(season: String, teamId: Int): Response<RemoteTeamStats> {
            return Response.success(RemoteTeamFactory.getRemoteTeamStats())
        }

        override suspend fun getTeamStats(season: String): Response<RemoteTeamStats> {
            return Response.success(RemoteTeamFactory.getRemoteTeamStats())
        }

        override suspend fun getScoreboard(
            leagueID: String,
            gameDate: String
        ): Response<GameScoreboard> {
            return Response.success(RemoteGameFactory.getRemoteGameScoreboard())
        }

        override suspend fun getScoreboards(
            leagueID: String,
            year: Int,
            month: Int,
            day: Int,
            total: Int
        ): Response<List<GameScoreboard>> {
            return Response.success(listOf(RemoteGameFactory.getRemoteGameScoreboard()))
        }

        override suspend fun getSchedule(): Response<Schedule> {
            return Response.success(RemoteGameFactory.getRemoteSchedule())
        }

        override suspend fun getGameBoxScore(gameId: String): Response<RemoteGameBoxScore> {
            return Response.success(RemoteGameFactory.getRemoteGameBoxScore())
        }

        override suspend fun login(loginBody: LoginBody): Response<User> {
            return Response.success(
                User(
                    account = loginBody.account,
                    name = USER_NAME,
                    points = USER_POINTS,
                    password = USER_PASSWORD,
                    token = null
                )
            )
        }

        override suspend fun register(loginBody: LoginBody): Response<User> {
            return Response.success(
                User(
                    account = loginBody.account,
                    name = USER_NAME,
                    points = USER_POINTS,
                    password = USER_PASSWORD,
                    token = null
                )
            )
        }

        override suspend fun updatePassword(updatePasswordBody: UpdatePasswordBody): Response<String> {
            return Response.success("success")
        }

        override suspend fun updatePoints(updatePointBody: UpdatePointBody): Response<String> {
            return Response.success("success")
        }

    }

    @Before
    fun setup() {
        mockkConstructor(Retrofit::class)
        every { anyConstructed<Retrofit>().create(NbaService::class.java) } returns testNbaService
        remoteDataSource = NbaRemoteDataSource(TestDataStore())
    }

    @Test
    fun cdnService_checksClassType() {
        assertThat(remoteDataSource.cdnService, instanceOf(CdnNbaService::class.java))
    }

    @Test
    fun statsService_checksClassType() {
        assertThat(remoteDataSource.statsService, instanceOf(StatsNbaService::class.java))
    }

    @Test
    fun getSchedule_checksResponseBody() = runTest {
        val actual = remoteDataSource.getSchedule()
        val expected = RemoteGameFactory.getRemoteSchedule()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getScoreboard_fullGameDate_checksResponseBody() = runTest {
        val actual = remoteDataSource.getScoreboard(NBA_LEAGUE_ID, GAME_DATE)
        val expected = RemoteGameFactory.getRemoteGameScoreboard()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getScoreboard_separateGameDate_checksResponseBody() = runTest {
        val actual = remoteDataSource.getScoreboard(NBA_LEAGUE_ID, 2023, 1, 1, 0)
        val expected = listOf(RemoteGameFactory.getRemoteGameScoreboard())
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getGameBoxScore_checksResponseBody() = runTest {
        val actual = remoteDataSource.getGameBoxScore(FINAL_GAME_ID)
        val expected = RemoteGameFactory.getRemoteGameBoxScore()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getTeamStats_checksResponseBody() = runTest {
        val actual = remoteDataSource.getTeamStats()
        val expected = RemoteTeamFactory.getRemoteTeamStats()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getTeamStats_teamId_checksResponseBody() = runTest {
        val actual = remoteDataSource.getTeamStats(HOME_TEAM_ID)
        val expected = RemoteTeamFactory.getRemoteTeamStats()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getTeamPlayersStats_checksResponseBody() = runTest {
        val actual = remoteDataSource.getTeamPlayersStats(HOME_TEAM_ID)
        val expected = RemoteTeamFactory.getRemoteTeamPlayerStats()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getPlayerInfo_checksResponseBody() = runTest {
        val actual = remoteDataSource.getPlayerInfo(HOME_PLAYER_ID)
        val expected = RemotePlayerFactory.getRemotePlayerInfo()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getPlayerCareerStats_checksResponseBody() = runTest {
        val actual = remoteDataSource.getPlayerCareerStats(HOME_PLAYER_ID)
        val expected = RemotePlayerFactory.getRemotePlayerStats()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getPlayerDetail_checksResponseBody() = runTest {
        val actual = remoteDataSource.getPlayerDetail(HOME_PLAYER_ID)
        val expected = RemotePlayerFactory.getRemotePlayerDetail()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun login_checksResponseBody() = runTest {
        val actual = remoteDataSource.login(USER_ACCOUNT, USER_PASSWORD)
        val expected = testNbaService.login(LoginBody(USER_ACCOUNT, USER_PASSWORD)).body()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun register_checksResponseBody() = runTest {
        val actual = remoteDataSource.register(USER_ACCOUNT, USER_PASSWORD)
        val expected = testNbaService.register(LoginBody(USER_ACCOUNT, USER_PASSWORD)).body()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun updatePassword_checksSuccess() = runTest {
        val actual = remoteDataSource.updatePassword(USER_ACCOUNT, USER_PASSWORD, "")
        assertThat(actual, `is`(Unit))
    }

    @Test
    fun updatePoints_checksSuccess() = runTest {
        val actual = remoteDataSource.updatePoints(USER_ACCOUNT, USER_POINTS, "")
        assertThat(actual, `is`(Unit))
    }
}