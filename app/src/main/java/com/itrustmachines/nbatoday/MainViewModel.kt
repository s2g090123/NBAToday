package com.itrustmachines.nbatoday

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itrustmachines.nbatoday.data.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val repository: BaseRepository
) : ViewModel() {

    fun refreshSchedule() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.refreshSchedule()
            }
        }
    }
}