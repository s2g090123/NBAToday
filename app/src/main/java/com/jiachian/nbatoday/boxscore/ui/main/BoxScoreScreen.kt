package com.jiachian.nbatoday.boxscore.ui.main

import androidx.compose.foundation.ScrollState
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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.boxscore.ui.leader.state.BoxScoreLeaderState
import com.jiachian.nbatoday.boxscore.ui.main.event.BoxScoreDataEvent
import com.jiachian.nbatoday.boxscore.ui.main.event.BoxScoreUIEvent
import com.jiachian.nbatoday.boxscore.ui.main.state.BoxScoreInfoState
import com.jiachian.nbatoday.boxscore.ui.player.state.BoxScorePlayerState
import com.jiachian.nbatoday.boxscore.ui.team.state.BoxScoreTeamState
import com.jiachian.nbatoday.common.ui.IconButton
import com.jiachian.nbatoday.main.ui.navigation.NavigationController
import com.jiachian.nbatoday.testing.testtag.BoxScoreTestTag
import com.jiachian.nbatoday.utils.showToast
import org.koin.androidx.compose.koinViewModel

private val TopMargin = 81.dp

@Composable
fun BoxScoreScreen(
    viewModel: BoxScoreViewModel = koinViewModel(),
    navigationController: NavigationController,
) {
    val context = LocalContext.current
    val state = viewModel.state
    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
        topBar = {
            ScoreTopBar(
                title = state.info.dateString,
                onBack = navigationController::back
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (state.loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .testTag(BoxScoreTestTag.ScoreScreen_Loading)
                        .align(Alignment.Center),
                    color = MaterialTheme.colors.secondary,
                )
            } else if (state.notFound) {
                Text(
                    modifier = Modifier
                        .testTag(BoxScoreTestTag.ScoreScreen_NotFound)
                        .align(Alignment.Center),
                    text = stringResource(R.string.box_score_empty),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            } else {
                ScoreScreen(
                    info = state.info,
                    player = state.player,
                    team = state.team,
                    leader = state.leader,
                    onClickPlayer = { navigationController.navigateToPlayer(it) }
                )
            }
        }
    }
    LaunchedEffect(state.event) {
        state.event?.let { event ->
            when (event) {
                is BoxScoreDataEvent.Error -> showToast(context, event.error.message)
            }
            viewModel.onEvent(BoxScoreUIEvent.EventReceived)
        }
    }
}

@Composable
private fun ScoreScreen(
    info: BoxScoreInfoState,
    player: BoxScorePlayerState,
    team: BoxScoreTeamState,
    leader: BoxScoreLeaderState,
    onClickPlayer: (playerId: Int) -> Unit,
) {
    val scrollState = rememberScrollState()
    val detailHeight = LocalConfiguration.current.screenHeightDp.dp - TopMargin
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        ScoreInfo(info = info)
        ScoreDetail(
            modifier = Modifier
                .padding(top = 16.dp)
                .height(detailHeight),
            scrollState = scrollState,
            player = player,
            team = team,
            leader = leader,
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
    scrollState: ScrollState,
    player: BoxScorePlayerState,
    team: BoxScoreTeamState,
    leader: BoxScoreLeaderState,
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
            homeAbbr = team.homeTeam.abbreviation,
            awayAbbr = team.awayTeam.abbreviation
        )
        ScoreDetailPager(
            modifier = Modifier
                .fillMaxHeight()
                .nestedScroll(nestedScrollConnection),
            pagerState = pagerState,
            player = player,
            team = team,
            leader = leader,
            onClickPlayer = onClickPlayer,
        )
    }
}
