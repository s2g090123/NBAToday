package com.jiachian.nbatoday.compose.screen.main

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.SplashViewModel
import com.jiachian.nbatoday.Transparency25
import com.jiachian.nbatoday.compose.screen.bet.BetScreen
import com.jiachian.nbatoday.compose.screen.calendar.CalendarScreen
import com.jiachian.nbatoday.compose.screen.home.HomeScreen
import com.jiachian.nbatoday.compose.screen.player.PlayerScreen
import com.jiachian.nbatoday.compose.screen.score.BoxScoreScreen
import com.jiachian.nbatoday.compose.screen.team.TeamScreen
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.navigation.rememberNavigationController
import kotlinx.coroutines.flow.filter
import org.koin.androidx.compose.koinViewModel

private const val SplashOffsetAnimationDurationMs = 2000

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    navigationController: NavigationController = rememberNavigationController(navController),
) {
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = MainRoute.Splash.route,
    ) {
        composable(MainRoute.Splash.route) {
            SplashScreen(
                colors = listOf(
                    MaterialTheme.colors.secondary.copy(Transparency25),
                    MaterialTheme.colors.secondary
                ),
                navigateToHome = navigationController::navigateToHome
            )
        }
        composable(MainRoute.Home.route) {
            HomeScreen(
                navigateToBoxScore = navigationController::navigateToBoxScore,
                navigateToTeam = navigationController::navigateToTeam,
                navigateToCalendar = navigationController::navigateToCalendar,
                navigateToBet = navigationController::navigateToBet,
            )
        }
        composable(MainRoute.BoxScore.route) {
            BoxScoreScreen(
                openPlayerInfo = navigationController::navigateToPlayer,
                onBack = navController::popBackStack
            )
        }
        composable(MainRoute.Team.route) {
            TeamScreen(
                navigateToPlayer = navigationController::navigateToPlayer,
                navigateToBoxScore = navigationController::navigateToBoxScore,
                onBack = navController::popBackStack
            )
        }
        composable(MainRoute.Player.route) {
            PlayerScreen(
                onBack = navController::popBackStack,
            )
        }
        composable(MainRoute.Calendar.route) {
            CalendarScreen(
                navigateToBoxScore = navigationController::navigateToBoxScore,
                navigateToTeam = navigationController::navigateToTeam,
                onBack = navController::popBackStack
            )
        }
        composable(MainRoute.Bet.route) {
            BetScreen(
                navigateToBoxScore = navigationController::navigateToBoxScore,
                navigateToTeam = navigationController::navigateToTeam,
                onBack = navController::popBackStack,
            )
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun SplashScreen(
    viewModel: SplashViewModel = koinViewModel(),
    colors: List<Color>,
    navigateToHome: () -> Unit,
) {
    val infiniteAnimation = rememberInfiniteTransition()
    val colorAnimation by infiniteAnimation.animateColor(
        initialValue = MaterialTheme.colors.secondary.copy(Transparency25),
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
            animation = tween(SplashOffsetAnimationDurationMs, easing = LinearEasing),
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
        Text(
            text = stringResource(R.string.app_name_splash),
            fontSize = 64.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.Cursive,
            style = TextStyle(brush = brush)
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(R.string.is_loading_app),
            color = colorAnimation,
            fontSize = 14.sp
        )
    }
    LaunchedEffect(Unit) {
        snapshotFlow { viewModel.isLoaded }
            .filter { it }
            .collect { navigateToHome() }
    }
}
