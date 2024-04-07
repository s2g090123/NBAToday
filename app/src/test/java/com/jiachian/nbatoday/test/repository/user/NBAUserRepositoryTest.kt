package com.jiachian.nbatoday.test.repository.user

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.HomeTeamColors
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.common.data.Response
import com.jiachian.nbatoday.data.local.UserGenerator
import com.jiachian.nbatoday.home.user.data.NBAUserRepository
import com.jiachian.nbatoday.service.TestUserService
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import com.jiachian.nbatoday.utils.assertIsNotNull
import com.jiachian.nbatoday.utils.assertIsNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NBAUserRepositoryTest : BaseUnitTest() {
    private lateinit var repository: NBAUserRepository

    @Before
    fun setup() {
        repository = NBAUserRepository(
            service = TestUserService(),
            dataStore = dataStore,
        )
    }

    @Test
    fun `login and check user is updated`() = runTest {
        repository.login(UserAccount, UserPassword)
        repository.user
            .stateIn(null)
            .value
            .assertIs(UserGenerator.get(true))
    }

    @Test
    fun `logout and check user is null`() = runTest {
        repository.login(UserAccount, UserPassword)
        repository.logout()
        repository.user
            .stateIn(UserGenerator.get(true))
            .value
            .assertIsNull()
    }

    @Test
    fun `register and check user is updated`() = runTest {
        repository.register(UserAccount, UserPassword)
        repository.user
            .stateIn(null)
            .value
            .assertIs(UserGenerator.get(true))
    }

    @Test
    fun `addPoints and check points are updated`() = runTest {
        repository.login(UserAccount, UserPassword)
        repository.addPoints(100)
        repository.user
            .stateIn(null)
            .value
            .assertIsNotNull()
            .points
            .assertIs(UserPoints + 100)
    }

    @Test
    fun `addPoints user not login and check points are updated`() = runTest {
        repository.addPoints(100).assertIsA(Response.Error::class.java)
    }

    @Test
    fun `updateTheme and verify theme is updated`() = runTest {
        repository.updateTheme(HomeTeamId)
        repository
            .theme
            .stateIn(null)
            .value
            .assertIs(HomeTeamColors)
    }
}
