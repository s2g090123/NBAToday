package com.jiachian.nbatoday.team.ui.main

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.data.Transparency25
import com.jiachian.nbatoday.common.ui.TeamLogoImage
import com.jiachian.nbatoday.team.data.model.local.Team
import com.jiachian.nbatoday.team.data.model.local.TeamRank
import com.jiachian.nbatoday.testing.testtag.TeamTestTag
import com.jiachian.nbatoday.utils.LocalColors
import com.jiachian.nbatoday.utils.toRank

@Composable
fun TeamInformation(
    team: Team,
    rank: TeamRank,
) {
    val colors = LocalColors.current
    Column {
        TeamNameAndStanding(
            stats = team,
            standing = rank.standing,
            color = colors.extra2,
        )
        TeamStatsDetail(
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                .height(IntrinsicSize.Min),
            team = team,
            rank = rank,
            color = colors.extra2,
        )
    }
}

@Composable
private fun TeamNameAndStanding(
    stats: Team,
    standing: Int,
    color: Color,
    modifier: Modifier = Modifier,
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
                color = color
            )
            Text(
                modifier = Modifier.testTag(TeamTestTag.TeamNameAndStanding_Text_StandingDetail),
                text = stats.getStandingDetail(standing),
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = color
            )
        }
    }
}

@Composable
private fun TeamStatsDetail(
    team: Team,
    rank: TeamRank,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        TeamRankBox(
            modifier = Modifier.testTag(TeamTestTag.TeamStatsDetail_TeamRankBox_Points),
            label = stringResource(R.string.team_rank_points_abbr),
            rank = rank.pointsRank,
            average = team.pointsAverage,
            color = color,
            divider = true,
        )
        TeamRankBox(
            modifier = Modifier.testTag(TeamTestTag.TeamStatsDetail_TeamRankBox_Rebounds),
            label = stringResource(R.string.team_rank_rebounds_abbr),
            rank = rank.reboundsRank,
            average = team.reboundsTotalAverage,
            color = color,
            divider = true,
        )
        TeamRankBox(
            modifier = Modifier.testTag(TeamTestTag.TeamStatsDetail_TeamRankBox_Assists),
            label = stringResource(R.string.team_rank_assists_abbr),
            rank = rank.assistsRank,
            average = team.assistsAverage,
            color = color,
            divider = true
        )
        TeamRankBox(
            modifier = Modifier.testTag(TeamTestTag.TeamStatsDetail_TeamRankBox_PlusMinus),
            label = stringResource(R.string.team_rank_plusMinus_abbr),
            rank = rank.plusMinusRank,
            average = team.plusMinus,
            color = color,
            divider = false
        )
    }
}

@Composable
private fun TeamRankBox(
    label: String,
    rank: Int,
    average: Double,
    color: Color,
    divider: Boolean,
    modifier: Modifier = Modifier,
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
                color = color
            )
            Text(
                modifier = Modifier.testTag(TeamTestTag.TeamRankBox_Text_Rank),
                text = rank.toRank(),
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = color
            )
            Text(
                modifier = Modifier.testTag(TeamTestTag.TeamRankBox_Text_Average),
                text = average.toString(),
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = color
            )
        }
        if (divider) {
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                color = color.copy(Transparency25)
            )
        }
    }
}
