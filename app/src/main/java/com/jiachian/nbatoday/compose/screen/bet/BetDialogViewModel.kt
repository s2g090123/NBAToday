package com.jiachian.nbatoday.compose.screen.bet

import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.utils.WhileSubscribed5000
import kotlin.math.min
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel for handling business logic related to [BetDialog].
 *
 * @property gameAndBets The data class containing information about the game and associated bets.
 * @property userPoints The total points of the user available for betting.
 * @property dispatcherProvider The provider for obtaining dispatchers for coroutines (default is [DefaultDispatcherProvider]).
 * @property coroutineScope The coroutine scope for managing coroutines (default is [CoroutineScope] with main dispatcher).
 */
class BetDialogViewModel(
    val gameAndBets: GameAndBets,
    val userPoints: Long,
    dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.main),
) {
    // the warning state in the UI
    private val warningImp = MutableStateFlow(false)
    val warning = warningImp.asStateFlow()

    // the selected home points in the UI
    private val homePointsImp = MutableStateFlow(0L)
    val homePoints = homePointsImp.asStateFlow()

    // the selected away points in the UI
    private val awayPointsImp = MutableStateFlow(0L)
    val awayPoints = awayPointsImp.asStateFlow()

    // the remaining points available for betting
    val remainingPoints = combine(
        homePoints,
        awayPoints
    ) { home, away ->
        userPoints - home - away
    }.stateIn(coroutineScope, WhileSubscribed5000, userPoints)

    // Determine if the bet button should be enabled based on [remainingPoints].
    val enabled = remainingPoints.map { points ->
        points != userPoints && points >= 0
    }.stateIn(coroutineScope, WhileSubscribed5000, false)

    fun showWarning() {
        warningImp.value = true
    }

    fun hideWarning() {
        warningImp.value = false
    }

    fun updateHomePoints(points: Long) {
        homePointsImp.value = min(points, userPoints - awayPoints.value)
    }

    fun updateAwayPoints(points: Long) {
        awayPointsImp.value = min(points, userPoints - homePoints.value)
    }
}
