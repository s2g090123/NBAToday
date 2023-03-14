package com.jiachian.nbatoday

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
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
            SplashScreen(
                listOf(MaterialTheme.colors.secondary.copy(0.25f), MaterialTheme.colors.secondary)
            )
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

@OptIn(ExperimentalTextApi::class)
@Composable
private fun SplashScreen(
    colors: List<Color>
) {
    val infiniteAnimation = rememberInfiniteTransition()
    val colorAnimation by infiniteAnimation.animateColor(
        initialValue = MaterialTheme.colors.secondary.copy(0.25f),
        targetValue = MaterialTheme.colors.secondary,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(durationMillis = 300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val offset by infiniteAnimation.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val brush = remember(offset) {
        object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                val widthOffset = size.width * offset
                val heightOffset = size.height
                return LinearGradientShader(
                    colors = colors,
                    from = Offset(widthOffset, heightOffset),
                    to = Offset(widthOffset + size.width, heightOffset + size.height),
                    tileMode = TileMode.Mirror
                )
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Text(
                text = stringResource(R.string.app_name_splash),
                fontSize = 64.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Cursive,
                style = TextStyle(brush = brush)
            )
        }
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(R.string.is_loading_app),
            color = colorAnimation,
            fontSize = 14.sp
        )
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