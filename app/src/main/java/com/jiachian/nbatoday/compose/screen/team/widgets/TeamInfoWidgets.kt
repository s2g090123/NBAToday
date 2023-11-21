package com.jiachian.nbatoday.compose.screen.team.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.team.TeamViewModel
import com.jiachian.nbatoday.compose.widget.TeamLogoImage
import com.jiachian.nbatoday.data.local.team.TeamStats
import com.jiachian.nbatoday.utils.decimalFormat
import com.jiachian.nbatoday.utils.toRank

@Composable
fun TeamInformation(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel
) {
    val stats by viewModel.teamStats.collectAsState()
    val teamRank by viewModel.teamRank.collectAsState()
    stats?.let {
        Column(modifier = modifier) {
            TeamNameAndStanding(
                stats = it,
                teamRank = teamRank,
                textColor = viewModel.colors.extra2,
            )
            TeamStatsDetail(
                modifier = Modifier
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .height(IntrinsicSize.Min),
                viewModel = viewModel,
                stats = it,
            )
        }
    }
}

@Composable
private fun TeamNameAndStanding(
    modifier: Modifier = Modifier,
    stats: TeamStats,
    teamRank: Int,
    textColor: Color,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TeamLogoImage(
            modifier = Modifier
                .padding(start = 8.dp)
                .size(120.dp),
            team = stats.team,
        )
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 8.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.testTag("TeamInformation_Text_TeamName"),
                text = stats.team.teamFullName,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = textColor
            )
            Text(
                modifier = Modifier.testTag("TeamInformation_Text_TeamRecord"),
                text = stringResource(
                    R.string.team_rank_record,
                    stats.win,
                    stats.lose,
                    teamRank.toRank(),
                    stats.teamConference.toString()
                ),
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = textColor
            )
        }
    }
}

@Composable
private fun TeamStatsDetail(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    stats: TeamStats,
) {
    val pointsRank by viewModel.teamPointsRank.collectAsState()
    val reboundsRank by viewModel.teamReboundsRank.collectAsState()
    val assistsRank by viewModel.teamAssistsRank.collectAsState()
    val plusMinusRank by viewModel.teamPlusMinusRank.collectAsState()
    Row(modifier = modifier) {
        TeamRankBox(
            modifier = Modifier.testTag("TeamInformation_Column_PointsRank"),
            label = stringResource(R.string.team_rank_points_abbr),
            rank = pointsRank,
            average = stats.pointsAverage,
            textColor = viewModel.colors.extra2,
            divider = true,
        )
        TeamRankBox(
            modifier = Modifier.testTag("TeamInformation_Column_ReboundsRank"),
            label = stringResource(R.string.team_rank_rebounds_abbr),
            rank = reboundsRank,
            average = stats.reboundsAverage,
            textColor = viewModel.colors.extra2,
            divider = true,
        )
        TeamRankBox(
            modifier = Modifier.testTag("TeamInformation_Column_AssistsRank"),
            label = stringResource(R.string.team_rank_assists_abbr),
            rank = assistsRank,
            average = stats.assistsAverage,
            textColor = viewModel.colors.extra2,
            divider = true
        )
        TeamRankBox(
            modifier = Modifier.testTag("TeamInformation_Column_PlusMinusRank"),
            label = stringResource(R.string.team_rank_plusMinus_abbr),
            rank = plusMinusRank,
            average = stats.plusMinusAverage,
            textColor = viewModel.colors.extra2,
            divider = false
        )
    }
}

@Composable
private fun TeamRankBox(
    modifier: Modifier = Modifier,
    label: String,
    rank: Int,
    average: Double,
    textColor: Color,
    divider: Boolean,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = textColor
            )
            Text(
                modifier = Modifier.testTag("TeamInformation_Text_Rank"),
                text = rank.toRank(),
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = textColor
            )
            Text(
                modifier = Modifier.testTag("TeamInformation_Text_Average"),
                text = average.decimalFormat().toString(),
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = textColor
            )
        }
        if (divider) {
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                color = textColor.copy(0.25f)
            )
        }
    }
}
