package com.jiachian.nbatoday.compose.screen.home.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.ScheduleDateRange
import com.jiachian.nbatoday.common.Resource2
import com.jiachian.nbatoday.compose.screen.card.models.GameCardData
import com.jiachian.nbatoday.compose.screen.home.schedule.event.ScheduleEvent
import com.jiachian.nbatoday.compose.screen.home.schedule.event.ScheduleUiEvent
import com.jiachian.nbatoday.compose.screen.home.schedule.models.DateData
import com.jiachian.nbatoday.compose.screen.home.schedule.state.ScheduleState
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.models.local.game.toGameCardState
import com.jiachian.nbatoday.usecase.game.GameUseCase
import com.jiachian.nbatoday.usecase.schedule.ScheduleUseCase
import com.jiachian.nbatoday.usecase.user.UserUseCase
import com.jiachian.nbatoday.utils.DateUtils
import com.jiachian.nbatoday.utils.DateUtils.reset
import java.util.Calendar
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SchedulePageViewModel(
    private val scheduleUseCase: ScheduleUseCase,
    private val gameUseCase: GameUseCase,
    userUseCase: UserUseCase,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) : ViewModel() {
    private val user = userUseCase.getUser()

    private val stateImp = MutableStateFlow(ScheduleState())
    val state = stateImp.asStateFlow()

    private val datesImp = mutableListOf<DateData>()
    val dates: List<DateData> = datesImp

    private var selectedDate = DateData()

    private val eventImp = MutableSharedFlow<ScheduleUiEvent>()
    val event = eventImp.asSharedFlow()

    private val cal = DateUtils.getCalendar()

    init {
        viewModelScope.launch {
            stateImp.value = state.value.copy(loading = true)
            withContext(dispatcherProvider.default) {
                datesImp.addAll(cal.getDates())
                selectedDate = dates[dates.size / 2]
            }
            collectGames()
        }
    }

    private suspend fun collectGames() {
        cal.apply {
            reset()
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.MILLISECOND, 0)
        }
        gameUseCase.getGamesDuring(
            cal.timeInMillis - DateUtils.DAY_IN_MILLIS * (ScheduleDateRange + 1),
            cal.timeInMillis + DateUtils.DAY_IN_MILLIS * (ScheduleDateRange)
        ).collect { games ->
            val groupedGames = cal.groupGames(games)
            stateImp.value = state.value.copy(
                games = groupedGames,
                loading = false,
                refreshing = false
            )
        }
    }

    fun onEvent(event: ScheduleEvent) {
        when (event) {
            ScheduleEvent.Refresh -> refreshSchedule()
            is ScheduleEvent.Select -> selectedDate = event.date
        }
    }

    private fun refreshSchedule() {
        if (state.value.refreshing) return
        viewModelScope.launch {
            scheduleUseCase.updateSchedule(
                selectedDate.year,
                selectedDate.month,
                selectedDate.day
            ).collect {
                when (it) {
                    is Resource2.Error -> {
                        eventImp.emit(ScheduleUiEvent.Toast(it.message))
                        stateImp.value = state.value.copy(refreshing = false)
                    }
                    is Resource2.Loading -> stateImp.value = state.value.copy(refreshing = true)
                    is Resource2.Success -> {
                        stateImp.value = state.value.copy(refreshing = false)
                    }
                }
            }
        }
    }

    private fun Calendar.getDates(): List<DateData> {
        val range = ScheduleDateRange * 2 + 1
        reset()
        add(Calendar.DAY_OF_MONTH, -ScheduleDateRange)
        return List(range) {
            DateData(
                get(Calendar.YEAR),
                get(Calendar.MONTH) + 1,
                get(Calendar.DAY_OF_MONTH)
            ).also {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }
    }

    private fun Calendar.groupGames(games: List<GameAndBets>): Map<DateData, List<GameCardData>> {
        reset()
        return games
            .toGameCardState(user)
            .groupBy { state ->
                time = state.data.game.gameDateTime
                DateData(
                    get(Calendar.YEAR),
                    get(Calendar.MONTH) + 1,
                    get(Calendar.DAY_OF_MONTH)
                )
            }
    }
}
