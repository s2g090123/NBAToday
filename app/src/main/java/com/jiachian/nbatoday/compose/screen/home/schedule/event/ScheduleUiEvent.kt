package com.jiachian.nbatoday.compose.screen.home.schedule.event

sealed class ScheduleUiEvent {
    class Toast(val message: String? = null) : ScheduleUiEvent()
}
