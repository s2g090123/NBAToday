package com.jiachian.nbatoday

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jiachian.nbatoday.compose.screen.bet.BetScreen
import com.jiachian.nbatoday.compose.screen.calendar.GameCalendarScreen
import com.jiachian.nbatoday.compose.screen.home.HomeScreen
import com.jiachian.nbatoday.compose.screen.player.PlayerCareerScreen
import com.jiachian.nbatoday.compose.screen.score.BoxScoreScreen
import com.jiachian.nbatoday.compose.screen.team.TeamScreen
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.compose.theme.NBATodayTheme
import com.jiachian.nbatoday.utils.LocalActivity
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Ltr,
                LocalTextStyle provides LocalTextStyle.current.copy(textDirection = TextDirection.Ltr),
                LocalActivity provides this
            ) {
                NBATodayTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.primary
                    ) {
                        NbaScreen(viewModel)
                    }
                }
            }
        }

        observeData()
    }

    private fun observeData() {
        viewModel.eventFlow.asLiveData().observe(this, Observer(this::onEvent))
    }

    private fun onEvent(event: MainViewModel.Event?) {
        when (event) {
            MainViewModel.Event.Exit -> finish()
            else -> {}
        }
        viewModel.onEventConsumed(event)
    }
}

@Composable
private fun NbaScreen(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val isLoadingApp by viewModel.isLoadingApp.collectAsState()

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen()
        }
        composable("home") {
            MainScreen(viewModel)
        }
    }
    LaunchedEffect(isLoadingApp) {
        if (!isLoadingApp) {
            navController.navigate("home")
        }
    }
}

@Composable
private fun SplashScreen() {
    val title = stringResource(R.string.app_name_splash)
    var tick by rememberSaveable { mutableStateOf(0) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            repeat(title.length) {
                Text(
                    text = title[it].toString(),
                    color = animateColorAsState(
                        targetValue = MaterialTheme.colors.secondary.copy(if (tick in it until title.length * 2 - it) 1f else 0.5f),
                        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
                    ).value,
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Cursive
                )
                if (it == 2) {
                    Text(
                        text = " ",
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Cursive
                    )
                }
            }
        }
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(R.string.is_loading_app),
            color = animateColorAsState(
                targetValue = MaterialTheme.colors.secondary.copy(if (tick % 2 == 0) 0.25f else 1f),
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            ).value,
            fontSize = 14.sp
        )
    }
    LaunchedEffect(Unit) {
        val length = title.length
        while (true) {
            delay(333)
            if (tick >= length * 2) {
                tick = 0
            } else {
                tick += 1
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun MainScreen(
    viewModel: MainViewModel
) {
    val currentState by viewModel.currentState.collectAsState()
    val stateStack by viewModel.stateStack.collectAsState()
    Scaffold {
        stateStack.forEach { state ->
            when (state) {
                is NbaState.Home -> {
                    HomeScreen(state.viewModel)
                }
                is NbaState.BoxScore -> {
                    BoxScoreScreen(
                        viewModel = state.viewModel,
                        onBack = { viewModel.backState() }
                    )
                }
                is NbaState.Team -> {
                    TeamScreen(
                        viewModel = state.viewModel,
                        onBack = { viewModel.backState() }
                    )
                }
                is NbaState.Player -> {
                    PlayerCareerScreen(
                        viewModel = state.viewModel,
                        onBack = { viewModel.backState() }
                    )
                }
                is NbaState.Calendar -> {
                    GameCalendarScreen(
                        viewModel = state.viewModel,
                        onClose = { viewModel.backState() }
                    )
                }
                is NbaState.Bet -> {
                    BetScreen(
                        viewModel = state.viewModel,
                        onBackClick = { viewModel.backState() }
                    )
                }
            }
        }
    }

    BackHandler {
        viewModel.backState()
    }
}