package com.jiachian.nbatoday.data.datastore

import androidx.test.core.app.ApplicationProvider
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserName
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.compose.theme.LakersColors
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.local.team.teamOfficial
import com.jiachian.nbatoday.data.remote.user.User
import com.jiachian.nbatoday.utils.NbaUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.empty
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NbaDataStoreTest {
    private val dataStore = NbaDataStore(ApplicationProvider.getApplicationContext())

    @Before
    fun setup() = runTest {
        dataStore.clear()
    }

    @After
    fun teardown() = runTest {
        dataStore.clear()
    }

    @Test
    fun preferencesKeys_checksKeyName_isCorrect() {
        assertThat(NbaDataStore.PreferencesKeys.STATS_COOKIES.name, `is`("stats_cookies"))
        assertThat(
            NbaDataStore.PreferencesKeys.RECORD_SCHEDULE_TODAY.name,
            `is`("record_schedule_today")
        )
        assertThat(NbaDataStore.PreferencesKeys.THEME_COLORS.name, `is`("theme_colors"))
        assertThat(NbaDataStore.PreferencesKeys.USER_DATA.name, `is`("user_data"))
    }

    @Test
    fun statsCookie_setValue_valueIsCorrect() = runTest {
        val emptyData = dataStore.statsCookies.first()
        assertThat(emptyData, `is`(empty()))
        val cookie = "test"
        dataStore.updateStatsCookies(setOf(cookie))
        val actualData = dataStore.statsCookies.first()
        assertThat(actualData, `is`(setOf(cookie)))
    }

    @Test
    fun recordScheduleToday_setValue_valueIsCorrect() = runTest {
        val emptyData = dataStore.recordScheduleToday.first()
        assertThat(emptyData, `is`(NbaUtils.formatDate(1990, 1, 1)))
        dataStore.updateRecordScheduleToday(2023, 1, 1)
        val actualData = dataStore.recordScheduleToday.first()
        assertThat(actualData, `is`(NbaUtils.formatDate(2023, 1, 1)))
    }

    @Test
    fun themeColors_setValue_valueIsCorrect() = runTest {
        val emptyData = dataStore.themeColors.first()
        assertThat(emptyData, `is`(LakersColors))
        val teamId = 1610612738
        dataStore.updateThemeColor(teamId)
        val actualData = dataStore.themeColors.first()
        val team = NBATeam.getTeamById(teamId) ?: teamOfficial
        assertThat(actualData, `is`(team.colors))
    }

    @Test
    fun userData_setValue_valueIsCorrect() = runTest {
        val emptyData = dataStore.userData.first()
        assertThat(emptyData, `is`(nullValue()))
        val user = User(UserAccount, UserName, UserPoints, UserPassword, "")
        dataStore.updateUser(user)
        val actualData = dataStore.userData.first()
        assertThat(actualData.account, `is`(user.account))
        assertThat(actualData.name, `is`(user.name))
        assertThat(actualData.points, `is`(user.points))
        assertThat(actualData.password, `is`(user.password))
        assertThat(actualData.token, `is`(user.token))
        dataStore.updateUser(null)
        val nullData = dataStore.userData.first()
        assertThat(nullData, `is`(nullValue()))
    }
}
