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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
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
    val currentState = screenStateHelper.currentState
        .stateIn(viewModelScope, SharingStarted.Eagerly, stateStack.value.first())

    fun loadData() {
        if (isLoading.value || isLoaded.value) return
        viewModelScope.launch(dispatcherProvider.io) {
            isLoading.value = true
            val refreshScheduleDeferred = async {
                repositoryProvider.schedule.refreshSchedule()
            }
            val updateColorsDeferred = async {
                val colors = dataStore.themeColors.first()
                updateColors(colors)
            }
            val loginDeferred = async {
                val user = dataStore.user.firstOrNull() ?: return@async
                val account = user.account ?: return@async
                val password = user.password ?: return@async
                repositoryProvider.user.login(account, password)
            }
            refreshScheduleDeferred.await()
            updateColorsDeferred.await()
            loginDeferred.await()
            isLoading.value = false
            isLoadedImp.value = true
        }
    }

    fun backState() {
        if (currentState.value is NbaState.Home) {
            Event.Exit.send()
        } else {
            screenStateHelper.exitScreen()
        }
    }
}
