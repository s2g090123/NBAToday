package com.jiachian.nbatoday.compose.screen.player.widgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.Transparency25
import com.jiachian.nbatoday.compose.screen.player.PlayerViewModel
import com.jiachian.nbatoday.compose.screen.player.models.PlayerStatsLabel
import com.jiachian.nbatoday.compose.screen.player.models.PlayerStatsRowData
import com.jiachian.nbatoday.compose.screen.player.models.PlayerStatsSorting
import com.jiachian.nbatoday.testing.testtag.PlayerTestTag
import com.jiachian.nbatoday.utils.dividerSecondaryColor
import com.jiachian.nbatoday.utils.rippleClickable

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.playerStats(
    viewModel: PlayerViewModel,
    scrollState: ScrollState,
    statsRowData: List<PlayerStatsRowData>,
    sorting: PlayerStatsSorting,
) {
    item {
        PlayerStatsBar(
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colors.secondary)
        )
    }
    stickyHeader {
        PlayerStatsLabelDraggableRow(
            viewModel = viewModel,
            scrollState = scrollState,
            sorting = sorting,
        )
    }
    items(statsRowData) { rowData ->
        PlayerStatsDraggableRow(
            scrollState = scrollState,
            rowData = rowData,
            sorting = sorting,
        )
    }
}

@Composable
private fun PlayerStatsLabelDraggableRow(
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel,
    scrollState: ScrollState,
    sorting: PlayerStatsSorting,
) {
    Column(modifier = Modifier.background(MaterialTheme.colors.primary)) {
        PlayerStatsLabelRow(
            modifier = modifier,
            viewModel = viewModel,
            scrollState = scrollState,
            sorting = sorting,
        )
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = dividerSecondaryColor(),
            thickness = 3.dp
        )
    }
}

@Composable
private fun PlayerStatsDraggableRow(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    rowData: PlayerStatsRowData,
    sorting: PlayerStatsSorting,
) {
    PlayerStatsRow(
        modifier = modifier,
        scrollState = scrollState,
        rowData = rowData,
        sorting = sorting,
    )
    Divider(
        modifier = Modifier.fillMaxWidth(),
        color = dividerSecondaryColor(),
        thickness = 1.dp
    )
}

@Composable
private fun PlayerStatsBar(modifier: Modifier = Modifier) {
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
private fun PlayerStatsLabelRow(
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel,
    scrollState: ScrollState,
    sorting: PlayerStatsSorting,
) {
    val selectTimeFrame by remember(sorting) {
        derivedStateOf { sorting == PlayerStatsSorting.TIME_FRAME }
    }
    Row(modifier = modifier) {
        Text(
            modifier = Modifier
                .size(120.dp, 40.dp)
                .background(
                    if (selectTimeFrame) MaterialTheme.colors.secondary.copy(Transparency25)
                    else MaterialTheme.colors.primary,
                )
                .rippleClickable { viewModel.updateStatsSorting(PlayerStatsSorting.TIME_FRAME) }
                .padding(8.dp),
            text = stringResource(R.string.player_career_by_year),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
        ) {
            viewModel.statsLabels.forEach { label ->
                PlayerStatsLabel(
                    label = label,
                    isFocus = label.sorting == sorting,
                    onClick = { viewModel.updateStatsSorting(label.sorting) }
                )
            }
        }
    }
}

@Composable
private fun PlayerStatsRow(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    rowData: PlayerStatsRowData,
    sorting: PlayerStatsSorting
) {
    val selectTimeFrame by remember(sorting) {
        derivedStateOf { sorting == PlayerStatsSorting.TIME_FRAME }
    }
    Row(modifier = modifier) {
        PlayerStatsYearText(
            modifier = Modifier
                .testTag(PlayerTestTag.PlayerStatsRow_PlayerStatsYearText)
                .size(120.dp, 40.dp)
                .background(
                    if (selectTimeFrame) MaterialTheme.colors.secondary.copy(Transparency25)
                    else MaterialTheme.colors.primary,
                ),
            time = rowData.timeFrame,
            teamNameAbbr = rowData.teamAbbr
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
        ) {
            rowData.data.forEach { data ->
                PlayerStatsText(
                    data = data,
                    focus = data.sorting == sorting,
                )
            }
        }
    }
}

@Composable
private fun PlayerStatsText(
    data: PlayerStatsRowData.Data,
    focus: Boolean,
) {
    Text(
        modifier = Modifier
            .testTag(PlayerTestTag.PlayerStatsText_Text)
            .size(data.width, 40.dp)
            .background(
                if (focus) MaterialTheme.colors.secondary.copy(Transparency25)
                else Color.Transparent
            )
            .padding(8.dp),
        text = data.value,
        textAlign = if (focus) TextAlign.Center else data.align,
        fontSize = 16.sp,
        color = MaterialTheme.colors.secondary
    )
}

@Composable
private fun PlayerStatsLabel(
    label: PlayerStatsLabel,
    isFocus: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(label.width)
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
            text = stringResource(label.textRes),
            textAlign = if (isFocus) TextAlign.Center else label.align,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
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
                .testTag(PlayerTestTag.PlayerStatsYearText_Text_Time)
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
                .testTag(PlayerTestTag.PlayerStatsYearText_Text_TeamName)
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
