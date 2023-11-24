package com.jiachian.nbatoday.compose.screen.score.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.score.BoxScoreViewModel
import com.jiachian.nbatoday.compose.screen.score.data.ScoreTeamRowData
import com.jiachian.nbatoday.compose.widget.TeamLogoImage
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.utils.dividerSecondaryColor

@Composable
fun TeamStatistics(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel
) {
    val homeTeam by viewModel.homeTeam.collectAsState()
    val awayTeam by viewModel.awayTeam.collectAsState()
    val teamRowData by viewModel.teamStatsRowData.collectAsState()
    LazyColumn(modifier = modifier) {
        item {
            TeamStatsTitleRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                homeTeam = homeTeam,
                awayTeam = awayTeam
            )
        }
        items(teamRowData) { rowData ->
            TeamStatsRow(
                modifier = Modifier
                    .testTag("TeamStatistics_TeamStatsRow")
                    .fillMaxWidth(),
                rowData = rowData
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun TeamStatsRow(
    modifier: Modifier = Modifier,
    rowData: ScoreTeamRowData
) {
    val label = rowData.label
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = if (label.topMargin) 8.dp else 0.dp, start = 4.dp, end = 4.dp),
        ) {
            Text(
                modifier = Modifier
                    .testTag("TeamStatsRow_Text_Home")
                    .align(Alignment.CenterStart),
                text = rowData.homeValue,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.secondary
            )
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(label.textRes),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.secondary
            )
            Text(
                modifier = Modifier
                    .testTag("TeamStatsRow_Text_Away")
                    .align(Alignment.CenterEnd),
                text = rowData.awayValue,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.secondary
            )
        }
        if (label.divider) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 12.dp),
                color = dividerSecondaryColor()
            )
        }
    }
}

@Composable
private fun TeamStatsTitleRow(
    modifier: Modifier = Modifier,
    homeTeam: NBATeam,
    awayTeam: NBATeam
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TeamLogoImage(
            modifier = Modifier.size(56.dp),
            team = homeTeam
        )
        Text(
            text = stringResource(R.string.box_score_team_statistics_title),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
        TeamLogoImage(
            modifier = Modifier.size(56.dp),
            team = awayTeam
        )
    }
}
