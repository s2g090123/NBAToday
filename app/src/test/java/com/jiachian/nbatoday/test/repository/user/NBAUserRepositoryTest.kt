package com.jiachian.nbatoday.test.repository.user

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.BetPoints
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.data.local.UserGenerator
import com.jiachian.nbatoday.datasource.remote.user.UserRemoteSource
import com.jiachian.nbatoday.home.user.data.NBAUserRepository
import com.jiachian.nbatoday.home.user.data.model.remote.UserDto
import com.jiachian.nbatoday.models.remote.user.extensions.toUser
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsNull
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
class NBAUserRepositoryTest : BaseUnitTest() {
    private lateinit var remoteSource: UserRemoteSource
    private lateinit var repository: NBAUserRepository

    @Before
    fun setup() {
        remoteSource = spyk(get<UserRemoteSource>())
        repository = NBAUserRepository(
            userRemoteSource = remoteSource,
            dataStore = dataStore
        )
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `user who had logged in expects correct`() = launch {
        dataStore.updateUser(UserGenerator.get(true))
        repository
            .user
            .stateIn(null)
            .value
            .assertIs(UserGenerator.get(true))
    }

    @Test
    fun `login() expects the user is updated`() = launch {
        repository.login(UserAccount, UserPassword)
        repository
            .user
            .stateIn(null)
            .value
            .assertIs(UserGenerator.get(true))
    }

    @Test
    fun `login() with error response expects onError is triggered`() = launch {
        coEvery {
            remoteSource.login(any(), any())
        } returns Response.error(404, "".toResponseBody())
        repository.login(UserAccount, UserPassword)
        repository
            .user
            .stateIn(null)
            .value
            .assertIsNull()
    }

    @Test
    fun `login() with unavailable user expects onError is triggered`() = launch {
        mockkStatic("com.jiachian.nbatoday.models.remote.user.extensions.RemoteUserExtKt")
        every {
            any<UserDto>().toUser()
        } returns UserGenerator.get(false)
        repository.login(UserAccount, UserPassword)
        repository
            .user
            .stateIn(null)
            .value
            .assertIsNull()
    }

    @Test
    fun `logout() expects the user is updated`() = launch {
        dataStore.updateUser(UserGenerator.get(true))
        repository.logout()
        repository
            .user
            .stateIn(null)
            .value
            .assertIsNull()
    }

    @Test
    fun `register() expects the user is updated`() = launch {
        repository.register(UserAccount, UserPassword)
        repository
            .user
            .stateIn(null)
            .value
            .assertIs(UserGenerator.get(true))
    }

    @Test
    fun `register() with error response expects onError is triggered`() = launch {
        coEvery {
            remoteSource.register(any(), any())
        } returns Response.error(404, "".toResponseBody())
        repository.register(UserAccount, UserPassword)
        repository
            .user
            .stateIn(null)
            .value
            .assertIsNull()
    }

    @Test
    fun `register() with unavailable user expects onError is triggered`() = launch {
        mockkStatic("com.jiachian.nbatoday.models.remote.user.extensions.RemoteUserExtKt")
        every {
            any<UserDto>().toUser()
        } returns UserGenerator.get(false)
        repository.register(UserAccount, UserPassword)
        repository
            .user
            .stateIn(null)
            .value
            .assertIsNull()
    }

    @Test
    fun `updatePoints() expects points are updated`() = launch {
        dataStore.updateUser(UserGenerator.get(true))
        repository.updatePoints(BetPoints)
        repository
            .user
            .stateIn(null)
            .value
            ?.points
            .assertIs(BetPoints)
    }

    @Test
    fun `updatePoints() with nonLoggIn user expects onError is triggered`() = launch {
        repository.updatePoints(BetPoints)
        repository
            .user
            .stateIn(null)
            .value
            ?.points
            .assertIsNull()
    }

    @Test
    fun `updatePoints() with error response expects onError is triggered`() = launch {
        dataStore.updateUser(UserGenerator.get(true))
        coEvery {
            remoteSource.updatePoints(any(), any(), any())
        } returns Response.error(404, "".toResponseBody())
        repository.updatePoints(BetPoints)
        repository
            .user
            .stateIn(null)
            .value
            ?.points
            .assertIs(UserPoints)
    }

    @Test
    fun `updatePoints() with unavailable user expects onError is triggered`() = launch {
        dataStore.updateUser(UserGenerator.get(false))
        repository.updatePoints(BetPoints)
        repository
            .user
            .stateIn(null)
            .value
            ?.points
            .assertIs(UserPoints)
    }

    @Test
    fun `addPoints() expects points are updated`() = launch {
        dataStore.updateUser(UserGenerator.get(true))
        repository.addPoints(BetPoints)
        repository
            .user
            .stateIn(null)
            .value
            ?.points
            .assertIs(UserPoints + BetPoints)
    }

    @Test
    fun `addPoints() with nonLoggIn user expects onError is triggered`() = launch {
        repository.addPoints(BetPoints)
        repository
            .user
            .stateIn(null)
            .value
            ?.points
            .assertIsNull()
    }

    @Test
    fun `addPoints() with error response expects onError is triggered`() = launch {
        dataStore.updateUser(UserGenerator.get(true))
        coEvery {
            remoteSource.updatePoints(any(), any(), any())
        } returns Response.error(404, "".toResponseBody())
        repository.addPoints(BetPoints)
        repository
            .user
            .stateIn(null)
            .value
            ?.points
            .assertIs(UserPoints)
    }

    @Test
    fun `addPoints() with unavailable user expects onError is triggered`() = launch {
        dataStore.updateUser(UserGenerator.get(false))
        repository.addPoints(BetPoints)
        repository
            .user
            .stateIn(null)
            .value
            ?.points
            .assertIs(UserPoints)
    }
}
