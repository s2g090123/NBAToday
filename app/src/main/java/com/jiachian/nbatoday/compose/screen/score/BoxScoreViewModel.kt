package com.jiachian.nbatoday.compose.screen.score

import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.data.BaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BoxScoreViewModel(
    private val gameId: String,
    private val repository: BaseRepository,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined)
) : ComposeViewModel() {

    private val isRefreshingImp = MutableStateFlow(false)
    val isRefreshing = isRefreshingImp.asStateFlow()

    val boxScore = repository.getGameBoxScore(gameId)
        .stateIn(coroutineScope, SharingStarted.Eagerly, null)

    init {
        refreshScore()
    }

    fun refreshScore() {
        coroutineScope.launch {
            isRefreshingImp.value = true
            withContext(Dispatchers.IO) {
                repository.refreshGameBoxScore(gameId)
            }
            isRefreshingImp.value = false
        }
    }
}