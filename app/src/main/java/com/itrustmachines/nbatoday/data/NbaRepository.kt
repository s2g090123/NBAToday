package com.itrustmachines.nbatoday.data

import com.itrustmachines.nbatoday.data.remote.RemoteDataSource

class NbaRepository(
    private val remoteDataSource: RemoteDataSource
) : BaseRepository {

    override suspend fun refreshSchedule() {
        val schedule = remoteDataSource.getSchedule()
    }
}