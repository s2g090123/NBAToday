package com.jiachian.nbatoday.repository.schedule

import com.jiachian.nbatoday.repository.BaseRepository

abstract class ScheduleRepository : BaseRepository() {
    abstract suspend fun updateSchedule()
    abstract suspend fun updateSchedule(year: Int, month: Int, day: Int)
}
