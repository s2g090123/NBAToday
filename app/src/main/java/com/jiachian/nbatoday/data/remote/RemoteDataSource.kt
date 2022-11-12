package com.jiachian.nbatoday.data.remote

abstract class RemoteDataSource {
    abstract suspend fun getSchedule(): Schedule?
}