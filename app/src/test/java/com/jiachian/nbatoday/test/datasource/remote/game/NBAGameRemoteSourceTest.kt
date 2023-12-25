package com.jiachian.nbatoday.test.datasource.remote.game

import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.NBALeagueId
import com.jiachian.nbatoday.data.remote.RemoteBoxScoreGenerator
import com.jiachian.nbatoday.data.remote.RemoteGameGenerator
import com.jiachian.nbatoday.data.remote.RemoteScheduleGenerator
import com.jiachian.nbatoday.datasource.remote.game.NBAGameRemoteSource
import com.jiachian.nbatoday.service.GameService
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
class NBAGameRemoteSourceTest {
    private lateinit var remoteSource: NBAGameRemoteSource
    private lateinit var mockedService: GameService

    @Before
    fun setup() {
        mockedService = mockk()
        remoteSource = NBAGameRemoteSource(mockedService)
    }

    @After
    fun teardown() {
        unmockkObject(mockedService)
    }

    @Test
    fun `getSchedule() expects the response is successful`() = runTest {
        val expected = RemoteScheduleGenerator.get()
        coEvery { mockedService.getSchedule() } returns Response.success(expected)
        val response = remoteSource.getSchedule()
        assertIsTrue(response.isSuccessful)
        assertIs(response.body(), expected)
    }

    @Test
    fun `getGame() expects the response is successful`() = runTest {
        val expected = RemoteGameGenerator.get()
        coEvery { mockedService.getGame(any(), any()) } returns Response.success(expected)
        val response = remoteSource.getGame(NBALeagueId, "")
        assertIsTrue(response.isSuccessful)
        assertIs(response.body(), expected)
    }

    @Test
    fun `getGames() expects the response is successful`() = runTest {
        val expected = listOf(RemoteGameGenerator.get())
        coEvery { mockedService.getGames(any(), any(), any(), any()) } returns Response.success(expected)
        val response = remoteSource.getGames(0, 0, 0, 0)
        assertIsTrue(response.isSuccessful)
        assertIs(response.body(), expected)
    }

    @Test
    fun `getBoxScore() expects the response is successful`() = runTest {
        val expected = RemoteBoxScoreGenerator.get(FinalGameId)
        coEvery { mockedService.getBoxScore(any()) } returns Response.success(expected)
        val response = remoteSource.getBoxScore(FinalGameId)
        assertIsTrue(response.isSuccessful)
        assertIs(response.body(), expected)
    }
}
