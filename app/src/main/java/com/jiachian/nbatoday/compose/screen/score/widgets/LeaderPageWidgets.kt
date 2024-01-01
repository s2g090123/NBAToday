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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreLeaderRowData
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreUI
import com.jiachian.nbatoday.compose.widget.PlayerImage
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.testing.testtag.BoxScoreTestTag
import com.jiachian.nbatoday.utils.dividerSecondaryColor

@Composable
fun ScoreLeaderPage(
    modifier: Modifier = Modifier,
    leadersUI: BoxScoreUI.BoxScoreLeadersUI,
) {
    LazyColumn(modifier = modifier) {
        item {
            ScoreLeaderTitleRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                homeLeader = leadersUI.home,
                awayLeader = leadersUI.away,
            )
        }
        items(leadersUI.rowData) { rowData ->
            ScoreLeaderRow(
                modifier = Modifier.fillMaxWidth(),
                rowData = rowData
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ScoreLeaderTitleRow(
    modifier: Modifier = Modifier,
    homeLeader: BoxScore.BoxScoreTeam.Player?,
    awayLeader: BoxScore.BoxScoreTeam.Player?
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlayerImage(
            modifier = Modifier.size(56.dp),
            playerId = homeLeader?.playerId
        )
        Text(
            text = stringResource(R.string.box_score_leader_statistics_title),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
        PlayerImage(
            modifier = Modifier.size(56.dp),
            playerId = awayLeader?.playerId
        )
    }
}

@Composable
private fun ScoreLeaderRow(
    modifier: Modifier = Modifier,
    rowData: BoxScoreLeaderRowData
) {
    val label = rowData.label
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .padding(
                    top = if (label.topMargin) 8.dp else 0.dp,
                    start = 4.dp,
                    end = 4.dp
                )
                .fillMaxWidth()
        ) {
            LeaderStatsText(
                modifier = Modifier
                    .testTag(BoxScoreTestTag.ScoreLeaderRow_LeaderStatsText_Home)
                    .align(Alignment.CenterStart),
                value = rowData.home
            )
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(label.textRes),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.secondary
            )
            LeaderStatsText(
                modifier = Modifier
                    .testTag(BoxScoreTestTag.ScoreLeaderRow_LeaderStatsText_Away)
                    .align(Alignment.CenterEnd),
                value = rowData.away
            )
        }
        if (label.bottomDivider) {
            Divider(
                modifier = Modifier.padding(top = 12.dp, bottom = 12.dp),
                color = dividerSecondaryColor()
            )
        }
    }
}

@Composable
private fun LeaderStatsText(
    modifier: Modifier = Modifier,
    value: String,
) {
    Text(
        modifier = modifier,
        text = value,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colors.secondary
    )
}
