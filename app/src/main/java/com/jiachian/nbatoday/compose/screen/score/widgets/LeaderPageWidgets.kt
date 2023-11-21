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
import com.jiachian.nbatoday.compose.screen.score.data.ScoreLeaderRowData
import com.jiachian.nbatoday.compose.widget.PlayerImage
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.utils.dividerSecondaryColor
import com.jiachian.nbatoday.utils.modifyIf

@Composable
fun LeaderStatistics(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel
) {
    val homeLeader by viewModel.homeLeader.collectAsState()
    val awayLeader by viewModel.awayLeader.collectAsState()
    val leaderRowData by viewModel.leaderStatsRowData.collectAsState()
    LazyColumn(modifier = modifier) {
        item {
            LeaderStatisticsTitleRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                homeLeader = homeLeader,
                awayLeader = awayLeader
            )
        }
        items(leaderRowData) { rowData ->
            LeaderStatisticsRow(
                modifier = Modifier
                    .testTag("LeaderStatistics_LeaderStatisticsRow")
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
private fun LeaderStatisticsTitleRow(
    modifier: Modifier = Modifier,
    homeLeader: GameBoxScore.BoxScoreTeam.Player?,
    awayLeader: GameBoxScore.BoxScoreTeam.Player?
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlayerImage(
            modifier = Modifier.size(56.dp),
            playerId = homeLeader?.personId
        )
        Text(
            text = stringResource(R.string.box_score_leader_statistics_title),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
        PlayerImage(
            modifier = Modifier.size(56.dp),
            playerId = awayLeader?.personId
        )
    }
}

@Composable
private fun LeaderStatisticsRow(
    modifier: Modifier = Modifier,
    rowData: ScoreLeaderRowData
) {
    val label = rowData.label
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .modifyIf(
                    condition = label.topMargin,
                    modify = { padding(top = 8.dp, start = 4.dp, end = 4.dp) },
                    elseModify = { padding(horizontal = 4.dp) }
                )
        ) {
            Text(
                modifier = Modifier
                    .testTag("LeaderStatisticsRow_Text_Home")
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
                    .testTag("LeaderStatisticsRow_Text_Away")
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
