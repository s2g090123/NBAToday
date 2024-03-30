package com.jiachian.nbatoday.usecase.schedule

import com.jiachian.nbatoday.common.Error
import com.jiachian.nbatoday.common.Resource
import com.jiachian.nbatoday.common.Response
import com.jiachian.nbatoday.repository.schedule.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

enum class UpdateScheduleError : Error {
    UPDATE_FAILED
}

class UpdateSchedule(
    private val repository: ScheduleRepository
) {
    operator fun invoke(
        year: Int = -1,
        month: Int = -1,
        day: Int = -1,
    ): Flow<Resource<Unit, UpdateScheduleError>> = flow {
        emit(Resource.Loading())
        val response = if (year <= 0 || month !in 1..12 || day !in 1..31) {
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
