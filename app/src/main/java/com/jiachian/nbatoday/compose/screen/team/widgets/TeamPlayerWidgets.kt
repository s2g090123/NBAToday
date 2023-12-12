package com.jiachian.nbatoday.compose.screen.team.widgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.Transparency25
import com.jiachian.nbatoday.compose.screen.team.TeamViewModel
import com.jiachian.nbatoday.compose.screen.team.models.TeamPlayerLabel
import com.jiachian.nbatoday.compose.screen.team.models.TeamPlayerRowData
import com.jiachian.nbatoday.compose.screen.team.models.TeamPlayerSorting
import com.jiachian.nbatoday.utils.dividerSecondaryColor
import com.jiachian.nbatoday.utils.rippleClickable
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TeamPlayerPage(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel
) {
    var scrollDelta by remember { mutableStateOf(0f) }
    val draggableState = rememberDraggableState(onDelta = { scrollDelta = it })
    val labelState = rememberLazyListState()
    val sorting by viewModel.playerSorting.collectAsState()
    val teamPlayers by viewModel.sortedPlayerRowData.collectAsState()
    LazyColumn(modifier = modifier) {
        stickyHeader {
            TeamPlayerLabelDraggableRow(
                modifier = Modifier.draggable(draggableState, Orientation.Horizontal),
                viewModel = viewModel,
                labelState = labelState,
                sorting = sorting,
                scrollDelta = scrollDelta,
            )
        }
        items(teamPlayers) { rowData ->
            TeamPlayerDraggableRow(
                modifier = Modifier.draggable(draggableState, Orientation.Horizontal),
                viewModel = viewModel,
                labelState = labelState,
                rowData = rowData,
                sorting = sorting
            )
        }
    }
}

@Composable
private fun TeamPlayerLabelDraggableRow(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    labelState: LazyListState,
    sorting: TeamPlayerSorting,
    scrollDelta: Float,
) {
    Column(modifier = Modifier.background(viewModel.colors.primary)) {
        TeamPlayerLabelRow(
            modifier = modifier,
            viewModel = viewModel,
            state = labelState,
            sorting = sorting,
        )
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = viewModel.colors.secondary.copy(Transparency25),
            thickness = 3.dp,
        )
    }
    LaunchedEffect(scrollDelta) {
        labelState.scrollBy(-scrollDelta)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TeamPlayerLabelRow(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    state: LazyListState,
    sorting: TeamPlayerSorting,
) {
    LazyRow(
        modifier = modifier,
        state = state,
        userScrollEnabled = false,
    ) {
        stickyHeader {
            Box(
                modifier = Modifier
                    .size(120.dp, 40.dp)
                    .background(viewModel.colors.primary),
            )
        }
        items(viewModel.labels) { label ->
            TeamPlayerLabel(
                isSelected = label.sorting == sorting,
                label = label,
                color = viewModel.colors.secondary,
                onClick = { viewModel.updatePlayerSorting(label.sorting) }
            )
        }
    }
}

@Composable
fun TeamPlayerLabel(
    isSelected: Boolean,
    label: TeamPlayerLabel,
    color: Color,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .width(label.width)
            .height(40.dp)
            .background(if (isSelected) color.copy(Transparency25) else Color.Transparent)
            .rippleClickable { onClick() }
            .padding(8.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxSize(),
            text = context.getString(label.textRes),
            textAlign = if (isSelected) TextAlign.Center else TextAlign.End,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = color,
        )
    }
}

@Composable
private fun TeamPlayerDraggableRow(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    labelState: LazyListState,
    rowData: TeamPlayerRowData,
    sorting: TeamPlayerSorting,
) {
    val statsState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val visibleIndex by remember(labelState) { derivedStateOf { labelState.firstVisibleItemIndex } }
    val visibleOffset by remember(labelState) { derivedStateOf { labelState.firstVisibleItemScrollOffset } }
    TeamPlayerRow(
        modifier = modifier,
        viewModel = viewModel,
        state = statsState,
        rowData = rowData,
        sorting = sorting,
    )
    Divider(
        modifier = Modifier.fillMaxWidth(),
        color = dividerSecondaryColor(),
        thickness = 1.dp
    )
    LaunchedEffect(visibleIndex, visibleOffset) {
        // WORKAROUND
        // LazyListState#scrollToItem is suspended if PlayerStatsDraggableRow is not visible on the screen.
        // Once it becomes visible, it may scroll to the wrong position.
        // To address this issue,
        // scrolling twice ensures that PlayerStatsDraggableRows scroll to correct position.
        coroutineScope.launch {
            statsState.scrollToItem(visibleIndex, visibleOffset)
            statsState.scrollToItem(visibleIndex, visibleOffset)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TeamPlayerRow(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    state: LazyListState,
    rowData: TeamPlayerRowData,
    sorting: TeamPlayerSorting
) {
    LazyRow(
        modifier = modifier,
        state = state,
        userScrollEnabled = false,
    ) {
        stickyHeader {
            Surface(color = viewModel.colors.primary) {
                TeamPlayerNameText(
                    modifier = Modifier
                        .testTag("PlayerStatistics_Text_PlayerName")
                        .size(120.dp, 40.dp)
                        .rippleClickable { viewModel.openPlayerInfo(rowData.player.playerId) }
                        .padding(top = 8.dp, bottom = 8.dp, start = 4.dp),
                    playerName = rowData.player.playerName,
                    color = viewModel.colors.secondary,
                )
            }
        }
        items(rowData.data) { data ->
            TeamPlayerStatsText(
                data = data,
                focus = data.sorting == sorting,
                color = viewModel.colors.secondary,
            )
        }
    }
}

@Composable
private fun TeamPlayerNameText(
    modifier: Modifier = Modifier,
    playerName: String,
    color: Color,
) {
    Text(
        modifier = modifier,
        text = playerName,
        fontSize = 16.sp,
        color = color,
        maxLines = 1,
        softWrap = false,
    )
}

@Composable
private fun TeamPlayerStatsText(
    data: TeamPlayerRowData.Data,
    focus: Boolean,
    color: Color,
) {
    Text(
        modifier = Modifier
            .size(data.width, 40.dp)
            .background(if (focus) color.copy(Transparency25) else Color.Transparent)
            .padding(8.dp),
        text = data.value,
        textAlign = if (focus) TextAlign.Center else TextAlign.End,
        fontSize = 16.sp,
        color = color,
    )
}
