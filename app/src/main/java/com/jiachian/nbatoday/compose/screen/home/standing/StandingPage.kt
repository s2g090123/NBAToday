package com.jiachian.nbatoday.compose.screen.home.standing

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.jiachian.nbatoday.Transparency25
import com.jiachian.nbatoday.compose.screen.home.standing.models.StandingLabel
import com.jiachian.nbatoday.compose.screen.home.standing.models.StandingRowData
import com.jiachian.nbatoday.compose.screen.home.standing.models.StandingRowData.Companion.ELIMINATED_STANDING
import com.jiachian.nbatoday.compose.screen.home.standing.models.StandingSorting
import com.jiachian.nbatoday.compose.widget.LoadingScreen
import com.jiachian.nbatoday.compose.widget.TeamLogoImage
import com.jiachian.nbatoday.compose.widget.UIStateScreen
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.testing.testtag.StandingTestTag
import com.jiachian.nbatoday.utils.dividerSecondaryColor
import com.jiachian.nbatoday.utils.modifyIf
import com.jiachian.nbatoday.utils.rippleClickable
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun StandingPage(
    modifier: Modifier = Modifier,
    viewModel: StandingPageViewModel = koinViewModel(),
    navigateToTeam: (teamId: Int) -> Unit,
) {
    val refreshing by viewModel.refreshing.collectAsState()
    val pagerState = rememberPagerState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = { viewModel.updateTeamStats() }
    )
    Box(modifier = modifier) {
        HorizontalPager(
            modifier = Modifier
                .testTag(StandingTestTag.StandingPage_Pager)
                .padding(top = 48.dp),
            state = pagerState,
            count = viewModel.conferences.size,
            userScrollEnabled = false
        ) { page ->
            Box(
                modifier = Modifier
                    .testTag(StandingTestTag.StandingPage_Content)
                    .pullRefresh(pullRefreshState)
            ) {
                StandingScreen(
                    modifier = Modifier.fillMaxHeight(),
                    viewModel = viewModel,
                    east = page == 0,
                    onClickTeam = navigateToTeam,
                )
                PullRefreshIndicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    refreshing = refreshing,
                    state = pullRefreshState
                )
            }
        }
        StandingTabRow(
            viewModel = viewModel,
            pagerState = pagerState,
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun StandingTabRow(
    viewModel: StandingPageViewModel,
    pagerState: PagerState,
) {
    val coroutineScope = rememberCoroutineScope()
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
        viewModel.conferences.forEachIndexed { index, conference ->
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
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                        viewModel.selectConference(conference)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun StandingScreen(
    modifier: Modifier = Modifier,
    viewModel: StandingPageViewModel,
    east: Boolean,
    onClickTeam: (teamId: Int) -> Unit,
) {
    val scrollState = rememberScrollState()
    val rowDataState by when (east) {
        true -> viewModel.sortedEastRowDataState.collectAsState()
        false -> viewModel.sortedWestRowDataState.collectAsState()
    }
    val sorting by when (east) {
        true -> viewModel.eastSorting.collectAsState()
        false -> viewModel.westSorting.collectAsState()
    }
    UIStateScreen(
        state = rowDataState,
        loading = {
            LoadingScreen(
                modifier = modifier,
                color = MaterialTheme.colors.secondary,
            )
        },
        ifNull = null
    ) { rowData ->
        LazyColumn(modifier = modifier) {
            stickyHeader {
                StandingLabelScrollableRow(
                    viewModel = viewModel,
                    sorting = sorting,
                    scrollState = scrollState
                )
            }
            itemsIndexed(rowData) { standing, rowData ->
                StandingStatsScrollableRow(
                    scrollState = scrollState,
                    standing = standing + 1,
                    rowData = rowData,
                    sorting = sorting,
                    onClickTeam = onClickTeam,
                )
            }
        }
    }
}

@Composable
private fun StandingLabelScrollableRow(
    modifier: Modifier = Modifier,
    viewModel: StandingPageViewModel,
    sorting: StandingSorting,
    scrollState: ScrollState,
) {
    Column(modifier = Modifier.background(MaterialTheme.colors.primary)) {
        StandingLabelRow(
            modifier = modifier,
            viewModel = viewModel,
            sorting = sorting,
            scrollState = scrollState,
        )
        Divider(
            color = dividerSecondaryColor(),
            thickness = 3.dp,
        )
    }
}

@Composable
private fun StandingLabelRow(
    modifier: Modifier = Modifier,
    viewModel: StandingPageViewModel,
    sorting: StandingSorting,
    scrollState: ScrollState,
) {
    Row(modifier = modifier) {
        Spacer(modifier = Modifier.width(135.dp))
        Row(modifier = Modifier.horizontalScroll(scrollState)) {
            viewModel.labels.forEach { label ->
                StandingLabel(
                    label = label,
                    focus = label.sorting == sorting,
                    onClick = { viewModel.updateSorting(label.sorting) }
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
    modifier: Modifier = Modifier,
    standing: Int,
    team: NBATeam
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
