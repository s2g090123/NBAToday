package com.jiachian.nbatoday

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.compose.theme.updateColors
import com.jiachian.nbatoday.datastore.BaseDataStore
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.repository.schedule.ScheduleRepository
import com.jiachian.nbatoday.repository.team.TeamRepository
import com.jiachian.nbatoday.repository.user.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class SplashViewModel(
    private val scheduleRepository: ScheduleRepository,
    private val teamRepository: TeamRepository,
    private val userRepository: UserRepository,
    private val dataStore: BaseDataStore,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) : ViewModel() {
    private val isLoadedImp = mutableStateOf(false)
    val isLoaded by isLoadedImp

    init {
        loadData()
    }

    /**
     * Loads data asynchronously.
     * 1. update schedules
     * 2. update teams
     * 3. update theme colors
     * 4. log in the user (if available)
     *
     * Finally, navigate to the home screen after loading data
     */
    private fun loadData() {
        viewModelScope.launch(dispatcherProvider.io) {
            val updateScheduleJob = launch {
                scheduleRepository.updateSchedule()
            }
            val updateTeamsJob = launch {
                teamRepository.insertTeams()
            }
            val updateColorsJob = launch {
                dataStore
                    .themeColors
                    .first()
                    .also { updateColors(it) }
            }
            val loginUserJob = launch {
                dataStore
                    .user
                    .first()
                    ?.also { user ->
                        userRepository.login(user.account, user.password)
                    }
            }
            joinAll(
                updateScheduleJob,
                updateTeamsJob,
                updateColorsJob,
                loginUserJob
            )
            isLoadedImp.value = true
        }
    }
}
