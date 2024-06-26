package com.jiachian.nbatoday.boxscore.ui.team

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
import com.jiachian.nbatoday.boxscore.ui.team.model.BoxScoreTeamRowData
import com.jiachian.nbatoday.common.ui.TeamLogoImage
import com.jiachian.nbatoday.team.data.model.local.teams.NBATeam
import com.jiachian.nbatoday.testing.testtag.BoxScoreTestTag
import com.jiachian.nbatoday.utils.dividerSecondaryColor

@Composable
fun ScoreTeamPage(
    homeTeam: NBATeam,
    awayTeam: NBATeam,
    data: List<BoxScoreTeamRowData>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        item {
            ScoreTeamTitleRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                homeTeam = homeTeam,
                awayTeam = awayTeam,
            )
        }
        items(data) { rowData ->
            TeamStatsRow(
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
private fun TeamStatsRow(
    rowData: BoxScoreTeamRowData,
    modifier: Modifier = Modifier,
) {
    val label = rowData.label
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = if (label.topMargin) 8.dp else 0.dp,
                    start = 4.dp,
                    end = 4.dp
                ),
        ) {
            ScoreTeamStatsText(
                modifier = Modifier
                    .testTag(BoxScoreTestTag.TeamStatsRow_ScoreTeamStatsText_Home)
                    .align(Alignment.CenterStart),
                value = rowData.home,
            )
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(label.textRes),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.secondary
            )
            ScoreTeamStatsText(
                modifier = Modifier
                    .testTag(BoxScoreTestTag.TeamStatsRow_ScoreTeamStatsText_Away)
                    .align(Alignment.CenterEnd),
                value = rowData.away,
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
private fun ScoreTeamTitleRow(
    homeTeam: NBATeam,
    awayTeam: NBATeam,
    modifier: Modifier = Modifier,
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

@Composable
private fun ScoreTeamStatsText(
    value: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = value,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colors.secondary
    )
}
