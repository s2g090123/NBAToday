package com.jiachian.nbatoday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jiachian.nbatoday.compose.screen.main.MainScreen
import com.jiachian.nbatoday.compose.theme.NBATodayTheme
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.utils.LocalActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()

    private var navController: NavHostController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Ltr,
                LocalTextStyle provides LocalTextStyle.current.copy(textDirection = TextDirection.Ltr),
                LocalActivity provides this
            ) {
                val navController = rememberNavController().apply {
                    navController = this
                }
                NBATodayTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.primary
                    ) {
                        MainScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
        viewModel.navigationEvent.collectWithLifecycle(this::onNavigationEvent)
    }

    private fun onNavigationEvent(event: NavigationController.Event?) {
        when (event) {
            is NavigationController.Event.BackScreen -> {
                if (event.departure is MainRoute.Home) {
                    finish()
                }
                viewModel.viewModelProvider.removeViewModel(event.departure)
                navController?.popBackStack()
            }
            is NavigationController.Event.NavigateToHome -> {
                navController?.navigate(MainRoute.Home.route) {
                    popUpTo(MainRoute.Splash.route) {
                        inclusive = true
                    }
                }
            }
            is NavigationController.Event.NavigateToBoxScore -> {
                navController?.navigate("${MainRoute.BoxScore.path}/${event.gameId}")
            }
            is NavigationController.Event.NavigateToTeam -> {
                navController?.navigate("${MainRoute.Team.path}/${event.teamId}")
            }
            is NavigationController.Event.NavigateToPlayer -> {
                navController?.navigate("${MainRoute.Player.path}/${event.playerId}")
            }
            is NavigationController.Event.NavigateToCalendar -> {
                navController?.navigate("${MainRoute.Calendar.path}/${event.dateTime}")
            }
            is NavigationController.Event.NavigateToBet -> {
                navController?.navigate("${MainRoute.Bet.path}/${event.account}")
            }
            null -> {}
        }
        viewModel.consumeNavigationEvent(event)
    }

    private fun <T> Flow<T>.collectWithLifecycle(collector: FlowCollector<T>) {
        lifecycleScope.launch {
            flowWithLifecycle(lifecycle).collect(collector)
        }
    }
}
