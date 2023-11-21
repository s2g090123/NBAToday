package com.jiachian.nbatoday.compose.screen.score.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.widget.TeamLogoImage
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.local.team.NBATeam

@Composable
fun ScoreTotal(
    modifier: Modifier = Modifier,
    score: GameBoxScore
) {
    val homeTeam = remember(score) { score.homeTeam }
    val awayTeam = remember(score) { score.awayTeam }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TeamInfo(
            modifier = Modifier
                .testTag("ScoreTotal_TeamInfo_Home")
                .padding(start = 24.dp),
            team = homeTeam.team
        )
        GameScoreStatus(
            homeScore = homeTeam.score,
            awayScore = awayTeam.score,
            gameStatus = score.statusText
        )
        TeamInfo(
            modifier = Modifier
                .testTag("ScoreTotal_TeamInfo_Away")
                .padding(end = 24.dp),
            team = awayTeam.team
        )
    }
}

@Composable
private fun TeamInfo(
    modifier: Modifier = Modifier,
    team: NBATeam
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TeamLogoImage(
            modifier = Modifier.size(100.dp),
            team = team
        )
        Text(
            modifier = Modifier
                .testTag("TeamInfo_Text_Name")
                .padding(top = 8.dp),
            text = team.teamName,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            color = MaterialTheme.colors.secondary
        )
    }
}

@Composable
private fun GameScoreStatus(
    modifier: Modifier = Modifier,
    homeScore: Int,
    awayScore: Int,
    gameStatus: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.testTag("ScoreTotal_Text_ScoreComparison"),
            text = stringResource(R.string.box_score_comparison, homeScore, awayScore),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = MaterialTheme.colors.secondary
        )
        Text(
            modifier = Modifier
                .testTag("ScoreTotal_Text_Status")
                .padding(top = 16.dp),
            text = gameStatus,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            fontSize = 24.sp,
            color = MaterialTheme.colors.secondary
        )
    }
}
