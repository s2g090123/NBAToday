package com.jiachian.nbatoday.compose.screen.player

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.player.event.PlayerDataEvent
import com.jiachian.nbatoday.compose.screen.player.event.PlayerUIEvent
import com.jiachian.nbatoday.compose.screen.player.models.PlayerStatsSorting
import com.jiachian.nbatoday.compose.screen.player.state.PlayerInfoState
import com.jiachian.nbatoday.compose.screen.player.state.PlayerStatsState
import com.jiachian.nbatoday.compose.screen.player.widgets.playerInfo
import com.jiachian.nbatoday.compose.screen.player.widgets.playerStats
import com.jiachian.nbatoday.compose.widget.IconButton
import com.jiachian.nbatoday.compose.widget.LoadingScreen
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.testing.testtag.PlayerTestTag
import com.jiachian.nbatoday.utils.showToast
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = koinViewModel(),
    navigationController: NavigationController,
) {
    val context = LocalContext.current
    val state = viewModel.state
    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
        topBar = {
            IconButton(
                modifier = Modifier
                    .testTag(PlayerTestTag.PlayerScreen_Button_Back)
                    .padding(top = 8.dp, start = 8.dp),
                drawableRes = R.drawable.ic_black_back,
                tint = MaterialTheme.colors.secondary,
                onClick = navigationController::back,
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.loading -> {
                    LoadingScreen(
                        modifier = Modifier
                            .testTag(PlayerTestTag.PlayerScreen_Loading)
                            .align(Alignment.Center),
                        color = MaterialTheme.colors.secondary,
                    )
                }
                state.notFound -> {
                    Text(
                        modifier = Modifier
                            .testTag(PlayerTestTag.PlayerScreen_PlayerNotFound)
                            .align(Alignment.Center),
                        text = stringResource(R.string.player_career_not_found),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colors.secondary
                    )
                }
                else -> {
                    PlayerDetail(
                        modifier = Modifier.fillMaxSize(),
                        info = state.info,
                        stats = state.stats,
                        updateSorting = { viewModel.onEvent(PlayerUIEvent.Sort(it)) }
                    )
                }
            }
        }
    }
    LaunchedEffect(state.event) {
        state.event?.let { event ->
            when (event) {
                is PlayerDataEvent.Error -> showToast(context, event.error.message)
            }
            viewModel.onEvent(PlayerUIEvent.EventReceived)
        }
    }
}

@Composable
private fun PlayerDetail(
    modifier: Modifier = Modifier,
    info: PlayerInfoState,
    stats: PlayerStatsState,
    updateSorting: (PlayerStatsSorting) -> Unit,
) {
    val scrollState = rememberScrollState()
    LazyColumn(modifier = modifier) {
        playerInfo(info = info)
        playerStats(
            scrollState = scrollState,
            stats = stats,
            updateSorting = updateSorting,
        )
    }
}
