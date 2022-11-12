package com.jiachian.nbatoday

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.data.BaseRepository
import com.jiachian.nbatoday.event.EventBroadcaster
import com.jiachian.nbatoday.event.EventManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val repository: BaseRepository,
    private val eventManager: EventManager<Event> = EventManager()
) : ViewModel(), EventBroadcaster<MainViewModel.Event> by eventManager {

    sealed class Event {

    }

    private val isLoadingAppImp = MutableStateFlow(true)
    val isLoadingApp = isLoadingAppImp.asStateFlow()

    private val stateStackImp = MutableStateFlow(listOf<NbaState>(NbaState.Home))
    val stateStack = stateStackImp.asStateFlow()
    private val currentState: NbaState?
        get() = stateStackImp.value.lastOrNull()

    init {
        viewModelScope.launch {
            isLoadingAppImp.value = true
            withContext(Dispatchers.IO) {
                repository.refreshSchedule()
            }
            isLoadingAppImp.value = false
        }
    }

    fun refreshSchedule() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.refreshSchedule()
            }
        }
    }

    fun updateState(state: NbaState) {
        if (currentState == state) return
        val stack = stateStack.value.toMutableList().apply {
            add(state)
        }
        stateStackImp.value = stack
    }

    fun backState() {
        stateStackImp.value = stateStack.value.dropLast(1)
    }
}