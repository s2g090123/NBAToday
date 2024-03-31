package com.jiachian.nbatoday.test.datastore

import androidx.test.core.app.ApplicationProvider
import com.jiachian.nbatoday.HomeTeamColors
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.common.data.datastore.NBADataStore
import com.jiachian.nbatoday.data.local.UserGenerator
import com.jiachian.nbatoday.utils.DateUtils
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.collectOnce
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NBADataStoreTest {
    private val dataStore = NBADataStore(
        ApplicationProvider.getApplicationContext(),
        "nba_test_data_store"
    )

    @After
    fun teardown() = runTest {
        dataStore.clear()
    }

    @Test
    fun dataStore_updateLastAccessedDate() = runTest {
        dataStore.updateLastAccessedDate(2023, 12, 31)
        dataStore.lastAccessedDate.collectOnce(this) {
            it.assertIs(DateUtils.formatDate(2023, 12, 31))
        }

        dataStore.updateThemeColorsTeamId(HomeTeamId)
        dataStore.themeColors.collectOnce(this) {
            it.assertIs(HomeTeamColors)
        }

        dataStore.updateUser(UserGenerator.get(true))
        dataStore.user.collectOnce(this) {
            it.assertIs(UserGenerator.get(true))
        }
    }
}
