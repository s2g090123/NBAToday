package com.jiachian.nbatoday.test.datasource.remote.game

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.NBALeagueId
import com.jiachian.nbatoday.datasource.remote.game.NBAGameRemoteSource
import com.jiachian.nbatoday.service.GameService
import com.jiachian.nbatoday.utils.assertIsTrue
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class NBAGameRemoteSourceTest : BaseUnitTest() {
    private lateinit var remoteSource: NBAGameRemoteSource
    private val mockedService = mockk<GameService>()

    @Before
    fun setup() {
        remoteSource = NBAGameRemoteSource(mockedService)
    }

    @Test
    fun `getSchedule() expects the response is successful`() = launch {
        coEvery { mockedService.getSchedule() } returns Response.success(null)
        val response = remoteSource.getSchedule()
        assertIsTrue(response.isSuccessful)
    }

    @Test
    fun `getGame() expects the response is successful`() = launch {
        coEvery { mockedService.getGame(any(), any()) } returns Response.success(null)
        val response = remoteSource.getGame(NBALeagueId, "")
        assertIsTrue(response.isSuccessful)
    }

    @Test
    fun `getGames() expects the response is successful`() = launch {
        coEvery { mockedService.getGames(any(), any(), any(), any()) } returns Response.success(null)
        val response = remoteSource.getGames(0, 0, 0, 0)
        assertIsTrue(response.isSuccessful)
    }

    @Test
    fun `getBoxScore() expects the response is successful`() = launch {
        coEvery { mockedService.getBoxScore(any()) } returns Response.success(null)
        val response = remoteSource.getBoxScore(FinalGameId)
        assertIsTrue(response.isSuccessful)
    }
}
