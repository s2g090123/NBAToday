package com.jiachian.nbatoday.utils

import androidx.annotation.VisibleForTesting
import com.jiachian.nbatoday.annotation.ExcludeFromJacocoGeneratedReport
import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.screen.card.GameCardUIData
import com.jiachian.nbatoday.compose.screen.home.user.UserPageViewModel
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
    ): GameCardUIData {
        return GameCardUIData(
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
