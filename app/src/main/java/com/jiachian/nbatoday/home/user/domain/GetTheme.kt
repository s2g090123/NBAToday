package com.jiachian.nbatoday.home.user.domain

import com.jiachian.nbatoday.common.data.datastore.BaseDataStore
import com.jiachian.nbatoday.common.ui.theme.NBAColors
import kotlinx.coroutines.flow.Flow

class GetTheme(
    private val dataStore: BaseDataStore
) {
    operator fun invoke(): Flow<NBAColors> {
        return dataStore.themeColors
    }
}
