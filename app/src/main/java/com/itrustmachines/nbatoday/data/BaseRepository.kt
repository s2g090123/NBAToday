package com.itrustmachines.nbatoday.data

import com.itrustmachines.nbatoday.data.local.NbaGame
import kotlinx.coroutines.flow.Flow

interface BaseRepository {

    val dates: Flow<List<NbaGame.NbaGameDate>>

    val games: Flow<List<NbaGame>>

    suspend fun refreshSchedule()
}