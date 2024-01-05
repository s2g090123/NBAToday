package com.jiachian.nbatoday.test.datasource.remote.user

import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.data.remote.RemoteUserGenerator
import com.jiachian.nbatoday.datasource.remote.user.NBAUserRemoteSource
import com.jiachian.nbatoday.service.UserService
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
class NBAUserRemoteSourceTest {
    private lateinit var mockedService: UserService
    private lateinit var remoteSource: NBAUserRemoteSource

    @Before
    fun setup() {
        mockedService = mockk()
        remoteSource = NBAUserRemoteSource(mockedService)
    }

    @After
    fun teardown() {
        unmockkObject(mockedService)
    }

    @Test
    fun `login() expects the response is successful`() = runTest {
        val expected = RemoteUserGenerator.get()
        coEvery { mockedService.login(any()) } returns Response.success(expected)
        val response = remoteSource.login(UserAccount, UserPassword)
        assertIsTrue(response.isSuccessful)
        assertIs(response.body(), expected)
    }

    @Test
    fun `register() expects the response is successful`() = runTest {
        val expected = RemoteUserGenerator.get()
        coEvery { mockedService.register(any()) } returns Response.success(expected)
        val response = remoteSource.register(UserAccount, UserPassword)
        assertIsTrue(response.isSuccessful)
        assertIs(response.body(), expected)
    }

    @Test
    fun `updatePassword() expects the response is successful`() = runTest {
        val expected = "successful"
        coEvery { mockedService.updatePassword(any()) } returns Response.success(expected)
        val response = remoteSource.updatePassword(UserAccount, UserPassword, "")
        assertIsTrue(response.isSuccessful)
        assertIs(response.body(), expected)
    }

    @Test
    fun `updatePoints() expects the response is successful`() = runTest {
        val expected = "successful"
        coEvery { mockedService.updatePoints(any()) } returns Response.success(expected)
        val response = remoteSource.updatePoints(UserAccount, UserPoints, "")
        assertIsTrue(response.isSuccessful)
        assertIs(response.body(), expected)
    }
}
