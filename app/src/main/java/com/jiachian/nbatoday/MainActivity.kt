package com.jiachian.nbatoday

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.jiachian.nbatoday.compose.screen.home.HomeScreen
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
    val isLoadingApp by viewModel.isLoadingApp.collectAsState()

    if (isLoadingApp) {
        SplashScreen()
    } else {
        MainScreen(viewModel)
    }
}

@Composable
private fun SplashScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = MaterialTheme.colors.secondary)
        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = stringResource(R.string.is_loading_app),
            color = MaterialTheme.colors.secondary
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
            }
        }
    }

    BackHandler {
        viewModel.backState()
    }
}