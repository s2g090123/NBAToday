package com.jiachian.nbatoday.test.datasource.remote.team

import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.data.remote.RemoteTeamGenerator
import com.jiachian.nbatoday.data.remote.RemoteTeamPlayerGenerator
import com.jiachian.nbatoday.datasource.remote.team.NBATeamRemoteSource
import com.jiachian.nbatoday.service.TeamService
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsTrue
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class NBATeamRemoteSourceTest {
    private lateinit var mockedService: TeamService
    private lateinit var remoteSource: NBATeamRemoteSource

    @Before
    fun setup() {
        mockedService = mockk()
        remoteSource = NBATeamRemoteSource(mockedService)
    }

    @After
    fun teardown() {
        unmockkObject(mockedService)
    }

    @Test
    fun `getTeam() expects the response is successful`() = runTest {
        val expected = RemoteTeamGenerator.get()
        coEvery { mockedService.getTeam(any(), null) } returns Response.success(expected)
        val response = remoteSource.getTeam()
        assertIsTrue(response.isSuccessful)
        assertIs(response.body(), expected)
    }

    @Test
    fun `getTeam(home) expects the response is successful`() = runTest {
        val expected = RemoteTeamGenerator.get()
        coEvery { mockedService.getTeam(any(), any()) } returns Response.success(expected)
        val response = remoteSource.getTeam(HomeTeamId)
        assertIsTrue(response.isSuccessful)
        assertIs(response.body(), expected)
    }

    @Test
    fun `getTeamPlayer(home) expects the response is successful`() = runTest {
        val expected = RemoteTeamPlayerGenerator.get(HomeTeamId)
        coEvery { mockedService.getTeamPlayer(any(), any()) } returns Response.success(expected)
        val response = remoteSource.getTeamPlayer(HomeTeamId)
        assertIsTrue(response.isSuccessful)
        assertIs(response.body(), expected)
    }
}
