package com.jiachian.nbatoday.utils

import com.jiachian.nbatoday.compose.screen.bet.BetViewModel
import com.jiachian.nbatoday.compose.screen.calendar.CalendarViewModel
import com.jiachian.nbatoday.compose.screen.card.GameStatusCardViewModel
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
import com.jiachian.nbatoday.models.local.game.GameAndBet
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.repository.RepositoryProvider
import java.util.Date
import kotlinx.coroutines.CoroutineScope

class ComposeViewModelProvider(
    private val repositoryProvider: RepositoryProvider,
    private val dataStore: BaseDataStore,
    private val screenStateHelper: ScreenStateHelper,
) {
    fun getHomeViewModel(
        dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
        coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined),
    ): HomeViewModel {
        return HomeViewModel(
            composeViewModelProvider = this,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope
        )
    }

    fun getBoxScoreViewModel(
        gameId: String,
        dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
        coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined),
    ): BoxScoreViewModel {
        return BoxScoreViewModel(
            gameId = gameId,
            repository = repositoryProvider.game,
            screenStateHelper = screenStateHelper,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope,
        )
    }

    fun getTeamViewModel(
        team: NBATeam,
        dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
        coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined),
    ): TeamViewModel {
        return TeamViewModel(
            team = team,
            teamRepository = repositoryProvider.team,
            gameRepository = repositoryProvider.game,
            screenStateHelper = screenStateHelper,
            composeViewModelProvider = this,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope
        )
    }

    fun getPlayerViewModel(
        playerId: Int,
        dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
        coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined),
    ): PlayerViewModel {
        return PlayerViewModel(
            playerId = playerId,
            repository = repositoryProvider.player,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope,
        )
    }

    fun getCalendarViewModel(
        date: Date,
        dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
        coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined),
    ): CalendarViewModel {
        return CalendarViewModel(
            date = date,
            repository = repositoryProvider.game,
            screenStateHelper = screenStateHelper,
            composeViewModelProvider = this,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope,
        )
    }

    fun getBetViewModel(
        account: String,
        dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
        coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined),
    ): BetViewModel {
        return BetViewModel(
            account = account,
            repository = repositoryProvider.bet,
            screenStateHelper = screenStateHelper,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope,
        )
    }

    fun getSchedulePageViewModel(
        dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
        coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined),
    ): SchedulePageViewModel {
        return SchedulePageViewModel(
            scheduleRepository = repositoryProvider.schedule,
            gameRepository = repositoryProvider.game,
            screenStateHelper = screenStateHelper,
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
            screenStateHelper = screenStateHelper,
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
            screenStateHelper = screenStateHelper,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope,
        )
    }

    fun getGameStatusCardViewModel(
        gameAndBet: GameAndBet,
        dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
        coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined),
    ): GameStatusCardViewModel {
        return GameStatusCardViewModel(
            gameAndBet = gameAndBet,
            betRepository = repositoryProvider.bet,
            userRepository = repositoryProvider.user,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope,
        )
    }
}
