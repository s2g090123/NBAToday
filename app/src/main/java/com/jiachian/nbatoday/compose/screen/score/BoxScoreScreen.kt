package com.jiachian.nbatoday.compose.screen.score

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.data.local.score.GameBoxScore
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
        val (backBtn, dateTitle) = createRefs()

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