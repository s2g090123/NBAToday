package com.jiachian.nbatoday.compose.screen.score.widgets

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
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.utils.dividerSecondaryColor

@Composable
fun ScorePeriod(
    modifier: Modifier = Modifier,
    score: BoxScore,
    labels: List<String>
) {
    Column(modifier = modifier) {
        Divider(
            modifier = Modifier.fillMaxWidth(),
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
            homeTeam = score.homeTeam,
            awayTeam = score.awayTeam
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            color = dividerSecondaryColor(),
            thickness = 2.dp
        )
    }
}

@Composable
private fun PeriodLabelRow(
    modifier: Modifier = Modifier,
    labels: List<String>
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        labels.forEach {
            ScoreLabelText(
                modifier = Modifier.width(38.dp),
                label = it
            )
        }
        ScoreLabelText(
            modifier = Modifier.width(38.dp),
            label = stringResource(R.string.box_score_total_abbr)
        )
    }
}

@Composable
private fun ScoreLabelText(
    modifier: Modifier = Modifier,
    label: String
) {
    Text(
        modifier = modifier,
        text = label,
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
        color = MaterialTheme.colors.secondary
    )
}

@Composable
private fun PeriodScoreTable(
    modifier: Modifier = Modifier,
    homeTeam: BoxScore.BoxScoreTeam,
    awayTeam: BoxScore.BoxScoreTeam
) {
    Column(modifier = modifier) {
        PeriodScoreRow(
            modifier = Modifier
                .testTag("ScorePeriod_Box_Score")
                .fillMaxWidth(),
            team = homeTeam
        )
        PeriodScoreRow(
            modifier = Modifier
                .testTag("ScorePeriod_Box_Score")
                .fillMaxWidth(),
            team = awayTeam
        )
    }
}

@Composable
private fun PeriodScoreRow(
    modifier: Modifier = Modifier,
    team: BoxScore.BoxScoreTeam
) {
    Box(modifier = modifier) {
        Text(
            modifier = Modifier
                .testTag("ScorePeriod_Text_TeamName")
                .align(Alignment.TopStart),
            text = team.team.teamName,
            color = MaterialTheme.colors.secondary,
            fontSize = 16.sp
        )
        Row(modifier = Modifier.align(Alignment.TopEnd)) {
            team.periods.forEach { period ->
                ScoreText(
                    modifier = Modifier
                        .testTag("ScorePeriod_Text_Score")
                        .width(38.dp),
                    score = period.score
                )
            }
            ScoreText(
                modifier = Modifier
                    .testTag("ScorePeriod_Text_ScoreTotal")
                    .width(38.dp),
                score = team.score
            )
        }
    }
}

@Composable
private fun ScoreText(
    modifier: Modifier = Modifier,
    score: Int
) {
    Text(
        modifier = modifier,
        text = score.toString(),
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
        color = MaterialTheme.colors.secondary
    )
}
