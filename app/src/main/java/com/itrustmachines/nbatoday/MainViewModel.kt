package com.itrustmachines.nbatoday

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itrustmachines.nbatoday.data.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val repository: BaseRepository
) : ViewModel() {

    private val isLoadingAppImp = MutableStateFlow(true)
    val isLoadingApp = isLoadingAppImp.asStateFlow()

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
}