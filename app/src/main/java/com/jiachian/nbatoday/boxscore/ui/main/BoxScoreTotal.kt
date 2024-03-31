package com.jiachian.nbatoday.boxscore.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.boxscore.data.model.local.BoxScore
import com.jiachian.nbatoday.common.ui.TeamLogoImage
import com.jiachian.nbatoday.team.data.model.local.teams.NBATeam
import com.jiachian.nbatoday.testing.testtag.BoxScoreTestTag

@Composable
fun ScoreTotal(
    modifier: Modifier = Modifier,
    boxScore: BoxScore,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TeamInfo(
            modifier = Modifier
                .testTag(BoxScoreTestTag.ScoreTotal_TeamInfo_Home)
                .padding(start = 24.dp),
            team = boxScore.homeTeam.team,
        )
        GameScoreStatus(
            scoreComparison = boxScore.scoreComparison,
            gameStatus = boxScore.statusText,
        )
        TeamInfo(
            modifier = Modifier
                .testTag(BoxScoreTestTag.ScoreTotal_TeamInfo_Away)
                .padding(end = 24.dp),
            team = boxScore.awayTeam.team
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
                .testTag(BoxScoreTestTag.TeamInfo_Text_TeamName)
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
    scoreComparison: String,
    gameStatus: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.testTag(BoxScoreTestTag.GameScoreStatus_Text_ScoreComparison),
            text = scoreComparison,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = MaterialTheme.colors.secondary
        )
        Text(
            modifier = Modifier
                .testTag(BoxScoreTestTag.GameScoreStatus_Text_Status)
                .padding(top = 16.dp),
            text = gameStatus,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            fontSize = 24.sp,
            color = MaterialTheme.colors.secondary
        )
    }
}
