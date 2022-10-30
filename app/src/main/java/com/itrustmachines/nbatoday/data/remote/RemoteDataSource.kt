package com.itrustmachines.nbatoday.data.remote

import com.itrustmachines.nbatoday.data.schedule.Schedule

abstract class RemoteDataSource {
    abstract suspend fun getSchedule(): Schedule?
}