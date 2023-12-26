package com.jiachian.nbatoday.test.repository.player

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.data.local.PlayerGenerator
import com.jiachian.nbatoday.datasource.local.player.PlayerLocalSource
import com.jiachian.nbatoday.datasource.remote.player.PlayerRemoteSource
import com.jiachian.nbatoday.models.remote.player.RemotePlayer
import com.jiachian.nbatoday.models.remote.player.extensions.toPlayer
import com.jiachian.nbatoday.repository.player.NBAPlayerRepository
import com.jiachian.nbatoday.utils.assertIs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.component.get
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class NBAPlayerRepositoryTest : BaseUnitTest() {
    private lateinit var remoteSource: PlayerRemoteSource
    private lateinit var localSource: PlayerLocalSource
    private lateinit var repository: NBAPlayerRepository

    @Before
    fun setup() {
        remoteSource = spyk(get<PlayerRemoteSource>())
        localSource = get()
        repository = NBAPlayerRepository(
            playerLocalSource = localSource,
            playerRemoteSource = remoteSource
        )
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `insertPlayer(home) expects the player is inserted`() = launch {
        repository.insertPlayer(HomePlayerId)
        dataHolder
            .players
            .value
            .assertIs(listOf(PlayerGenerator.getHome()))
    }

    @Test
    fun `insertPlayer(home) with error response expects onError is triggered`() = launch {
        coEvery {
            remoteSource.getPlayer(any())
        } returns Response.error(404, "".toResponseBody())
        repository.insertPlayer(HomePlayerId)
        dataHolder
            .players
            .value
            .assertIs(emptyList())
    }

    @Test
    fun `insertPlayer(home) with error player expects onError is triggered`() = launch {
        mockkStatic("com.jiachian.nbatoday.models.remote.player.extensions.RemotePlayerExtKt")
        every {
            any<RemotePlayer>().toPlayer()
        } returns null
        repository.insertPlayer(HomePlayerId)
        dataHolder
            .players
            .value
            .assertIs(emptyList())
    }

    @Test
    fun `getPlayer(home) expects correct`() = launch {
        localSource.insertPlayer(PlayerGenerator.getHome())
        repository
            .getPlayer(HomePlayerId)
            .stateIn(null)
            .value
            .assertIs(PlayerGenerator.getHome())
    }
}
