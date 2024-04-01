package com.jiachian.nbatoday.player.ui

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.data.Transparency25
import com.jiachian.nbatoday.player.ui.model.PlayerStatsLabel
import com.jiachian.nbatoday.player.ui.model.PlayerStatsRowData
import com.jiachian.nbatoday.player.ui.model.PlayerStatsSorting
import com.jiachian.nbatoday.player.ui.state.PlayerStatsState
import com.jiachian.nbatoday.testing.testtag.PlayerTestTag
import com.jiachian.nbatoday.utils.dividerSecondaryColor
import com.jiachian.nbatoday.utils.modifyIf
import com.jiachian.nbatoday.utils.rippleClickable

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.playerStats(
    scrollState: ScrollState,
    stats: PlayerStatsState,
    onSortingUpdate: (PlayerStatsSorting) -> Unit,
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
        PlayerStatsLabelScrollableRow(
            scrollState = scrollState,
            sorting = stats.sorting,
            onSortingUpdate = onSortingUpdate,
        )
    }
    items(
        stats.data,
        key = { it.timeFrame + it.teamAbbr }
    ) {
        PlayerStatsScrollableRow(
            scrollState = scrollState,
            rowData = it,
            sorting = stats.sorting,
        )
    }
}

@Composable
private fun PlayerStatsLabelScrollableRow(
    scrollState: ScrollState,
    sorting: PlayerStatsSorting,
    onSortingUpdate: (PlayerStatsSorting) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = Modifier.background(MaterialTheme.colors.primary)) {
        PlayerStatsLabelRow(
            modifier = modifier,
            scrollState = scrollState,
            sorting = sorting,
            onSortingUpdate = onSortingUpdate,
        )
        Divider(
            color = dividerSecondaryColor(),
            thickness = 3.dp
        )
    }
}

@Composable
private fun PlayerStatsScrollableRow(
    scrollState: ScrollState,
    rowData: PlayerStatsRowData,
    sorting: PlayerStatsSorting,
    modifier: Modifier = Modifier,
) {
    PlayerStatsRow(
        modifier = modifier,
        scrollState = scrollState,
        rowData = rowData,
        sorting = sorting,
    )
    Divider(color = dividerSecondaryColor())
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
    scrollState: ScrollState,
    sorting: PlayerStatsSorting,
    onSortingUpdate: (PlayerStatsSorting) -> Unit,
    modifier: Modifier = Modifier,
) {
    val labels = remember { PlayerStatsLabel.values() }
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
                .rippleClickable { onSortingUpdate(PlayerStatsSorting.TIME_FRAME) }
                .padding(8.dp),
            text = stringResource(R.string.player_career_by_year),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
        Row(modifier = Modifier.horizontalScroll(scrollState)) {
            labels.forEach { label ->
                PlayerStatsLabel(
                    label = label,
                    focus = label.sorting == sorting,
                    onClick = { onSortingUpdate(label.sorting) }
                )
            }
        }
    }
}

@Composable
private fun PlayerStatsRow(
    scrollState: ScrollState,
    rowData: PlayerStatsRowData,
    sorting: PlayerStatsSorting,
    modifier: Modifier = Modifier,
) {
    val selectTimeFrame by remember(sorting) {
        derivedStateOf { sorting == PlayerStatsSorting.TIME_FRAME }
    }
    Row(modifier = modifier) {
        PlayerStatsYearText(
            modifier = Modifier
                .size(120.dp, 40.dp)
                .background(
                    if (selectTimeFrame) MaterialTheme.colors.secondary.copy(Transparency25)
                    else MaterialTheme.colors.primary,
                ),
            time = rowData.timeFrame,
            teamNameAbbr = rowData.teamAbbr
        )
        Row(modifier = Modifier.horizontalScroll(scrollState)) {
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
            .modifyIf(focus, MaterialTheme.colors.secondary.copy(Transparency25)) { background(it) }
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
    focus: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(label.width)
            .height(40.dp)
            .modifyIf(focus, MaterialTheme.colors.secondary.copy(Transparency25)) { background(it) }
            .rippleClickable { onClick() }
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxSize(),
            text = stringResource(label.textRes),
            textAlign = if (focus) TextAlign.Center else label.align,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
    }
}

@Composable
private fun PlayerStatsYearText(
    time: String,
    teamNameAbbr: String,
    modifier: Modifier = Modifier,
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
