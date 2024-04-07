package com.jiachian.nbatoday.home.schedule.domain

import com.jiachian.nbatoday.common.data.Response
import com.jiachian.nbatoday.common.domain.Resource
import com.jiachian.nbatoday.home.schedule.data.ScheduleRepository
import com.jiachian.nbatoday.home.schedule.domain.error.UpdateScheduleError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdateSchedule(
    private val repository: ScheduleRepository
) {
    companion object {
        private const val MONTH_PER_YEAR = 12
        private const val DAY_PER_MONTH = 31
    }

    operator fun invoke(
        year: Int = -1,
        month: Int = -1,
        day: Int = -1,
    ): Flow<Resource<Unit, UpdateScheduleError>> = flow {
        emit(Resource.Loading())
        val response = if (year <= 0 || month !in 1..MONTH_PER_YEAR || day !in 1..DAY_PER_MONTH) {
            repository.updateSchedule()
        } else {
            repository.updateSchedule(year, month, day)
        }
        when (response) {
            is Response.Error -> emit(Resource.Error(UpdateScheduleError.UPDATE_FAILED))
            is Response.Success -> emit(Resource.Success(response.data))
        }
    }
}
