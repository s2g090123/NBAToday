package com.itrustmachines.nbatoday.data

interface BaseRepository {
    suspend fun refreshSchedule()
}