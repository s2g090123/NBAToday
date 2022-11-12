package com.jiachian.nbatoday.compose.screen.home

import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ComposeViewModel() {

    private val homeIndexImp = MutableStateFlow(0)
    val homeIndex = homeIndexImp.asStateFlow()

    private val scheduleIndexImp = MutableStateFlow(0)
    val scheduleIndex = scheduleIndexImp.asStateFlow()

    fun updateHomeIndex(index: Int) {
        homeIndexImp.value = index.coerceIn(0, 2)
    }

    fun updateScheduleIndex(index: Int) {
        scheduleIndexImp.value = index
    }
}