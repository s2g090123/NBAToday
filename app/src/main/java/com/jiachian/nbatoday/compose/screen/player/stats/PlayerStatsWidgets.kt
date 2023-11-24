package com.jiachian.nbatoday.compose.screen.player.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.Transparency25
import com.jiachian.nbatoday.compose.screen.player.CareerStatsLabel
import com.jiachian.nbatoday.compose.screen.player.CareerStatsRowData
import com.jiachian.nbatoday.compose.screen.player.CareerStatsSort
import com.jiachian.nbatoday.compose.screen.player.CareerTimeFrameRowData
import com.jiachian.nbatoday.compose.screen.player.PlayerViewModel
import com.jiachian.nbatoday.compose.widget.DisableOverscroll
import com.jiachian.nbatoday.utils.dividerSecondaryColor
import com.jiachian.nbatoday.utils.px2Dp
import com.jiachian.nbatoday.utils.rippleClickable
import kotlin.math.max

@Composable
fun PlayerCareerStats(
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel
) {
    val horizontalScrollState = rememberScrollState()
    val timeframeState = rememberLazyListState()
    val statsState = rememberLazyListState()
    val timeframeOffset by remember { derivedStateOf { timeframeState.firstVisibleItemScrollOffset } }
    val timeFrameIndex by remember { derivedStateOf { timeframeState.firstVisibleItemIndex } }
    val statsOffset by remember { derivedStateOf { statsState.firstVisibleItemScrollOffset } }
    val statsIndex by remember { derivedStateOf { statsState.firstVisibleItemIndex } }
    val timeFrameRowData by viewModel.timeFrameRowData.collectAsState()
    val sort by viewModel.statsSort.collectAsState()
    Column(modifier = modifier) {
        PlayerStatsBar(
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colors.secondary)
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            PlayerStatsYearContent(
                modifier = Modifier.width(124.dp),
                state = timeframeState,
                rowData = timeFrameRowData,
                isFocus = sort == CareerStatsSort.TIME_FRAME,
                onClickLabel = { viewModel.updateStatsSort(CareerStatsSort.TIME_FRAME) }
            )
            PlayerStatsContent(
                modifier = Modifier.horizontalScroll(horizontalScrollState),
                viewModel = viewModel,
                state = statsState,
            )
        }
    }
    LaunchedEffect(timeframeOffset, timeFrameIndex) {
        statsState.scrollToItem(timeFrameIndex, timeframeOffset)
    }
    LaunchedEffect(statsOffset, statsIndex) {
        timeframeState.scrollToItem(statsIndex, statsOffset)
    }
}

