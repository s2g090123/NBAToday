package com.jiachian.nbatoday.test.datasource.remote.player

import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.data.remote.RemotePlayerGenerator
import com.jiachian.nbatoday.datasource.remote.player.NBAPlayerRemoteSource
import com.jiachian.nbatoday.player.data.PlayerService
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
class NBAPlayerRemoteSourceTest {
    private lateinit var mockedService: PlayerService
    private lateinit var remoteSource: NBAPlayerRemoteSource

    @Before
    fun setup() {
        mockedService = mockk()
        remoteSource = NBAPlayerRemoteSource(mockedService)
    }

    @After
    fun teardown() {
        unmockkObject(mockedService)
    }

    @Test
    fun `getPlayer(home) expects the response is successful`() = runTest {
        val expected = RemotePlayerGenerator.get(HomePlayerId)
        coEvery { mockedService.getPlayer(any(), any()) } returns Response.success(expected)
        val response = remoteSource.getPlayer(HomePlayerId)
        assertIsTrue(response.isSuccessful)
        assertIs(response.body(), expected)
    }
}
