package com.jiachian.nbatoday

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.compose.theme.updateColors
import com.jiachian.nbatoday.datastore.BaseDataStore
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.event.EventBroadcaster
import com.jiachian.nbatoday.event.EventManager
import com.jiachian.nbatoday.repository.RepositoryProvider
import com.jiachian.nbatoday.utils.ScreenStateHelper
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val repositoryProvider: RepositoryProvider,
    private val dataStore: BaseDataStore,
    private val screenStateHelper: ScreenStateHelper,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    private val eventManager: EventManager<Event> = EventManager()
) : ViewModel(), EventBroadcaster<MainViewModel.Event> by eventManager {

    sealed class Event {
        object Exit : Event()
    }

    private fun Event.send() {
        eventManager.send(this)
    }

    private val isLoading = MutableStateFlow(false)

    private val isLoadedImp = MutableStateFlow(false)
    val isLoaded = isLoadedImp.asStateFlow()

    val stateStack = screenStateHelper.stateStack
    private val currentState = screenStateHelper.currentState
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        loadData()
    }

    private fun loadData() {
        if (isLoading.value || isLoaded.value) return
        viewModelScope.launch(dispatcherProvider.io) {
            isLoading.value = true
            val updateScheduleDeferred = async {
                repositoryProvider.schedule.updateSchedule()
            }
            val updateColorsDeferred = async {
                dataStore
                    .themeColors
                    .firstOrNull()
                    ?.also { updateColors(it) }
            }
            val loginUserDeferred = async {
                dataStore
                    .user
                    .firstOrNull()
                    ?.also { user ->
                        repositoryProvider.user.login(user.account, user.password)
                    }
            }
            awaitAll(
                updateScheduleDeferred,
                updateColorsDeferred,
                loginUserDeferred
            )
            isLoading.value = false
            isLoadedImp.value = true
        }
    }

    fun exitScreen() {
        if (currentState.value is NbaState.Home) {
            Event.Exit.send()
        } else {
            screenStateHelper.exitScreen()
        }
    }
}
