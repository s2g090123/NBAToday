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
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreUI
import com.jiachian.nbatoday.compose.screen.score.widgets.ScoreDetailPager
import com.jiachian.nbatoday.compose.screen.score.widgets.ScorePeriod
import com.jiachian.nbatoday.compose.screen.score.widgets.ScoreTabRow
import com.jiachian.nbatoday.compose.screen.score.widgets.ScoreTotal
import com.jiachian.nbatoday.compose.widget.IconButton
import com.jiachian.nbatoday.compose.widget.LoadingScreen
import com.jiachian.nbatoday.compose.widget.UIStateScreen
import com.jiachian.nbatoday.testing.testtag.BoxScoreTestTag
import org.koin.androidx.compose.koinViewModel

private val TopMargin = 81.dp

@Composable
fun BoxScoreScreen(
    viewModel: BoxScoreViewModel = koinViewModel(),
    openPlayerInfo: (playerId: Int) -> Unit,
    onBack: () -> Unit,
) {
    val date by viewModel.date.collectAsState()
    val boxScoreUIState by viewModel.boxScoreUIState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
    ) {
        ScoreTopBar(
            title = date,
            onBack = onBack
        )
        UIStateScreen(
            state = boxScoreUIState,
            loading = {
                LoadingScreen(
                    modifier = Modifier
                        .testTag(BoxScoreTestTag.ScoreScreen_Loading)
                        .fillMaxSize(),
                    color = MaterialTheme.colors.secondary,
                )
            },
            ifNull = {
                NotFoundScreen(
                    modifier = Modifier
                        .testTag(BoxScoreTestTag.ScoreScreen_NotFoundScreen)
                        .fillMaxSize()
                )
            }
        ) { boxScoreUI ->
            val scrollState = rememberScrollState()
            ScoreScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                viewModel = viewModel,
                scrollState = scrollState,
                boxScoreUI = boxScoreUI,
                onClickPlayer = openPlayerInfo,
            )
        }
    }
}

@Composable
private fun ScoreScreen(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel,
    scrollState: ScrollState,
    boxScoreUI: BoxScoreUI,
    onClickPlayer: (playerId: Int) -> Unit,
) {
    val detailHeight = LocalConfiguration.current.screenHeightDp.dp - TopMargin
    Column(modifier = modifier) {
        ScoreTotal(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            boxScore = boxScoreUI.boxScore,
        )
        ScorePeriod(
            modifier = Modifier
                .padding(top = 16.dp, start = 12.dp, end = 12.dp)
                .fillMaxWidth(),
            score = boxScoreUI.boxScore,
            labels = boxScoreUI.periods,
        )
        ScoreDetail(
            modifier = Modifier
                .padding(top = 16.dp)
                .height(detailHeight),
            viewModel = viewModel,
            boxScoreUI = boxScoreUI,
            scrollState = scrollState,
            onClickPlayer = onClickPlayer,
        )
    }
}

@Composable
private fun ScoreTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onBack: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            modifier = Modifier
                .testTag(BoxScoreTestTag.ScoreTopBar_Button_Back)
                .padding(start = 8.dp, top = 8.dp),
            drawableRes = R.drawable.ic_black_back,
            tint = MaterialTheme.colors.secondary,
            onClick = onBack
        )
        Text(
            modifier = Modifier
                .testTag(BoxScoreTestTag.ScoreTopBar_Text_Date)
                .align(Alignment.CenterVertically)
                .padding(start = 16.dp, top = 8.dp),
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
    boxScoreUI: BoxScoreUI,
    scrollState: ScrollState,
    onClickPlayer: (playerId: Int) -> Unit,
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
            homeTeam = boxScoreUI.teams.home,
            awayTeam = boxScoreUI.teams.away,
        )
        ScoreDetailPager(
            modifier = Modifier
                .fillMaxHeight()
                .nestedScroll(nestedScrollConnection),
            viewModel = viewModel,
            pagerState = pagerState,
            boxScoreUI = boxScoreUI,
            onClickPlayer = onClickPlayer,
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
