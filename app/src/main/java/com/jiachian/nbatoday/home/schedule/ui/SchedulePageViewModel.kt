package com.jiachian.nbatoday.home.schedule.ui

import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.common.data.ScheduleDateRange
import com.jiachian.nbatoday.common.domain.Resource
import com.jiachian.nbatoday.common.ui.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.common.ui.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.game.data.model.local.GameAndBets
import com.jiachian.nbatoday.game.data.model.toGameCardState
import com.jiachian.nbatoday.game.domain.GameUseCase
import com.jiachian.nbatoday.game.ui.model.GameCardData
import com.jiachian.nbatoday.home.schedule.domain.ScheduleUseCase
import com.jiachian.nbatoday.home.schedule.ui.error.asScheduleError
import com.jiachian.nbatoday.home.schedule.ui.event.ScheduleDataEvent
import com.jiachian.nbatoday.home.schedule.ui.event.ScheduleUIEvent
import com.jiachian.nbatoday.home.schedule.ui.model.DateData
import com.jiachian.nbatoday.home.schedule.ui.state.MutableScheduleState
import com.jiachian.nbatoday.home.schedule.ui.state.ScheduleState
import com.jiachian.nbatoday.home.user.domain.GetUser
import com.jiachian.nbatoday.utils.DateUtils
import com.jiachian.nbatoday.utils.DateUtils.reset
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class SchedulePageViewModel(
    private val scheduleUseCase: ScheduleUseCase,
    private val gameUseCase: GameUseCase,
    getUser: GetUser,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) : ViewModel() {
    private val user = getUser()

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
                    is Resource.Error -> {
                        Snapshot.withMutableSnapshot {
                            stateImp.apply {
                                event = ScheduleDataEvent.Error(it.error.asScheduleError())
                                refreshing = false
                            }
                        }
                    }
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
