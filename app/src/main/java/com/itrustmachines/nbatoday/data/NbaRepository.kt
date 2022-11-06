package com.itrustmachines.nbatoday.data

import com.itrustmachines.nbatoday.data.local.LocalDataSource
import com.itrustmachines.nbatoday.data.local.NbaGame
import com.itrustmachines.nbatoday.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class NbaRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : BaseRepository {

    override val dates: Flow<List<NbaGame.NbaGameDate>> = localDataSource.dates
    override val games: Flow<List<NbaGame>> = localDataSource.games

    override suspend fun refreshSchedule() {
        val schedule = remoteDataSource.getSchedule() ?: return
        val leagueSchedule = schedule.leagueSchedule ?: return
        val nbaGames = leagueSchedule.toNbaGames()
        localDataSource.insertGames(nbaGames)
    }
}