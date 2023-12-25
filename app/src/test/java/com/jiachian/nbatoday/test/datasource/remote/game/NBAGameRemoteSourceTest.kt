package com.jiachian.nbatoday.test.datasource.remote.game

import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.NBALeagueId
import com.jiachian.nbatoday.datasource.remote.game.NBAGameRemoteSource
import com.jiachian.nbatoday.service.GameService
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
        coEvery { mockedService.getSchedule() } returns Response.success(null)
        val response = remoteSource.getSchedule()
        assertIsTrue(response.isSuccessful)
    }

    @Test
    fun `getGame() expects the response is successful`() = runTest {
        coEvery { mockedService.getGame(any(), any()) } returns Response.success(null)
        val response = remoteSource.getGame(NBALeagueId, "")
        assertIsTrue(response.isSuccessful)
    }

    @Test
    fun `getGames() expects the response is successful`() = runTest {
        coEvery { mockedService.getGames(any(), any(), any(), any()) } returns Response.success(null)
        val response = remoteSource.getGames(0, 0, 0, 0)
        assertIsTrue(response.isSuccessful)
    }

    @Test
    fun `getBoxScore() expects the response is successful`() = runTest {
        coEvery { mockedService.getBoxScore(any()) } returns Response.success(null)
        val response = remoteSource.getBoxScore(FinalGameId)
        assertIsTrue(response.isSuccessful)
    }
}
