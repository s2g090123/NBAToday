package com.jiachian.nbatoday

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.compose.screen.home.HomeViewModel
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.compose.theme.updateColors
import com.jiachian.nbatoday.data.BaseRepository
import com.jiachian.nbatoday.data.datastore.BaseDataStore
import com.jiachian.nbatoday.event.EventBroadcaster
import com.jiachian.nbatoday.event.EventManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: BaseRepository,
    private val dataStore: BaseDataStore,
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
        viewModelScope.launch(Dispatchers.IO) {
            isLoadingAppImp.value = true
            val refreshScheduleDeferred = async {
                repository.refreshSchedule()
            }
            val updateColorsDeferred = async {
                val colors = dataStore.themeColors.first()
                updateColors(colors)
            }
            val loginDeferred = async {
                val user = dataStore.userData.firstOrNull() ?: return@async
                val account = user.account ?: return@async
                val password = user.password ?: return@async
                repository.login(account, password)
            }
            refreshScheduleDeferred.await()
            updateColorsDeferred.await()
            loginDeferred.await()
            isLoadingAppImp.value = false
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