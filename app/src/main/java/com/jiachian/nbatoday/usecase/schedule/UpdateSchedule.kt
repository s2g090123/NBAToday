package com.jiachian.nbatoday.usecase.schedule

import com.jiachian.nbatoday.common.Resource2
import com.jiachian.nbatoday.common.Response
import com.jiachian.nbatoday.repository.schedule.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdateSchedule(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(
        year: Int = -1,
        month: Int = -1,
        day: Int = -1,
    ): Flow<Resource2<Unit>> = flow {
        emit(Resource2.Loading())
        val response = if (year == -1 || month == -1 || day == -1) {
            repository.updateSchedule()
        } else {
            repository.updateSchedule(year, month, day)
        }
        when (response) {
            is Response.Error -> emit(Resource2.Error(response.message))
            is Response.Success -> emit(Resource2.Success(response.data))
        }
    }
}
