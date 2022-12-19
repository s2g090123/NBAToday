package com.jiachian.nbatoday.compose.screen.score

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.utils.NbaUtils
import com.jiachian.nbatoday.utils.dividerSecondary
import com.jiachian.nbatoday.utils.noRippleClickable

@Composable
fun BoxScoreScreen(
    viewModel: BoxScoreViewModel,
    onBack: () -> Unit
) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val score by viewModel.boxScore.collectAsState()

    when {
        isRefreshing -> {
            RefreshScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.primary)
                    .noRippleClickable { },
                onBack = onBack
            )
        }
        score == null -> {
            EmptyScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.primary)
                    .noRippleClickable { },
                onBack = onBack
            )
        }
        else -> {
            score?.let {
                ScoreScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.primary)
                        .noRippleClickable { },
                    score = it,
                    viewModel = viewModel,
                    onBack = onBack
                )
            }
        }
    }
}

@Composable
private fun ScoreScreen(
    modifier: Modifier = Modifier,
    score: GameBoxScore,
    viewModel: BoxScoreViewModel,
    onBack: () -> Unit
) {
    ConstraintLayout(modifier = modifier) {
        val (backBtn, dateTitle, scoreTotal, scorePeriod) = createRefs()

        IconButton(
            modifier = Modifier
                .constrainAs(backBtn) {
                    top.linkTo(parent.top, 8.dp)
                    start.linkTo(parent.start, 8.dp)
                },
            onClick = onBack
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_black_back),
                contentDescription = null,
                tint = MaterialTheme.colors.secondary
            )
        }
        Text(
            modifier = Modifier
                .constrainAs(dateTitle) {
                    linkTo(backBtn.top, backBtn.bottom)
                    start.linkTo(backBtn.end, 16.dp)
                },
            text = score.gameDate,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
        ScoreTotal(
            modifier = Modifier
                .constrainAs(scoreTotal) {
                    top.linkTo(backBtn.bottom, 16.dp)
                }
                .fillMaxWidth()
                .wrapContentHeight(),
            score = score
        )
        ScorePeriod(
            modifier = Modifier
                .constrainAs(scorePeriod) {
                    top.linkTo(scoreTotal.bottom, 16.dp)
                }
                .padding(horizontal = 12.dp)
                .fillMaxWidth(),
            score = score
        )
    }
}

@Composable
private fun ScoreTotal(
    modifier: Modifier = Modifier,
    score: GameBoxScore
) {
    ConstraintLayout(modifier = modifier) {
        val (
            homeImage, homeNameText, scoreText,
            awayImage, awyNameText
        ) = createRefs()

        Text(
            modifier = Modifier
                .constrainAs(scoreText) {
                    linkTo(homeImage.top, homeImage.bottom)
                    linkTo(parent.start, parent.end)
                },
            text = "${score.homeTeam?.score} - ${score.awayTeam?.score}",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = MaterialTheme.colors.secondary
        )
        AsyncImage(
            modifier = Modifier
                .constrainAs(homeImage) {
                    top.linkTo(parent.top)
                    end.linkTo(scoreText.start, 24.dp)
                }
                .size(100.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(NbaUtils.getTeamLogoUrlById(score.homeTeam?.teamId ?: 0))
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            error = painterResource(NbaUtils.getTeamLogoResById(score.homeTeam?.teamId ?: 0)),
            placeholder = painterResource(NbaUtils.getTeamLogoResById(score.homeTeam?.teamId ?: 0)),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .constrainAs(homeNameText) {
                    top.linkTo(homeImage.bottom, 8.dp)
                    linkTo(homeImage.start, homeImage.end)
                },
            text = score.homeTeam?.teamName ?: "",
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            color = MaterialTheme.colors.secondary
        )
        AsyncImage(
            modifier = Modifier
                .constrainAs(awayImage) {
                    top.linkTo(parent.top)
                    start.linkTo(scoreText.end, 24.dp)
                }
                .size(100.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(NbaUtils.getTeamLogoUrlById(score.awayTeam?.teamId ?: 0))
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            error = painterResource(NbaUtils.getTeamLogoResById(score.awayTeam?.teamId ?: 0)),
            placeholder = painterResource(NbaUtils.getTeamLogoResById(score.awayTeam?.teamId ?: 0)),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .constrainAs(awyNameText) {
                    top.linkTo(awayImage.bottom, 8.dp)
                    linkTo(awayImage.start, awayImage.end)
                },
            text = score.awayTeam?.teamName ?: "",
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            color = MaterialTheme.colors.secondary
        )
    }
}

@Composable
private fun ScorePeriod(
    modifier: Modifier = Modifier,
    score: GameBoxScore
) {
    Column(modifier = modifier) {
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.dividerSecondary(),
            thickness = 2.dp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            score.homeTeam?.periods?.forEach {
                Text(
                    modifier = Modifier.width(38.dp),
                    text = it.periodLabel,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.secondary
                )
            }
            Text(
                modifier = Modifier.width(38.dp),
                text = stringResource(R.string.box_score_total_abbr),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = MaterialTheme.colors.secondary
            )
        }
        arrayOf(score.homeTeam, score.awayTeam).forEach { team ->
            if (team != null) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.align(Alignment.TopStart),
                        text = team.teamName,
                        color = MaterialTheme.colors.secondary,
                        fontSize = 16.sp
                    )
                    Row(modifier = Modifier.align(Alignment.TopEnd)) {
                        team.periods.forEach { period ->
                            Text(
                                modifier = Modifier.width(38.dp),
                                text = period.score.toString(),
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp,
                                color = MaterialTheme.colors.secondary
                            )
                        }
                        Text(
                            modifier = Modifier.width(38.dp),
                            text = team.score.toString(),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.secondary
                        )
                    }
                }
            }
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            color = MaterialTheme.colors.dividerSecondary(),
            thickness = 2.dp
        )
    }
}

@Composable
private fun RefreshScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    Box(modifier = modifier) {
        IconButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 8.dp, start = 8.dp),
            onClick = onBack,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_black_back),
                contentDescription = null,
                tint = MaterialTheme.colors.secondary
            )
        }
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = MaterialTheme.colors.secondary
        )
    }
}

@Composable
private fun EmptyScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    Box(modifier = modifier) {
        IconButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 8.dp, start = 8.dp),
            onClick = onBack,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_black_back),
                contentDescription = null,
                tint = MaterialTheme.colors.secondary
            )
        }
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(R.string.box_score_empty),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
    }
}