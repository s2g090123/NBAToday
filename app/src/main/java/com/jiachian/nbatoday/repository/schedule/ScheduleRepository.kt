package com.jiachian.nbatoday.repository.schedule

import com.jiachian.nbatoday.repository.BaseRepository

abstract class ScheduleRepository : BaseRepository() {
    abstract suspend fun refreshSchedule()
    abstract suspend fun refreshSchedule(year: Int, month: Int, day: Int)
}
