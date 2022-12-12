package com.jiachian.nbatoday.data

import com.jiachian.nbatoday.data.local.LocalDataSource
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import java.util.*

class NbaRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : BaseRepository {

    override val dates: Flow<List<Date>> = localDataSource.dates
    override val games: Flow<List<NbaGame>> = localDataSource.games

    override suspend fun refreshSchedule() {
        val schedule = remoteDataSource.getSchedule() ?: return
        val leagueSchedule = schedule.leagueSchedule ?: return
        val nbaGames = leagueSchedule.toNbaGames()
        localDataSource.insertGames(nbaGames)
    }

    override fun getGamesDuring(from: Long, to: Long): Flow<List<NbaGame>> {
        return localDataSource.getGamesDuring(from, to)
    }
}