package com.jiachian.nbatoday.compose.screen.team.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.compose.screen.team.TeamViewModel
import com.jiachian.nbatoday.models.local.team.TeamPlayerStats

@Composable
fun PlayerStatistics(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    players: List<TeamPlayerStats>
) {
    val playerState = rememberLazyListState()
    val statsState = rememberLazyListState()
    val statePlayerOffset by remember { derivedStateOf { playerState.firstVisibleItemScrollOffset } }
    val statePlayerIndex by remember { derivedStateOf { playerState.firstVisibleItemIndex } }
    val stateStatsOffset by remember { derivedStateOf { statsState.firstVisibleItemScrollOffset } }
    val stateStatsIndex by remember { derivedStateOf { statsState.firstVisibleItemIndex } }
    Row(modifier = modifier) {
        PlayerNamesColumn(
            modifier = Modifier
                .testTag("PlayerStatistics_LC_Players")
                .width(124.dp)
                .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp),
            viewModel = viewModel,
            playerState = playerState,
            players = players,
            textColor = viewModel.colors.secondary,
            onClickPlayer = { viewModel.openPlayerInfo(it) },
        )
        PlayerStatsColumn(
            viewModel = viewModel,
            lazyListState = statsState,
            players = players,
        )
    }
    LaunchedEffect(statePlayerOffset, statePlayerIndex) {
        statsState.scrollToItem(statePlayerIndex, statePlayerOffset)
    }
    LaunchedEffect(stateStatsOffset, stateStatsIndex) {
        playerState.scrollToItem(stateStatsIndex, stateStatsOffset)
    }
}
