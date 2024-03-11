package com.jiachian.nbatoday.utils

import androidx.annotation.VisibleForTesting
import com.jiachian.nbatoday.annotation.ExcludeFromJacocoGeneratedReport
import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.screen.bet.BetViewModel
import com.jiachian.nbatoday.compose.screen.calendar.CalendarViewModel
import com.jiachian.nbatoday.compose.screen.card.GameCardViewModel
import com.jiachian.nbatoday.compose.screen.home.schedule.SchedulePageViewModel
import com.jiachian.nbatoday.compose.screen.home.standing.StandingPageViewModel
import com.jiachian.nbatoday.compose.screen.home.user.UserPageViewModel
import com.jiachian.nbatoday.compose.screen.player.PlayerViewModel
import com.jiachian.nbatoday.datastore.BaseDataStore
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.repository.RepositoryProvider
import kotlinx.coroutines.CoroutineScope

/**
 * A provider class for creating and managing ComposeViewModel instances.
 */
class ComposeViewModelProvider(
    private val repositoryProvider: RepositoryProvider,
    private val dataStore: BaseDataStore,
    private val navigationController: NavigationController,
) {
    /**
     * A map to store created ComposeViewModel instances for reuse.
     */
    private val viewModelMap = mutableMapOf<MainRoute, ComposeViewModel>()

    fun getPlayerViewModel(
        playerId: Int,
        dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
        coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.main),
    ): PlayerViewModel {
        return viewModelMap[MainRoute.Player] as? PlayerViewModel ?: PlayerViewModel(
            playerId = playerId,
            repository = repositoryProvider.player,
            navigationController = navigationController,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope,
        ).apply {
            viewModelMap[MainRoute.Player] = this
        }
    }

    fun getCalendarViewModel(
        dateTime: Long,
        dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
        coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.main),
    ): CalendarViewModel {
        return viewModelMap[MainRoute.Calendar] as? CalendarViewModel ?: CalendarViewModel(
            dateTime = dateTime,
            repository = repositoryProvider.game,
            navigationController = navigationController,
            composeViewModelProvider = this,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope,
        ).apply {
            viewModelMap[MainRoute.Calendar] = this
        }
    }

    fun getBetViewModel(
        account: String,
        dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
        coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.main),
    ): BetViewModel {
        return viewModelMap[MainRoute.Bet] as? BetViewModel ?: BetViewModel(
            account = account,
            repository = repositoryProvider.bet,
            navigationController = navigationController,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope,
        ).apply {
            viewModelMap[MainRoute.Bet] = this
        }
    }

    fun getSchedulePageViewModel(
        dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
        coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.main),
    ): SchedulePageViewModel {
        return SchedulePageViewModel(
            scheduleRepository = repositoryProvider.schedule,
            gameRepository = repositoryProvider.game,
            navigationController = navigationController,
            composeViewModelProvider = this,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope,
        )
    }

    fun getStandingPageViewModel(
        dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
        coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.main),
    ): StandingPageViewModel {
        return StandingPageViewModel(
            repository = repositoryProvider.team,
            navigationController = navigationController,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope,
        )
    }

    fun getUserPageViewModel(
        dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
        coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.main),
    ): UserPageViewModel {
        return UserPageViewModel(
            repository = repositoryProvider.user,
            dataStore = dataStore,
            navigationController = navigationController,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope,
        )
    }

    fun getGameCardViewModel(
        gameAndBets: GameAndBets,
        dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
        coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.main),
    ): GameCardViewModel {
        return GameCardViewModel(
            gameAndBets = gameAndBets,
            betRepository = repositoryProvider.bet,
            userRepository = repositoryProvider.user,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope,
        )
    }

    /**
     * Removes a ComposeViewModel instance from the map based on the specified route.
     *
     * @param route The MainRoute associated with the view model to be removed.
     */
    fun removeViewModel(route: MainRoute) {
        viewModelMap.remove(route)
    }

    @VisibleForTesting
    @ExcludeFromJacocoGeneratedReport
    fun getViewModelMap() = viewModelMap
}
