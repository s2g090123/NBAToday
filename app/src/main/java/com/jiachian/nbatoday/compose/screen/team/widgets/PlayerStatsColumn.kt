package com.jiachian.nbatoday.compose.screen.team.widgets

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.Transparency25
import com.jiachian.nbatoday.compose.screen.team.PlayerSort
import com.jiachian.nbatoday.compose.screen.team.TeamPlayerLabel
import com.jiachian.nbatoday.compose.screen.team.TeamViewModel
import com.jiachian.nbatoday.compose.widget.DisableOverscroll
import com.jiachian.nbatoday.models.local.team.TeamPlayer
import com.jiachian.nbatoday.utils.px2Dp
import com.jiachian.nbatoday.utils.rippleClickable
import kotlin.math.max

@Composable
fun PlayerStatsColumn(
    viewModel: TeamViewModel,
    lazyListState: LazyListState,
    players: List<TeamPlayer>,
) {
    val horizontalScrollState = rememberScrollState()
    PlayerStatsTable(
        modifier = Modifier
            .testTag("PlayerStatistics_LC_PlayerStats")
            .horizontalScroll(horizontalScrollState)
            .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
            .fillMaxWidth(),
        viewModel = viewModel,
        statsState = lazyListState,
        players = players,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlayerStatsTable(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    statsState: LazyListState,
    players: List<TeamPlayer>
) {
    val sort by viewModel.playerSort.collectAsState()
    var dividerWidth by remember { mutableStateOf(0) }
    DisableOverscroll {
        LazyColumn(
            modifier = modifier,
            state = statsState,
        ) {
            stickyHeader {
                PlayerStatsHeader(
                    modifier = Modifier
                        .onSizeChanged { dividerWidth = max(dividerWidth, it.width) }
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(viewModel.colors.primary),
                    viewModel = viewModel,
                    sort = sort,
                    dividerWidth = dividerWidth.px2Dp(),
                )
            }
            itemsIndexed(players) { index, stats ->
                PlayerStatsRow(
                    viewModel = viewModel,
                    stats = stats,
                    sort = sort,
                    divider = index < players.size - 1,
                    dividerWidth = dividerWidth.px2Dp(),
                )
            }
        }
    }
}

@Composable
private fun PlayerStatsHeader(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    sort: PlayerSort,
    dividerWidth: Dp
) {
    Column(modifier = modifier) {
        PlayerStatsLabelRow(
            labels = viewModel.labels,
            sort = sort,
            labelColor = viewModel.colors.secondary,
            updateSort = { viewModel.updatePlayerSort(it) },
        )
        Divider(
            modifier = Modifier.width(dividerWidth),
            color = viewModel.colors.secondary.copy(Transparency25),
            thickness = 3.dp,
        )
    }
}

@Composable
private fun PlayerStatsLabelRow(
    modifier: Modifier = Modifier,
    labels: Array<TeamPlayerLabel>,
    sort: PlayerSort,
    labelColor: Color,
    updateSort: (sort: PlayerSort) -> Unit
) {
    Row(modifier = modifier) {
        labels.forEach { label ->
            PlayerStatsLabel(
                isSelected = sort == label.sort,
                label = label,
                color = labelColor,
                onClick = { updateSort(label.sort) },
            )
        }
    }
}

@Composable
private fun PlayerStatsLabel(
    isSelected: Boolean,
    label: TeamPlayerLabel,
    color: Color,
    onClick: (label: TeamPlayerLabel) -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .width(label.width)
            .height(40.dp)
            .background(if (isSelected) color.copy(Transparency25) else Color.Transparent)
            .rippleClickable { onClick(label) }
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
private fun PlayerStatsRow(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    stats: TeamPlayer,
    sort: PlayerSort,
    divider: Boolean,
    dividerWidth: Dp,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .testTag("PlayerStatistics_Row_PlayerStats")
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            viewModel.labels.forEach { label ->
                PlayerStatsText(
                    isSelected = sort == label.sort,
                    width = label.width,
                    color = viewModel.colors.secondary,
                    text = viewModel.getStatsTextByLabel(label, stats),
                )
            }
        }
        if (divider) {
            Divider(
                modifier = Modifier.width(dividerWidth),
                color = viewModel.colors.secondary.copy(Transparency25),
                thickness = 1.dp,
            )
        }
    }
}

@Composable
private fun PlayerStatsText(
    isSelected: Boolean,
    width: Dp,
    color: Color,
    text: String
) {
    Text(
        modifier = Modifier
            .testTag("PlayerStatistics_Text_PlayerStats")
            .width(width)
            .height(40.dp)
            .background(if (isSelected) color.copy(Transparency25) else Color.Transparent)
            .padding(8.dp),
        text = text,
        textAlign = if (isSelected) TextAlign.Center else TextAlign.End,
        fontSize = 16.sp,
        color = color,
    )
}
