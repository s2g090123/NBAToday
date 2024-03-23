package com.jiachian.nbatoday.usecase.user

import com.jiachian.nbatoday.compose.theme.NBAColors
import com.jiachian.nbatoday.datastore.BaseDataStore
import kotlinx.coroutines.flow.Flow

class GetTheme(
    private val dataStore: BaseDataStore
) {
    operator fun invoke(): Flow<NBAColors> {
        return dataStore.themeColors
    }
}
