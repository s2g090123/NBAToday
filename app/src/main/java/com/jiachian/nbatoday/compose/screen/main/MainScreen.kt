package com.jiachian.nbatoday.compose.screen.main

import android.os.Bundle
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.jiachian.nbatoday.MainViewModel
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.Transparency25
import com.jiachian.nbatoday.compose.screen.bet.BetScreen
import com.jiachian.nbatoday.compose.screen.calendar.CalendarScreen
import com.jiachian.nbatoday.compose.screen.home.HomeScreen
import com.jiachian.nbatoday.compose.screen.player.PlayerScreen
import com.jiachian.nbatoday.compose.screen.score.BoxScoreScreen
import com.jiachian.nbatoday.compose.screen.team.TeamScreen
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.utils.getOrError

private const val SplashOffsetAnimationDurationMs = 2000

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    navController: NavHostController,
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
                )
            )
        }
        composable(MainRoute.Home.route) {
            remember {
                viewModel.viewModelProvider.getHomeViewModel()
            }.let { viewModel -> HomeScreen(viewModel = viewModel) }
        }
        composable(MainRoute.BoxScore.route) {
            remember {
                viewModel.viewModelProvider.getBoxScoreViewModel(
                    gameId = it.arguments?.getString(MainRoute.BoxScore.param).getOrError()
                )
            }.let { viewModel -> BoxScoreScreen(viewModel = viewModel) }
        }
        composable(MainRoute.Team.route) {
            remember {
                viewModel.viewModelProvider.getTeamViewModel(
                    teamId = it.arguments?.getStringToInt(MainRoute.Team.param).getOrError()
                )
            }.let { viewModel -> TeamScreen(viewModel = viewModel) }
        }
        composable(MainRoute.Player.route) {
            remember {
                viewModel.viewModelProvider.getPlayerViewModel(
                    playerId = it.arguments?.getStringToInt(MainRoute.Player.param).getOrError()
                )
            }.let { viewModel -> PlayerScreen(viewModel = viewModel) }
        }
        composable(MainRoute.Calendar.route) {
            remember {
                viewModel.viewModelProvider.getCalendarViewModel(
                    dateTime = it.arguments?.getStringToLong(MainRoute.Calendar.param).getOrError()
                )
            }.let { viewModel -> CalendarScreen(viewModel = viewModel) }
        }
        composable(MainRoute.Bet.route) {
            remember {
                viewModel.viewModelProvider.getBetViewModel(
                    account = it.arguments?.getString(MainRoute.Bet.param).getOrError()
                )
            }.let { viewModel -> BetScreen(viewModel = viewModel) }
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun SplashScreen(colors: List<Color>) {
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
}

private fun Bundle.getStringToInt(key: String) = this.getString(key)?.toIntOrNull()
private fun Bundle.getStringToLong(key: String) = this.getString(key)?.toLongOrNull()
