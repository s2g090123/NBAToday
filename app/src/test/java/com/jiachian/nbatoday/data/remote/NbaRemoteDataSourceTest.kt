package com.jiachian.nbatoday.data.remote

import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.GameDate
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.NbaLeagueId
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserName
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.UserPoints
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
import com.jiachian.nbatoday.service.StatsNbaService
import io.mockk.every
import io.mockk.mockkConstructor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
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
                    name = UserName,
                    points = UserPoints,
                    password = UserPassword,
                    token = null
                )
            )
        }

        override suspend fun register(loginBody: LoginBody): Response<User> {
            return Response.success(
                User(
                    account = loginBody.account,
                    name = UserName,
                    points = UserPoints,
                    password = UserPassword,
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
        val actual = remoteDataSource.getScoreboard(NbaLeagueId, GameDate)
        val expected = RemoteGameFactory.getRemoteGameScoreboard()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getScoreboard_separateGameDate_checksResponseBody() = runTest {
        val actual = remoteDataSource.getScoreboard(NbaLeagueId, 2023, 1, 1, 0)
        val expected = listOf(RemoteGameFactory.getRemoteGameScoreboard())
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getGameBoxScore_checksResponseBody() = runTest {
        val actual = remoteDataSource.getGameBoxScore(FinalGameId)
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
        val actual = remoteDataSource.getTeamStats(HomeTeamId)
        val expected = RemoteTeamFactory.getRemoteTeamStats()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getTeamPlayersStats_checksResponseBody() = runTest {
        val actual = remoteDataSource.getTeamPlayersStats(HomeTeamId)
        val expected = RemoteTeamFactory.getRemoteTeamPlayerStats()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getPlayerInfo_checksResponseBody() = runTest {
        val actual = remoteDataSource.getPlayerInfo(HomePlayerId)
        val expected = RemotePlayerFactory.getRemotePlayerInfo()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getPlayerCareerStats_checksResponseBody() = runTest {
        val actual = remoteDataSource.getPlayerCareerStats(HomePlayerId)
        val expected = RemotePlayerFactory.getRemotePlayerStats()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getPlayerDetail_checksResponseBody() = runTest {
        val actual = remoteDataSource.getPlayerDetail(HomePlayerId)
        val expected = RemotePlayerFactory.getRemotePlayerDetail()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun login_checksResponseBody() = runTest {
        val actual = remoteDataSource.login(UserAccount, UserPassword)
        val expected = testNbaService.login(LoginBody(UserAccount, UserPassword)).body()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun register_checksResponseBody() = runTest {
        val actual = remoteDataSource.register(UserAccount, UserPassword)
        val expected = testNbaService.register(LoginBody(UserAccount, UserPassword)).body()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun updatePassword_checksResponseBody() = runTest {
        val actual = remoteDataSource.updatePassword(UserAccount, UserPassword, "")
        val expected =
            testNbaService.updatePassword(UpdatePasswordBody(UserAccount, UserPassword, ""))
                .body()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun updatePoints_checksResponseBody() = runTest {
        val actual = remoteDataSource.updatePoints(UserAccount, UserPoints, "")
        val expected =
            testNbaService.updatePoints(UpdatePointBody(UserAccount, "", UserPoints)).body()
        assertThat(actual, `is`(expected))
    }
}
