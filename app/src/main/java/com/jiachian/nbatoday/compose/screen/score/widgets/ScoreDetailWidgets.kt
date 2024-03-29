package com.jiachian.nbatoday.compose.screen.score.widgets

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.score.state.BoxScoreLeaderState
import com.jiachian.nbatoday.compose.screen.score.state.BoxScorePlayerState
import com.jiachian.nbatoday.compose.screen.score.state.BoxScoreTeamState
import com.jiachian.nbatoday.testing.testtag.BoxScoreTestTag
import kotlinx.coroutines.launch

private const val HomePageIndex = 0
private const val AwayPageIndex = 1
private const val TeamPageIndex = 2
private const val LeaderPageIndex = 3

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScoreDetailPager(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    player: BoxScorePlayerState,
    team: BoxScoreTeamState,
    leader: BoxScoreLeaderState,
    onClickPlayer: (playerId: Int) -> Unit,
) {
    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        count = 4,
        userScrollEnabled = false
    ) { index ->
        when (index) {
            HomePageIndex -> {
                ScorePlayerPage(
                    modifier = Modifier
                        .testTag(BoxScoreTestTag.ScoreDetailPager_ScorePlayerPage_Home)
                        .fillMaxSize(),
                    players = player.homePlayers,
                    onClickPlayer = onClickPlayer,
                )
            }
            AwayPageIndex -> {
                ScorePlayerPage(
                    modifier = Modifier
                        .testTag(BoxScoreTestTag.ScoreDetailPager_ScorePlayerPage_Away)
                        .fillMaxHeight(),
                    players = player.awayPlayers,
                    onClickPlayer = onClickPlayer,
                )
            }
            TeamPageIndex -> {
                ScoreTeamPage(
                    modifier = Modifier
                        .testTag(BoxScoreTestTag.ScoreDetailPager_ScoreTeamPage)
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp),
                    homeTeam = team.homeTeam,
                    awayTeam = team.awayTeam,
                    data = team.data,
                )
            }
            LeaderPageIndex -> {
                ScoreLeaderPage(
                    modifier = Modifier
                        .testTag(BoxScoreTestTag.ScoreDetailPager_ScoreLeaderPage)
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp),
                    homeLeaderId = leader.homePlayerId,
                    awayLeaderId = leader.awayPlayerId,
                    data = leader.data,
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScoreTabRow(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    homeAbbr: String,
    awayAbbr: String,
) {
    val coroutineScope = rememberCoroutineScope()
    TabRow(
        modifier = modifier,
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = MaterialTheme.colors.secondary,
        contentColor = MaterialTheme.colors.primaryVariant,
        indicator = @Composable { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                color = MaterialTheme.colors.primaryVariant
            )
        }
    ) {
        ScoreTab(
            modifier = Modifier.testTag(BoxScoreTestTag.ScoreTabRow_ScoreTab_Home),
            text = homeAbbr,
            selected = pagerState.currentPage == HomePageIndex,
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(HomePageIndex)
                }
            }
        )
        ScoreTab(
            modifier = Modifier.testTag(BoxScoreTestTag.ScoreTabRow_ScoreTab_Away),
            text = awayAbbr,
            selected = pagerState.currentPage == AwayPageIndex,
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(AwayPageIndex)
                }
            }
        )
        ScoreTab(
            modifier = Modifier.testTag(BoxScoreTestTag.ScoreTabRow_ScoreTab_Team),
            text = stringResource(R.string.box_score_tab_statistics),
            selected = pagerState.currentPage == TeamPageIndex,
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(TeamPageIndex)
                }
            }
        )
        ScoreTab(
            modifier = Modifier.testTag(BoxScoreTestTag.ScoreTabRow_ScoreTab_Leader),
            text = stringResource(R.string.box_score_tab_leaders),
            selected = pagerState.currentPage == LeaderPageIndex,
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(LeaderPageIndex)
                }
            }
        )
    }
}

@Composable
private fun ScoreTab(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Tab(
        modifier = modifier,
        text = {
            Text(
                text = text,
                color = MaterialTheme.colors.primary,
                fontSize = 14.sp
            )
        },
        selected = selected,
        onClick = onClick
    )
}
