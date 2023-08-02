package com.jiachian.nbatoday.data.datastore

import androidx.test.core.app.ApplicationProvider
import com.jiachian.nbatoday.USER_ACCOUNT
import com.jiachian.nbatoday.USER_NAME
import com.jiachian.nbatoday.USER_PASSWORD
import com.jiachian.nbatoday.USER_POINTS
import com.jiachian.nbatoday.compose.theme.LakersColors
import com.jiachian.nbatoday.data.local.team.DefaultTeam
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
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NbaDataStoreTest {
    private val dataStore = NbaDataStore(ApplicationProvider.getApplicationContext())

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
        val expectData = dataStore.statsCookies.first()
        assertThat(expectData, `is`(setOf(cookie)))
    }

    @Test
    fun recordScheduleToday_setValue_valueIsCorrect() = runTest {
        val emptyData = dataStore.recordScheduleToday.first()
        assertThat(emptyData, `is`(NbaUtils.formatDate(1990, 1, 1)))
        dataStore.updateRecordScheduleToday(2023, 1, 1)
        val expectData = dataStore.recordScheduleToday.first()
        assertThat(expectData, `is`(NbaUtils.formatDate(2023, 1, 1)))
    }

    @Test
    fun themeColors_setValue_valueIsCorrect() = runTest {
        val emptyData = dataStore.themeColors.first()
        assertThat(emptyData, `is`(LakersColors))
        val teamId = 1610612738
        dataStore.updateThemeColor(teamId)
        val expectData = dataStore.themeColors.first()
        assertThat(expectData, `is`(DefaultTeam.getColorsById(teamId)))
    }

    @Test
    fun userData_setValue_valueIsCorrect() = runTest {
        val emptyData = dataStore.userData.first()
        assertThat(emptyData, `is`(nullValue()))
        val user = User(USER_ACCOUNT, USER_NAME, USER_POINTS, USER_PASSWORD, "")
        dataStore.updateUser(user)
        val expectData = dataStore.userData.first()
        assertThat(expectData.account, `is`(user.account))
        assertThat(expectData.name, `is`(user.name))
        assertThat(expectData.points, `is`(user.points))
        assertThat(expectData.password, `is`(user.password))
        assertThat(expectData.token, `is`(user.token))
        dataStore.updateUser(null)
        val nullData = dataStore.userData.first()
        assertThat(nullData, `is`(nullValue()))
    }
}