package com.jiachian.nbatoday.boxscore.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.boxscore.data.model.local.BoxScore
import com.jiachian.nbatoday.testing.testtag.BoxScoreTestTag
import com.jiachian.nbatoday.utils.dividerSecondaryColor

@Composable
fun ScorePeriod(
    boxScore: BoxScore,
    labels: List<String>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Divider(
            color = dividerSecondaryColor(),
            thickness = 2.dp
        )
        PeriodLabelRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            labels = labels
        )
        PeriodScoreTable(
            modifier = Modifier.fillMaxWidth(),
            homeTeam = boxScore.homeTeam,
            awayTeam = boxScore.awayTeam
        )
        Divider(
            modifier = Modifier.padding(top = 8.dp),
            color = dividerSecondaryColor(),
            thickness = 2.dp
        )
    }
}

@Composable
private fun PeriodLabelRow(
    labels: List<String>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        labels.forEach {
            ScoreLabelText(label = it)
        }
        ScoreLabelText(label = stringResource(R.string.box_score_total_abbr))
    }
}

@Composable
private fun ScoreLabelText(label: String) {
    Text(
        modifier = Modifier.width(38.dp),
        text = label,
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
        color = MaterialTheme.colors.secondary
    )
}

@Composable
private fun PeriodScoreTable(
    homeTeam: BoxScore.BoxScoreTeam,
    awayTeam: BoxScore.BoxScoreTeam,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        PeriodScoreRow(
            modifier = Modifier
                .testTag(BoxScoreTestTag.PeriodScoreTable_PeriodScoreRow_Home)
                .fillMaxWidth(),
            team = homeTeam
        )
        PeriodScoreRow(
            modifier = Modifier
                .testTag(BoxScoreTestTag.PeriodScoreTable_PeriodScoreRow_Away)
                .fillMaxWidth(),
            team = awayTeam
        )
    }
}

@Composable
private fun PeriodScoreRow(
    team: BoxScore.BoxScoreTeam,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Text(
            modifier = Modifier
                .testTag(BoxScoreTestTag.PeriodScoreRow_Text_TeamName)
                .align(Alignment.TopStart),
            text = team.team.teamName,
            color = MaterialTheme.colors.secondary,
            fontSize = 16.sp
        )
        Row(modifier = Modifier.align(Alignment.TopEnd)) {
            team.periods.forEach { period ->
                ScoreText(
                    modifier = Modifier
                        .testTag(BoxScoreTestTag.PeriodScoreRow_Text_Score)
                        .width(38.dp),
                    score = period.score
                )
            }
            ScoreText(
                modifier = Modifier
                    .testTag(BoxScoreTestTag.PeriodScoreRow_Text_ScoreTotal)
                    .width(38.dp),
                score = team.score
            )
        }
    }
}

@Composable
private fun ScoreText(
    score: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = score.toString(),
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
        color = MaterialTheme.colors.secondary
    )
}
