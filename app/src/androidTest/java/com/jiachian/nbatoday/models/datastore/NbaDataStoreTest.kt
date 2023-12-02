package com.jiachian.nbatoday.models.datastore

import androidx.test.core.app.ApplicationProvider
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserName
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.compose.theme.LakersColors
import com.jiachian.nbatoday.datastore.NBADataStore
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.data.teamOfficial
import com.jiachian.nbatoday.models.local.user.User
import com.jiachian.nbatoday.utils.DateUtils
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
    private val dataStore = NBADataStore(ApplicationProvider.getApplicationContext())

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
        assertThat(NBADataStore.PreferencesKeys.STATS_COOKIES.name, `is`("stats_cookies"))
        assertThat(
            NBADataStore.PreferencesKeys.LAST_ACCESSED_DAY.name,
            `is`("record_schedule_today")
        )
        assertThat(NBADataStore.PreferencesKeys.THEME_COLORS_TEAM_ID.name, `is`("theme_colors"))
        assertThat(NBADataStore.PreferencesKeys.USER.name, `is`("user_data"))
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
        val emptyData = dataStore.lastAccessedDay.first()
        assertThat(emptyData, `is`(DateUtils.formatDate(1990, 1, 1)))
        dataStore.updateLastAccessedDay(2023, 1, 1)
        val actualData = dataStore.lastAccessedDay.first()
        assertThat(actualData, `is`(DateUtils.formatDate(2023, 1, 1)))
    }

    @Test
    fun themeColors_setValue_valueIsCorrect() = runTest {
        val emptyData = dataStore.themeColors.first()
        assertThat(emptyData, `is`(LakersColors))
        val teamId = 1610612738
        dataStore.updateThemeColorsTeamId(teamId)
        val actualData = dataStore.themeColors.first()
        val team = NBATeam.getTeamById(teamId) ?: teamOfficial
        assertThat(actualData, `is`(team.colors))
    }

    @Test
    fun userData_setValue_valueIsCorrect() = runTest {
        val emptyData = dataStore.user.first()
        assertThat(emptyData, `is`(nullValue()))
        val user = User(UserAccount, UserName, UserPoints, UserPassword, "")
        dataStore.updateUser(user)
        val actualData = dataStore.user.first()
        assertThat(actualData.account, `is`(user.account))
        assertThat(actualData.name, `is`(user.name))
        assertThat(actualData.points, `is`(user.points))
        assertThat(actualData.password, `is`(user.password))
        assertThat(actualData.token, `is`(user.token))
        dataStore.updateUser(null)
        val nullData = dataStore.user.first()
        assertThat(nullData, `is`(nullValue()))
    }
}
