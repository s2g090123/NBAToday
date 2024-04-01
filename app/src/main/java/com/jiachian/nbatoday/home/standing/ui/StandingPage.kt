package com.jiachian.nbatoday.home.standing.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.jiachian.nbatoday.common.data.Transparency25
import com.jiachian.nbatoday.common.ui.TeamLogoImage
import com.jiachian.nbatoday.home.standing.ui.event.StandingDataEvent
import com.jiachian.nbatoday.home.standing.ui.event.StandingUIEvent
import com.jiachian.nbatoday.home.standing.ui.model.StandingLabel
import com.jiachian.nbatoday.home.standing.ui.model.StandingRowData
import com.jiachian.nbatoday.home.standing.ui.model.StandingRowData.Companion.ELIMINATED_STANDING
import com.jiachian.nbatoday.home.standing.ui.model.StandingSorting
import com.jiachian.nbatoday.home.standing.ui.state.StandingTeamState
import com.jiachian.nbatoday.main.ui.navigation.NavigationController
import com.jiachian.nbatoday.team.data.model.local.teams.NBATeam
import com.jiachian.nbatoday.testing.testtag.StandingTestTag
import com.jiachian.nbatoday.utils.dividerSecondaryColor
import com.jiachian.nbatoday.utils.modifyIf
import com.jiachian.nbatoday.utils.rippleClickable
import com.jiachian.nbatoday.utils.showToast
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun StandingPage(
    navigationController: NavigationController,
    viewModel: StandingPageViewModel = koinViewModel(),
) {
    val state = viewModel.state
    val conferences = remember { NBATeam.Conference.values() }
    val context = LocalContext.current
    val pagerState = rememberPagerState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.refreshing,
        onRefresh = { viewModel.onEvent(StandingUIEvent.Refresh) }
    )
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            modifier = Modifier
                .testTag(StandingTestTag.StandingPage_Pager)
                .padding(top = 48.dp),
            state = pagerState,
            count = conferences.size,
            userScrollEnabled = false
        ) { page ->
            StandingScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState),
                state = if (page == 0) state.eastTeamState else state.westTeamState,
                onClickTeam = navigationController::navigateToTeam,
                onLabelClick = { viewModel.onEvent(StandingUIEvent.UpdateSorting(it.sorting)) }
            )
        }
        PullRefreshIndicator(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp),
            refreshing = state.refreshing,
            state = pullRefreshState
        )
        StandingTabRow(
            conferences = conferences,
            pagerState = pagerState,
            onTabClick = { viewModel.onEvent(StandingUIEvent.SelectConference(it)) }
        )
    }
    LaunchedEffect(state.event) {
        state.event?.let { event ->
            when (event) {
                is StandingDataEvent.Error -> showToast(context, event.error.message)
                is StandingDataEvent.ScrollTo -> pagerState.animateScrollToPage(event.page)
            }
            viewModel.onEvent(StandingUIEvent.EventReceived)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun StandingTabRow(
    pagerState: PagerState,
    conferences: Array<NBATeam.Conference>,
    onTabClick: (NBATeam.Conference) -> Unit,
) {
    TabRow(
        modifier = Modifier.testTag(StandingTestTag.StandingTabRow_TabRow),
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
        conferences.forEachIndexed { index, conference ->
            Tab(
                modifier = Modifier.testTag(StandingTestTag.StandingTabRow_Tab),
                text = {
                    Text(
                        text = stringResource(conference.textRes),
                        color = MaterialTheme.colors.primary,
                        fontSize = 14.sp
                    )
                },
                selected = index == pagerState.currentPage,
                onClick = { onTabClick(conference) }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun StandingScreen(
    state: StandingTeamState,
    onClickTeam: (teamId: Int) -> Unit,
    onLabelClick: (StandingLabel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    Box(modifier = modifier) {
        if (state.loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colors.secondary,
            )
        } else {
            LazyColumn(modifier = modifier) {
                stickyHeader {
                    StandingLabelScrollableRow(
                        scrollState = scrollState,
                        sorting = state.sorting,
                        onLabelClick = onLabelClick,
                    )
                }
                itemsIndexed(state.teams) { standing, rowData ->
                    StandingStatsScrollableRow(
                        scrollState = scrollState,
                        standing = standing + 1,
                        rowData = rowData,
                        sorting = state.sorting,
                        onClickTeam = onClickTeam,
                    )
                }
            }
        }
    }
}

@Composable
private fun StandingLabelScrollableRow(
    sorting: StandingSorting,
    scrollState: ScrollState,
    onLabelClick: (StandingLabel) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
    ) {
        StandingLabelRow(
            sorting = sorting,
            scrollState = scrollState,
            onClickItem = onLabelClick,
        )
        Divider(
            color = dividerSecondaryColor(),
            thickness = 3.dp,
        )
    }
}

@Composable
private fun StandingLabelRow(
    scrollState: ScrollState,
    sorting: StandingSorting,
    onClickItem: (StandingLabel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val labels = remember { StandingLabel.values() }
    Row(modifier = modifier) {
        Spacer(modifier = Modifier.width(135.dp))
        Row(modifier = Modifier.horizontalScroll(scrollState)) {
            labels.forEach { label ->
                StandingLabel(
                    label = label,
                    focus = label.sorting == sorting,
                    onClick = { onClickItem(label) }
                )
            }
        }
    }
}

@Composable
private fun StandingLabel(
    label: StandingLabel,
    focus: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .testTag(StandingTestTag.StandingLabel)
            .size(label.width, 36.dp)
            .modifyIf(focus, MaterialTheme.colors.secondary.copy(Transparency25)) { background(it) }
            .rippleClickable { onClick() }
            .padding(8.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxSize(),
            text = stringResource(label.textRes),
            textAlign = if (focus) TextAlign.Center else label.align,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary,
        )
    }
}

@Composable
private fun StandingStatsScrollableRow(
    scrollState: ScrollState,
    standing: Int,
    rowData: StandingRowData,
    sorting: StandingSorting,
    onClickTeam: (teamId: Int) -> Unit,
) {
    StandingStatsRow(
        scrollState = scrollState,
        standing = standing,
        rowData = rowData,
        sorting = sorting,
        onClickTeam = onClickTeam,
    )
    Divider(
        color = dividerSecondaryColor(),
        thickness = if (standing == ELIMINATED_STANDING) 3.dp else 1.dp
    )
}

@Composable
private fun StandingStatsRow(
    scrollState: ScrollState,
    standing: Int,
    rowData: StandingRowData,
    sorting: StandingSorting,
    onClickTeam: (teamId: Int) -> Unit,
) {
    Row(modifier = Modifier.testTag(StandingTestTag.StandingStatsRow)) {
        StandingTeamRow(
            modifier = Modifier
                .size(135.dp, 40.dp)
                .rippleClickable { onClickTeam(rowData.team.teamId) },
            standing = standing,
            team = rowData.team.team
        )
        Row(modifier = Modifier.horizontalScroll(scrollState)) {
            rowData.data.forEach { data ->
                StandingStatsText(
                    data = data,
                    focus = data.sorting == sorting,
                )
            }
        }
    }
}

@Composable
private fun StandingTeamRow(
    standing: Int,
    team: NBATeam,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .testTag(StandingTestTag.StandingTeamRow_Text_Standing)
                .padding(start = 4.dp)
                .width(24.dp),
            text = "$standing.",
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            color = MaterialTheme.colors.secondary,
            maxLines = 1
        )
        TeamLogoImage(
            modifier = Modifier.size(24.dp),
            team = team
        )
        Text(
            modifier = Modifier
                .testTag(StandingTestTag.StandingTeamRow_Text_TeamName)
                .padding(start = 4.dp),
            text = team.teamName,
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            color = MaterialTheme.colors.secondary,
            maxLines = 1
        )
    }
}

@Composable
private fun StandingStatsText(
    data: StandingRowData.Data,
    focus: Boolean,
) {
    Text(
        modifier = Modifier
            .testTag(StandingTestTag.StandingStatsText)
            .size(data.width, 40.dp)
            .modifyIf(focus, MaterialTheme.colors.secondary.copy(Transparency25)) { background(it) }
            .padding(8.dp),
        text = data.value,
        textAlign = if (focus) TextAlign.Center else data.align,
        fontSize = 16.sp,
        color = MaterialTheme.colors.secondary,
    )
}
