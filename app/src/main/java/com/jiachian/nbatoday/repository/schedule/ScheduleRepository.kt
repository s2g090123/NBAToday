package com.jiachian.nbatoday.repository.schedule

import com.jiachian.nbatoday.repository.BaseRepository

/**
 * Manage schedule-related data.
 */
abstract class ScheduleRepository : BaseRepository() {
    /**
     * Updates the schedule data.
     */
    abstract suspend fun updateSchedule()

    /**
     * Updates the schedule data for a specific date.
     *
     * @param year The year of the schedule date.
     * @param month The month of the schedule date.
     * @param day The day of the schedule date.
     */
    abstract suspend fun updateSchedule(year: Int, month: Int, day: Int)
}
