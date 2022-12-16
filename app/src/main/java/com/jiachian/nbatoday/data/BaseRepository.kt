package com.jiachian.nbatoday.data

import com.jiachian.nbatoday.data.local.NbaGame
import kotlinx.coroutines.flow.Flow
import java.util.*

interface BaseRepository {

    val dates: Flow<List<Date>>

    val games: Flow<List<NbaGame>>

    suspend fun refreshSchedule()
    suspend fun refreshSchedule(year: Int, month: Int, day: Int)

    fun getGamesDuring(from: Long, to: Long): Flow<List<NbaGame>>
}