package com.jiachian.nbatoday.data.local

import kotlinx.coroutines.flow.Flow
import java.util.*

abstract class LocalDataSource {

    abstract val dates: Flow<List<Date>>

    abstract val games: Flow<List<NbaGame>>

    abstract fun getGamesDuring(from: Long, to: Long): Flow<List<NbaGame>>

    abstract suspend fun insertGames(games: List<NbaGame>)
}