package com.jiachian.nbatoday.compose.screen.team.widgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.Transparency25
import com.jiachian.nbatoday.compose.screen.team.TeamViewModel
import com.jiachian.nbatoday.compose.screen.team.models.TeamPlayerLabel
import com.jiachian.nbatoday.compose.screen.team.models.TeamPlayerRowData
import com.jiachian.nbatoday.compose.screen.team.models.TeamPlayerSorting
import com.jiachian.nbatoday.testing.testtag.TeamTestTag
import com.jiachian.nbatoday.utils.modifyIf
import com.jiachian.nbatoday.utils.rippleClickable

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TeamPlayerPage(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    teamPlayers: List<TeamPlayerRowData>,
) {
    val scrollState = rememberScrollState()
    val sorting by viewModel.playerSorting.collectAsState()
    LazyColumn(modifier = modifier) {
        stickyHeader {
            ScorePlayerLabelScrollableRow(
                viewModel = viewModel,
                scrollState = scrollState,
                sorting = sorting,
            )
        }
        items(teamPlayers) { rowData ->
            TeamPlayerScrollableRow(
                viewModel = viewModel,
                scrollState = scrollState,
                rowData = rowData,
                sorting = sorting
            )
        }
    }
}

@Composable
private fun ScorePlayerLabelScrollableRow(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    scrollState: ScrollState,
    sorting: TeamPlayerSorting,
) {
    Column(modifier = Modifier.background(viewModel.colors.primary)) {
        TeamPlayerLabelRow(
            modifier = modifier,
            viewModel = viewModel,
            scrollState = scrollState,
            sorting = sorting,
        )
        Divider(
            color = viewModel.colors.secondary.copy(Transparency25),
            thickness = 3.dp,
        )
    }
}

@Composable
private fun TeamPlayerLabelRow(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    scrollState: ScrollState,
    sorting: TeamPlayerSorting,
) {
    Row(modifier = modifier) {
        Spacer(modifier = Modifier.width(120.dp))
        Row(modifier = Modifier.horizontalScroll(scrollState)) {
            viewModel.labels.forEach { label ->
                TeamPlayerLabel(
                    focus = label.sorting == sorting,
                    label = label,
                    color = viewModel.colors.secondary,
                    onClick = { viewModel.updatePlayerSorting(label.sorting) }
                )
            }
        }
    }
}

@Composable
fun TeamPlayerLabel(
    focus: Boolean,
    label: TeamPlayerLabel,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(label.width)
            .height(40.dp)
            .modifyIf(focus) { background(color.copy(Transparency25)) }
            .rippleClickable { onClick() }
            .padding(8.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxSize(),
            text = stringResource(label.textRes),
            textAlign = if (focus) TextAlign.Center else TextAlign.End,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = color,
        )
    }
}

@Composable
private fun TeamPlayerScrollableRow(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    scrollState: ScrollState,
    rowData: TeamPlayerRowData,
    sorting: TeamPlayerSorting,
) {
    TeamPlayerRow(
        modifier = modifier,
        viewModel = viewModel,
        scrollState = scrollState,
        rowData = rowData,
        sorting = sorting,
    )
    Divider(color = viewModel.colors.secondary.copy(Transparency25))
}

@Composable
private fun TeamPlayerRow(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    scrollState: ScrollState,
    rowData: TeamPlayerRowData,
    sorting: TeamPlayerSorting
) {
    Row(modifier = modifier) {
        TeamPlayerNameText(
            modifier = Modifier
                .testTag(TeamTestTag.TeamPlayerRow_Text_PlayerName)
                .size(120.dp, 40.dp)
                .rippleClickable { viewModel.onPlayerClick(rowData.player.playerId) }
                .padding(top = 8.dp, bottom = 8.dp, start = 4.dp),
            name = rowData.player.playerName,
            color = viewModel.colors.secondary,
        )
        Row(modifier = Modifier.horizontalScroll(scrollState)) {
            rowData.data.forEach { data ->
                TeamPlayerStatsText(
                    data = data,
                    focus = data.sorting == sorting,
                    color = viewModel.colors.secondary,
                )
            }
        }
    }
}

@Composable
private fun TeamPlayerNameText(
    modifier: Modifier = Modifier,
    name: String,
    color: Color,
) {
    Text(
        modifier = modifier,
        text = name,
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
            .testTag(TeamTestTag.TeamPlayerStatsText)
            .size(data.width, 40.dp)
            .modifyIf(focus) { background(color.copy(Transparency25)) }
            .padding(8.dp),
        text = data.value,
        textAlign = if (focus) TextAlign.Center else TextAlign.End,
        fontSize = 16.sp,
        color = color,
    )
}