@Composable
private fun PlayerStatsBar(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
            text = stringResource(R.string.player_career_stats_split),
            color = MaterialTheme.colors.secondaryVariant,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun PlayerStatsContent(
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel,
    state: LazyListState,
) {
    val stats by viewModel.statsRowData.collectAsState()
    val sorting by viewModel.statsSort.collectAsState()
    var dividerWidth by remember { mutableStateOf(0) }
    Column(modifier = modifier) {
        PlayerStatsLabelRow(
            modifier = Modifier
                .onSizeChanged {
                    dividerWidth = max(dividerWidth, it.width)
                }
                .fillMaxWidth()
                .wrapContentHeight(),
            labels = viewModel.statsLabels,
            dividerWidth = dividerWidth.px2Dp(),
            sorting = sorting,
            onClickLabel = { viewModel.updateStatsSort(it.sort) }
        )
        PlayerStatsTable(
            modifier = Modifier
                .testTag("PlayerCareerStats_LC_Stats")
                .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                .fillMaxWidth(),
            state = state,
            stats = stats,
            dividerWidth = dividerWidth.px2Dp()
        )
    }
}

@Composable
private fun PlayerStatsTable(
    modifier: Modifier = Modifier,
    state: LazyListState,
    stats: List<List<CareerStatsRowData>>,
    dividerWidth: Dp
) {
    DisableOverscroll {
        LazyColumn(
            modifier = modifier,
            state = state
        ) {
            items(stats) { stats ->
                Row(
                    modifier = Modifier
                        .testTag("PlayerCareerStats_Row_Stats")
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    stats.forEach { rowData ->
                        PlayerStatsText(rowData = rowData)
                    }
                }
                Divider(
                    modifier = Modifier.width(dividerWidth),
                    color = dividerSecondaryColor(),
                    thickness = 1.dp
                )
            }
        }
    }
}

@Composable
private fun PlayerStatsText(
    rowData: CareerStatsRowData
) {
    Text(
        modifier = Modifier
            .testTag("PlayerCareerStats_Text_Stats")
            .width(rowData.textWidth)
            .height(40.dp)
            .background(
                if (rowData.isFocus) MaterialTheme.colors.secondary.copy(Transparency25)
                else Color.Transparent
            )
            .padding(8.dp),
        text = rowData.value,
        textAlign = if (rowData.isFocus) TextAlign.Center else rowData.textAlign,
        fontSize = 16.sp,
        color = MaterialTheme.colors.secondary
    )
}

@Composable
private fun PlayerStatsLabelRow(
    modifier: Modifier = Modifier,
    labels: List<CareerStatsLabel>,
    dividerWidth: Dp,
    sorting: CareerStatsSort,
    onClickLabel: (CareerStatsLabel) -> Unit
) {
    Row(modifier = modifier) {
        labels.forEach { label ->
            PlayerStatsLabel(
                label = label,
                isFocus = label.sort == sorting,
                onClick = { onClickLabel(label) }
            )
        }
    }
    Divider(
        modifier = Modifier.width(dividerWidth),
        color = dividerSecondaryColor(),
        thickness = 3.dp
    )
}

@Composable
private fun PlayerStatsLabel(
    label: CareerStatsLabel,
    isFocus: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(label.textWidth)
            .height(40.dp)
            .background(
                if (isFocus) MaterialTheme.colors.secondary.copy(Transparency25)
                else Color.Transparent
            )
            .rippleClickable { onClick() }
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxSize(),
            text = label.text,
            textAlign = if (isFocus) TextAlign.Center else label.textAlign,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
    }
}

@Composable
private fun PlayerStatsYearContent(
    modifier: Modifier = Modifier,
    state: LazyListState,
    rowData: List<CareerTimeFrameRowData>,
    isFocus: Boolean,
    onClickLabel: () -> Unit
) {
    Column(modifier = modifier) {
        DisableOverscroll {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(
                        if (isFocus) MaterialTheme.colors.secondary.copy(Transparency25)
                        else Color.Transparent
                    )
                    .rippleClickable { onClickLabel() }
                    .padding(8.dp),
                text = "By Year",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.secondary
            )
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = dividerSecondaryColor(),
                thickness = 3.dp
            )
            PlayerStatsYearColumn(
                modifier = Modifier
                    .testTag("PlayerCareerStats_LC_Year")
                    .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp),
                state = state,
                rowData = rowData,
                isFocus = isFocus
            )
        }
    }
}

@Composable
private fun PlayerStatsYearColumn(
    modifier: Modifier = Modifier,
    state: LazyListState,
    rowData: List<CareerTimeFrameRowData>,
    isFocus: Boolean
) {
    LazyColumn(
        modifier = modifier,
        state = state
    ) {
        items(rowData) { data ->
            Column {
                PlayerStatsYearText(
                    modifier = Modifier
                        .testTag("PlayerCareerStats_Row_Year")
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(
                            if (isFocus) MaterialTheme.colors.secondary.copy(Transparency25)
                            else Color.Transparent
                        ),
                    time = data.timeFrame,
                    teamNameAbbr = data.teamNameAbbr
                )
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = dividerSecondaryColor(),
                    thickness = 1.dp
                )
            }
        }
    }
}

@Composable
private fun PlayerStatsYearText(
    modifier: Modifier = Modifier,
    time: String,
    teamNameAbbr: String
) {
    Row(modifier = modifier) {
        Text(
            modifier = Modifier
                .testTag("PlayerCareerStats_Text_Year")
                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                .weight(1f),
            text = time,
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            color = MaterialTheme.colors.secondary,
            maxLines = 1,
            softWrap = false
        )
        Text(
            modifier = Modifier
                .testTag("PlayerCareerStats_Text_TeamNaeAbbr")
                .padding(end = 8.dp, top = 8.dp, bottom = 8.dp),
            text = teamNameAbbr,
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            color = MaterialTheme.colors.secondary,
            maxLines = 1,
            softWrap = false
        )
    }
}
