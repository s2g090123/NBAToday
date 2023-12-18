package com.jiachian.nbatoday.utils

import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.screen.bet.BetViewModel
import com.jiachian.nbatoday.compose.screen.calendar.CalendarViewModel
import com.jiachian.nbatoday.compose.screen.card.GameCardViewModel
import com.jiachian.nbatoday.compose.screen.home.HomeViewModel
import com.jiachian.nbatoday.compose.screen.home.schedule.SchedulePageViewModel
import com.jiachian.nbatoday.compose.screen.home.standing.StandingPageViewModel
import com.jiachian.nbatoday.compose.screen.home.user.UserPageViewModel
import com.jiachian.nbatoday.compose.screen.player.PlayerViewModel
import com.jiachian.nbatoday.compose.screen.score.BoxScoreViewModel
import com.jiachian.nbatoday.compose.screen.team.TeamViewModel
import com.jiachian.nbatoday.datastore.BaseDataStore
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.repository.RepositoryProvider
import kotlinx.coroutines.CoroutineScope

class ComposeViewModelProvider(
    private val repositoryProvider: RepositoryProvider,
    private val dataStore: BaseDataStore,
    private val navigationController: NavigationController,
) {
    private val viewModelMap = mutableMapOf<MainRoute, ComposeViewModel>()

    fun getHomeViewModel(
        dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
        coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined),
    ): HomeViewModel {
        return viewModelMap[MainRoute.Home] as? HomeViewModel ?: HomeViewModel(
            composeViewModelProvider = this,
            navigationController = navigationController,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope
        ).apply {
            viewModelMap[MainRoute.Home] = this
        }
    }

    fun getBoxScoreViewModel(
        gameId: String,
        dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
        coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined),
    ): BoxScoreViewModel {
        return viewModelMap[MainRoute.BoxScore] as? BoxScoreViewModel ?: BoxScoreViewModel(
            gameId = gameId,
            repository = repositoryProvider.game,
            navigationController = navigationController,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope,
        ).apply {
            viewModelMap[MainRoute.BoxScore] = this
        }
    }

    fun getTeamViewModel(
        teamId: Int,
        dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
        coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined),
    ): TeamViewModel {
        return viewModelMap[MainRoute.Team] as? TeamViewModel ?: TeamViewModel(
            teamId = teamId,
            teamRepository = repositoryProvider.team,
            gameRepository = repositoryProvider.game,
            navigationController = navigationController,
            composeViewModelProvider = this,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope
        ).apply {
            viewModelMap[MainRoute.Team] = this
        }
    }

    fun getPlayerViewModel(
        playerId: Int,
        dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
        coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined),
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
        coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined),
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
        coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined),
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
        coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined),
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
        coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined),
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
        coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined),
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
        coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined),
    ): GameCardViewModel {
        return GameCardViewModel(
            gameAndBets = gameAndBets,
            betRepository = repositoryProvider.bet,
            userRepository = repositoryProvider.user,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope,
        )
    }

    fun removeViewModel(route: MainRoute) {
        viewModelMap.remove(route)
    }
}
