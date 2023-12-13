package com.jiachian.nbatoday.compose.screen.score.widgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.score.BoxScoreViewModel
import com.jiachian.nbatoday.compose.screen.score.models.BoxScorePlayerLabel
import com.jiachian.nbatoday.compose.screen.score.models.BoxScorePlayerRowData
import com.jiachian.nbatoday.utils.dividerSecondaryColor
import com.jiachian.nbatoday.utils.rippleClickable
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScorePlayerPage(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel,
    players: List<BoxScorePlayerRowData>,
) {
    var scrollDelta by remember { mutableStateOf(0f) }
    val scrollableState = rememberScrollableState {
        scrollDelta = it
        scrollDelta
    }
    val labelState = rememberLazyListState()
    LazyColumn(modifier = modifier) {
        stickyHeader {
            ScorePlayerLabelScrollableRow(
                modifier = Modifier.scrollable(scrollableState, Orientation.Horizontal),
                viewModel = viewModel,
                labelState = labelState,
                scrollDelta = scrollDelta,
            )
        }
        items(players) { rowData ->
            ScorePlayerScrollableRow(
                modifier = Modifier.scrollable(scrollableState, Orientation.Horizontal),
                viewModel = viewModel,
                labelState = labelState,
                rowData = rowData,
            )
        }
    }
}

@Composable
private fun ScorePlayerLabelScrollableRow(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel,
    labelState: LazyListState,
    scrollDelta: Float,
) {
    Column(modifier = Modifier.background(MaterialTheme.colors.primary)) {
        ScorePlayerLabelRow(
            modifier = modifier,
            viewModel = viewModel,
            state = labelState,
        )
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = dividerSecondaryColor(),
            thickness = 3.dp,
        )
    }
    LaunchedEffect(scrollDelta) {
        labelState.scrollBy(-scrollDelta)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScorePlayerLabelRow(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel,
    state: LazyListState,
) {
    val selectedLabel by viewModel.selectedPlayerLabel.collectAsState()
    LazyRow(
        modifier = modifier,
        state = state,
        userScrollEnabled = false,
    ) {
        stickyHeader {
            Text(
                modifier = Modifier
                    .size(120.dp, 40.dp)
                    .background(MaterialTheme.colors.primary)
                    .padding(top = 8.dp, bottom = 8.dp, start = 4.dp),
                text = stringResource(R.string.box_score_label_player),
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.secondary
            )
        }
        item {
            Spacer(modifier = Modifier.width(40.dp))
        }
        items(viewModel.playerLabels) { label ->
            ScorePlayerLabel(
                label = label,
                isPopupVisible = label == selectedLabel,
                onClick = { viewModel.selectPlayerLabel(label) },
                onDismiss = { viewModel.selectPlayerLabel(null) }
            )
        }
    }
}

@Composable
private fun ScorePlayerLabel(
    label: BoxScorePlayerLabel,
    isPopupVisible: Boolean,
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .testTag("PlayerStatistics_Box_Label")
            .size(label.width, 40.dp)
            .rippleClickable { onClick() }
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxSize(),
            text = stringResource(label.textRes),
            textAlign = label.align,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
        if (isPopupVisible) {
            ScorePlayerLabelPopup(
                text = stringResource(label.infoRes),
                onDismiss = { onDismiss() }
            )
        }
    }
}

@Composable
private fun ScorePlayerLabelPopup(
    text: String,
    onDismiss: () -> Unit
) {
    Popup(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .shadow(8.dp)
                .wrapContentSize()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colors.secondaryVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier
                    .testTag("LabelAboutPopup_Text_About")
                    .padding(8.dp),
                text = text,
                color = MaterialTheme.colors.primaryVariant
            )
        }
    }
}

@Composable
private fun ScorePlayerScrollableRow(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel,
    labelState: LazyListState,
    rowData: BoxScorePlayerRowData,
) {
    val statsState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val visibleIndex by remember(labelState) { derivedStateOf { labelState.firstVisibleItemIndex } }
    val visibleOffset by remember(labelState) { derivedStateOf { labelState.firstVisibleItemScrollOffset } }
    ScorePlayerRow(
        modifier = modifier,
        viewModel = viewModel,
        state = statsState,
        rowData = rowData,
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
            //statsState.scrollToItem(visibleIndex, visibleOffset)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScorePlayerRow(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel,
    state: LazyListState,
    rowData: BoxScorePlayerRowData,
) {
    LazyRow(
        modifier = modifier,
        state = state,
        userScrollEnabled = false,
    ) {
        stickyHeader {
            Surface(color = MaterialTheme.colors.primary) {
                ScorePlayerNameText(
                    modifier = Modifier
                        .testTag("PlayerStatistics_Text_PlayerName")
                        .size(120.dp, 40.dp)
                        .rippleClickable { viewModel.openPlayerInfo(rowData.player.playerId) }
                        .padding(top = 8.dp, bottom = 8.dp, start = 4.dp),
                    playerName = rowData.player.nameAbbr,
                    color = MaterialTheme.colors.secondary,
                )
            }
        }
        item {
            Text(
                modifier = Modifier
                    .size(40.dp)
                    .padding(8.dp),
                text = if (rowData.player.starter) rowData.player.position else "",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = MaterialTheme.colors.secondary,
            )
        }
        items(rowData.data) { data ->
            ScorePlayerStatsText(
                data = data,
                color = MaterialTheme.colors.secondary,
            )
        }
    }
}

@Composable
private fun ScorePlayerNameText(
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
private fun ScorePlayerStatsText(
    data: BoxScorePlayerRowData.Data,
    color: Color,
) {
    Text(
        modifier = Modifier
            .size(data.width, 40.dp)
            .padding(8.dp),
        text = data.value,
        textAlign = data.align,
        fontSize = 16.sp,
        color = color,
    )
}
