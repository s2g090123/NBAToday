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
import com.jiachian.nbatoday.Transparency25
import com.jiachian.nbatoday.compose.screen.team.TeamViewModel
import com.jiachian.nbatoday.compose.widget.TeamLogoImage
import com.jiachian.nbatoday.models.local.team.Team
import com.jiachian.nbatoday.testing.testtag.TeamTestTag
import com.jiachian.nbatoday.utils.decimalFormat
import com.jiachian.nbatoday.utils.toRank

@Composable
fun TeamInformation(
    viewModel: TeamViewModel,
    stats: Team,
) {
    val standing by viewModel.teamStanding.collectAsState()
    Column {
        TeamNameAndStanding(
            stats = stats,
            standing = standing,
            textColor = viewModel.colors.extra2,
        )
        TeamStatsDetail(
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                .height(IntrinsicSize.Min),
            viewModel = viewModel,
            stats = stats,
        )
    }
}

@Composable
private fun TeamNameAndStanding(
    modifier: Modifier = Modifier,
    stats: Team,
    standing: Int,
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
                modifier = Modifier.testTag(TeamTestTag.TeamNameAndStanding_Text_TeamName),
                text = stats.team.teamFullName,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = textColor
            )
            Text(
                modifier = Modifier.testTag(TeamTestTag.TeamNameAndStanding_Text_StandingDetail),
                text = stats.getStandingDetail(standing),
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
    stats: Team,
) {
    val pointsRank by viewModel.teamPointsRank.collectAsState()
    val reboundsRank by viewModel.teamReboundsRank.collectAsState()
    val assistsRank by viewModel.teamAssistsRank.collectAsState()
    val plusMinusRank by viewModel.teamPlusMinusRank.collectAsState()
    Row(modifier = modifier) {
        TeamRankBox(
            modifier = Modifier.testTag(TeamTestTag.TeamStatsDetail_TeamRankBox_Points),
            label = stringResource(R.string.team_rank_points_abbr),
            rank = pointsRank,
            average = stats.pointsAverage,
            textColor = viewModel.colors.extra2,
            divider = true,
        )
        TeamRankBox(
            modifier = Modifier.testTag(TeamTestTag.TeamStatsDetail_TeamRankBox_Rebounds),
            label = stringResource(R.string.team_rank_rebounds_abbr),
            rank = reboundsRank,
            average = stats.reboundsTotalAverage,
            textColor = viewModel.colors.extra2,
            divider = true,
        )
        TeamRankBox(
            modifier = Modifier.testTag(TeamTestTag.TeamStatsDetail_TeamRankBox_Assists),
            label = stringResource(R.string.team_rank_assists_abbr),
            rank = assistsRank,
            average = stats.assistsAverage,
            textColor = viewModel.colors.extra2,
            divider = true
        )
        TeamRankBox(
            modifier = Modifier.testTag(TeamTestTag.TeamStatsDetail_TeamRankBox_PlusMinus),
            label = stringResource(R.string.team_rank_plusMinus_abbr),
            rank = plusMinusRank,
            average = stats.plusMinus,
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
                modifier = Modifier.testTag(TeamTestTag.TeamRankBox_Text_Rank),
                text = rank.toRank(),
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = textColor
            )
            Text(
                modifier = Modifier.testTag(TeamTestTag.TeamRankBox_Text_Average),
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
                color = textColor.copy(Transparency25)
            )
        }
    }
}
