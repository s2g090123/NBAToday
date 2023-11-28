package com.jiachian.nbatoday.compose.screen.bet

import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.game.NbaGameAndBet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class BetDialogViewModel(
    val gameAndBet: NbaGameAndBet,
    val userPoints: Long,
    dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined),
) {
    private val showWarningImp = MutableStateFlow(false)
    val showWarning = showWarningImp.asStateFlow()

    private val homePointsImp = MutableStateFlow(0L)
    val homePoints = homePointsImp.asStateFlow()

    private val awayPointsImp = MutableStateFlow(0L)
    val awayPoints = awayPointsImp.asStateFlow()

    val remainedPoints = combine(homePoints, awayPoints) { home, away ->
        userPoints - home - away
    }.stateIn(coroutineScope, SharingStarted.Lazily, userPoints)

    val confirmEnabled = remainedPoints.map { points ->
        points != userPoints && points >= 0
    }.stateIn(coroutineScope, SharingStarted.Lazily, false)

    fun showWarning() {
        showWarningImp.value = true
    }

    fun hideWarning() {
        showWarningImp.value = false
    }

    fun updateHomePoints(points: Long) {
        homePointsImp.value = points
    }

    fun updateAwayPoints(points: Long) {
        awayPointsImp.value = points
    }
}
