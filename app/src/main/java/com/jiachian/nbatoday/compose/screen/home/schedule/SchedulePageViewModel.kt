package com.jiachian.nbatoday.compose.screen.home.schedule

import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.ScheduleDateRange
import com.jiachian.nbatoday.common.Resource
import com.jiachian.nbatoday.compose.screen.card.models.GameCardData
import com.jiachian.nbatoday.compose.screen.home.schedule.event.ScheduleDataEvent
import com.jiachian.nbatoday.compose.screen.home.schedule.event.ScheduleUIEvent
import com.jiachian.nbatoday.compose.screen.home.schedule.models.DateData
import com.jiachian.nbatoday.compose.screen.home.schedule.state.MutableScheduleState
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SchedulePageViewModel(
    private val scheduleUseCase: ScheduleUseCase,
    private val gameUseCase: GameUseCase,
    userUseCase: UserUseCase,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) : ViewModel() {
    private val user = userUseCase.getUser()

    private val stateImp = MutableScheduleState()
    val state: ScheduleState = stateImp

    private var selectedDate = DateData()

    private val cal = DateUtils.getCalendar()

    init {
        collectGames()
    }

    private fun collectGames() {
        stateImp.loading = true
        viewModelScope.launch(dispatcherProvider.default) {
            val dates = cal.getDates()
            withContext(dispatcherProvider.main) {
                stateImp.dates = dates
            }
            selectedDate = dates[dates.size / 2]
            cal.apply {
                reset()
                set(Calendar.HOUR, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.MILLISECOND, 0)
            }
            gameUseCase.getGamesDuring(
                cal.timeInMillis - DateUtils.DAY_IN_MILLIS * (ScheduleDateRange + 1),
                cal.timeInMillis + DateUtils.DAY_IN_MILLIS * (ScheduleDateRange)
            ).collectLatest { games ->
                val groupedGames = cal.groupGames(games)
                Snapshot.withMutableSnapshot {
                    stateImp.let { state ->
                        state.games = groupedGames
                        state.loading = false
                    }
                }
            }
        }
    }

    fun onEvent(event: ScheduleUIEvent) {
        when (event) {
            ScheduleUIEvent.Refresh -> refreshSchedule()
            ScheduleUIEvent.EventReceived -> stateImp.event = null
            is ScheduleUIEvent.SelectDate -> selectedDate = event.date
        }
    }

    private fun refreshSchedule() {
        if (state.refreshing) return
        viewModelScope.launch {
            scheduleUseCase.updateSchedule(
                selectedDate.year,
                selectedDate.month,
                selectedDate.day
            ).collect {
                when (it) {
                    is Resource.Error -> stateImp.event = ScheduleDataEvent.Error(it.error.asScheduleError())
                    is Resource.Loading -> stateImp.refreshing = true
                    is Resource.Success -> stateImp.refreshing = false
                }
            }
        }
    }

    private fun Calendar.getDates(): List<DateData> {
        reset()
        val range = ScheduleDateRange * 2 + 1
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
