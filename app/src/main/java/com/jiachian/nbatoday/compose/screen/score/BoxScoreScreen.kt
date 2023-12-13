package com.jiachian.nbatoday.compose.screen.score

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.score.widgets.ScoreDetailPager
import com.jiachian.nbatoday.compose.screen.score.widgets.ScorePeriod
import com.jiachian.nbatoday.compose.screen.score.widgets.ScoreTabRow
import com.jiachian.nbatoday.compose.screen.score.widgets.ScoreTotal
import com.jiachian.nbatoday.compose.widget.IconButton
import com.jiachian.nbatoday.compose.widget.LoadingScreen
import com.jiachian.nbatoday.compose.widget.NullCheckScreen
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.testing.testtag.BoxScoreTestTag

private val TopMargin = 56.dp

@Composable
fun BoxScoreScreen(viewModel: BoxScoreViewModel) {
    val isLoading by viewModel.isLoading.collectAsState()
    val date by viewModel.date.collectAsState()
    val notFound by viewModel.notFound.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
    ) {
        ScoreTopBar(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            title = date,
            onBack = viewModel::close
        )
        when {
            isLoading -> {
                LoadingScreen(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.secondary,
                )
            }
            notFound -> NotFoundScreen(modifier = Modifier.fillMaxSize())
            else -> {
                val scrollState = rememberScrollState()
                ScoreScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                    viewModel = viewModel,
                    scrollState = scrollState,
                )
            }
        }
    }
}

@Composable
private fun ScoreScreen(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel,
    scrollState: ScrollState,
) {
    val boxScore by viewModel.boxScore.collectAsState()
    val labels by viewModel.periodLabels.collectAsState()
    val detailHeight = LocalConfiguration.current.screenHeightDp.dp - TopMargin
    NullCheckScreen(
        data = boxScore,
        ifNull = { NotFoundScreen(modifier = modifier) },
        ifNotNull = { score ->
            Column(modifier = modifier) {
                ScoreTotal(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    score = score
                )
                ScorePeriod(
                    modifier = Modifier
                        .testTag(BoxScoreTestTag.ScoreScreen_ScorePeriod)
                        .padding(top = 16.dp, start = 12.dp, end = 12.dp)
                        .fillMaxWidth(),
                    score = score,
                    labels = labels
                )
                ScoreDetail(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .height(detailHeight),
                    viewModel = viewModel,
                    score = score,
                    scrollState = scrollState,
                )
            }
        }
    )
}

@Composable
private fun ScoreTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onBack: () -> Unit
) {
    Row(modifier = modifier) {
        IconButton(
            modifier = Modifier
                .testTag(BoxScoreTestTag.ScoreTopBar_Button_Back)
                .padding(start = 8.dp),
            drawableRes = R.drawable.ic_black_back,
            tint = MaterialTheme.colors.secondary,
            onClick = onBack
        )
        Text(
            modifier = Modifier
                .testTag(BoxScoreTestTag.ScoreTopBar_Text_Date)
                .align(Alignment.CenterVertically)
                .padding(start = 16.dp),
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ScoreDetail(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel,
    score: BoxScore,
    scrollState: ScrollState,
) {
    val pagerState = rememberPagerState()
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                return when {
                    available.y > 0 -> Offset.Zero
                    else -> Offset(x = 0f, y = -scrollState.dispatchRawDelta(-available.y))
                }
            }
        }
    }
    Column(modifier = modifier) {
        ScoreTabRow(
            modifier = Modifier.fillMaxWidth(),
            pagerState = pagerState,
            score = score
        )
        ScoreDetailPager(
            modifier = Modifier
                .testTag(BoxScoreTestTag.ScoreDetail_ScoreDetailPager)
                .fillMaxHeight()
                .nestedScroll(nestedScrollConnection),
            viewModel = viewModel,
            pagerState = pagerState,
            count = 4,
        )
    }
}

@Composable
private fun NotFoundScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(R.string.box_score_empty),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
    }
}
