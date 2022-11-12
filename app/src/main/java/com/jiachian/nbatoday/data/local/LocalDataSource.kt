package com.jiachian.nbatoday.data.local

import kotlinx.coroutines.flow.Flow

abstract class LocalDataSource {

    abstract val dates: Flow<List<NbaGame.NbaGameDate>>

    abstract val games: Flow<List<NbaGame>>

    abstract fun insertGames(games: List<NbaGame>)
}