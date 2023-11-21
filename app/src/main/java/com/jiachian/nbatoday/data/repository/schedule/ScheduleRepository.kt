package com.jiachian.nbatoday.data.repository.schedule

import com.jiachian.nbatoday.data.repository.BaseRepository

abstract class ScheduleRepository : BaseRepository() {
    abstract suspend fun refreshSchedule()
    abstract suspend fun refreshSchedule(year: Int, month: Int, day: Int)
}
