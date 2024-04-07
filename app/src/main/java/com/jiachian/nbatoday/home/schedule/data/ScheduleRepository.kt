package com.jiachian.nbatoday.home.schedule.data

import com.jiachian.nbatoday.common.data.Response

/**
 * Manage schedule-related data.
 */
interface ScheduleRepository {
    /**
     * Updates the schedule data.
     */
    suspend fun updateSchedule(): Response<Unit>

    /**
     * Updates the schedule data for a specific date.
     *
     * @param year The year of the schedule date.
     * @param month The month of the schedule date.
     * @param day The day of the schedule date.
     */
    suspend fun updateSchedule(year: Int, month: Int, day: Int): Response<Unit>
}
