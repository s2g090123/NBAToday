package com.jiachian.nbatoday.player.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.CircularProgressIndicator
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
import com.jiachian.nbatoday.common.ui.IconButton
import com.jiachian.nbatoday.main.ui.navigation.NavigationController
import com.jiachian.nbatoday.player.ui.error.PlayerError
import com.jiachian.nbatoday.player.ui.event.PlayerDataEvent
import com.jiachian.nbatoday.player.ui.event.PlayerUIEvent
import com.jiachian.nbatoday.player.ui.model.PlayerStatsSorting
import com.jiachian.nbatoday.player.ui.state.PlayerInfoState
import com.jiachian.nbatoday.player.ui.state.PlayerState
import com.jiachian.nbatoday.player.ui.state.PlayerStatsState
import com.jiachian.nbatoday.testing.testtag.PlayerTestTag
import com.jiachian.nbatoday.utils.showToast

@Composable
fun PlayerScreen(
    state: PlayerState,
    onEvent: (PlayerUIEvent) -> Unit,
    navigationController: NavigationController,
) {
    val context = LocalContext.current
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
            if (state.loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .testTag(PlayerTestTag.PlayerScreen_Loading)
                        .align(Alignment.Center),
                    color = MaterialTheme.colors.secondary,
                )
            } else if (state.notFound) {
                Text(
                    modifier = Modifier
                        .testTag(PlayerTestTag.PlayerScreen_PlayerNotFound)
                        .align(Alignment.Center),
                    text = stringResource(R.string.player_career_not_found),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            } else {
                PlayerDetail(
                    info = state.info,
                    stats = state.stats,
                    onSortingUpdate = { onEvent(PlayerUIEvent.Sort(it)) }
                )
            }
        }
    }
    LaunchedEffect(state.event) {
        state.event?.let { event ->
            when (event) {
                is PlayerDataEvent.Error -> {
                    when (event.error) {
                        PlayerError.UpdateFailed -> showToast(context, event.error.message)
                    }
                }
            }
            onEvent(PlayerUIEvent.EventReceived)
        }
    }
}

@Composable
private fun PlayerDetail(
    info: PlayerInfoState,
    stats: PlayerStatsState,
    onSortingUpdate: (PlayerStatsSorting) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    LazyColumn(modifier = modifier) {
        playerInfo(info = info)
        playerStats(
            scrollState = scrollState,
            stats = stats,
            onSortingUpdate = onSortingUpdate,
        )
    }
}
