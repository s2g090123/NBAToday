package com.jiachian.nbatoday

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.compose.screen.home.HomeViewModel
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.compose.theme.updateColors
import com.jiachian.nbatoday.data.BaseRepository
import com.jiachian.nbatoday.data.datastore.BaseDataStore
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.event.EventBroadcaster
import com.jiachian.nbatoday.event.EventManager
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: BaseRepository,
    private val dataStore: BaseDataStore,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
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

    private val isLoading = MutableStateFlow(false)

    private val isLoadedImp = MutableStateFlow(false)
    val isLoaded = isLoadedImp.asStateFlow()

    private val stateStackImp by lazy { MutableStateFlow(listOf(initState)) }
    val stateStack by lazy { stateStackImp.asStateFlow() }
    val currentState by lazy {
        stateStack.map { it.last() }
            .stateIn(viewModelScope, SharingStarted.Eagerly, initState)
    }

    fun loadData() {
        if (isLoading.value || isLoaded.value) return
        viewModelScope.launch(dispatcherProvider.io) {
            isLoading.value = true
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
            isLoading.value = false
            isLoadedImp.value = true
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
