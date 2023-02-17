package com.jiachian.nbatoday

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.compose.screen.home.HomeViewModel
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.compose.theme.updateColors
import com.jiachian.nbatoday.data.BaseRepository
import com.jiachian.nbatoday.data.datastore.NbaDataStore
import com.jiachian.nbatoday.event.EventBroadcaster
import com.jiachian.nbatoday.event.EventManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val repository: BaseRepository,
    private val dataStore: NbaDataStore,
    private val eventManager: EventManager<Event> = EventManager()
) : ViewModel(), EventBroadcaster<MainViewModel.Event> by eventManager {

    sealed class Event {
        object Exit : Event()
    }

    private fun Event.send() {
        eventManager.send(this)
    }

    private val initState: NbaState by lazy {
        NbaState.Home(
            HomeViewModel(
                repository = repository,
                dataStore = dataStore,
                openScreen = this::updateState
            )
        )
    }

    private val isLoadingAppImp = MutableStateFlow(true)
    val isLoadingApp = isLoadingAppImp.asStateFlow()

    private val stateStackImp by lazy { MutableStateFlow(listOf(initState)) }
    val stateStack by lazy { stateStackImp.asStateFlow() }
    val currentState by lazy {
        stateStack.map { it.last() }
            .stateIn(viewModelScope, SharingStarted.Eagerly, initState)
    }

    init {
        viewModelScope.launch {
            isLoadingAppImp.value = true
            withContext(Dispatchers.IO) {
                repository.refreshSchedule()
            }
            isLoadingAppImp.value = false
        }
        viewModelScope.launch(Dispatchers.IO) {
            val colors = dataStore.themeColors.first()
            updateColors(colors)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val user = dataStore.userData.firstOrNull() ?: return@launch
            val account = user.account ?: return@launch
            val password = user.password ?: return@launch
            repository.login(account, password)
        }
    }

    fun updateState(state: NbaState) {
        if (currentState.value == state) return
        val stack = stateStack.value.toMutableList().apply {
            add(state)
        }
        stateStackImp.value = stack
    }

    fun backState() {
        if (currentState.value == initState) {
            Event.Exit.send()
        } else {
            stateStackImp.value = stateStack.value.dropLast(1)
        }
    }
}