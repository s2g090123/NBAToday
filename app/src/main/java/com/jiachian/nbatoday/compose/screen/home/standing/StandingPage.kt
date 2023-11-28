package com.jiachian.nbatoday.compose.screen.home.standing

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.Transparency25
import com.jiachian.nbatoday.compose.widget.DisableOverscroll
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.TeamStats
import com.jiachian.nbatoday.utils.NbaUtils
import com.jiachian.nbatoday.utils.dividerSecondaryColor
import com.jiachian.nbatoday.utils.px2Dp
import com.jiachian.nbatoday.utils.rippleClickable
import kotlin.math.max

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun StandingPage(
    modifier: Modifier = Modifier,
    viewModel: StandingPageViewModel,
) {
    val teamStats by viewModel.teamStats.collectAsState()
    val selectedConference by viewModel.selectedConference.collectAsState()
    val isRefreshing by viewModel.isRefreshingTeamStats.collectAsState()
    val pagerState = rememberPagerState()
    val conferences = remember { NBATeam.Conference.values() }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.updateTeamStats() }
    )
    val selectIndex by remember {
        derivedStateOf { conferences.indexOf(selectedConference) }
    }
    Box(
        modifier = modifier
    ) {
        HorizontalPager(
            modifier = Modifier
                .testTag("StandingPage_Pager")
                .padding(top = 48.dp)
                .fillMaxSize(),
            state = pagerState,
            count = conferences.size,
            userScrollEnabled = false
        ) { page ->
            val stats = teamStats[conferences[page]]
            if (stats != null) {
                TeamStanding(
                    modifier = Modifier
                        .testTag("StandingPage_TeamStanding_Root")
                        .fillMaxSize()
                        .pullRefresh(pullRefreshState),
                    viewModel = viewModel,
                    teamStats = stats
                )
            }
        }
        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = isRefreshing,
            state = pullRefreshState
        )
        StandingTabRow(
            selectPage = pagerState.currentPage,
            conferences = conferences,
            selectedConference = selectedConference,
            selectConference = viewModel::selectConference
        )
    }
    LaunchedEffect(selectIndex) {
        pagerState.animateScrollToPage(selectIndex)
    }
}

@Composable
private fun StandingTabRow(
    selectPage: Int,
    conferences: Array<NBATeam.Conference>,
    selectedConference: NBATeam.Conference,
    selectConference: (NBATeam.Conference) -> Unit
) {
    TabRow(
        modifier = Modifier.testTag("StandingPage_TabRow_Conference"),
        selectedTabIndex = selectPage,
        backgroundColor = MaterialTheme.colors.secondary,
        contentColor = MaterialTheme.colors.primaryVariant,
        indicator = @Composable { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectPage]),
                color = MaterialTheme.colors.primaryVariant
            )
        }
    ) {
        conferences.forEach {
            Tab(
                modifier = Modifier.testTag("StandingPage_Tab_Conference"),
                text = {
                    Text(
                        text = stringResource(
                            when (it) {
                                NBATeam.Conference.EAST -> R.string.standing_conference_east
                                NBATeam.Conference.WEST -> R.string.standing_conference_west
                            }
                        ),
                        color = MaterialTheme.colors.primary,
                        fontSize = 14.sp
                    )
                },
                selected = selectedConference == it,
                onClick = { selectConference(it) }
            )
        }
    }
}

@Composable
private fun TeamStanding(
    modifier: Modifier = Modifier,
    viewModel: StandingPageViewModel,
    teamStats: List<TeamStats>
) {
    val teamState = rememberLazyListState()
    val statsState = rememberLazyListState()
    val horizontalScrollState = rememberScrollState()
    val stateTeamOffset by remember { derivedStateOf { teamState.firstVisibleItemScrollOffset } }
    val stateTeamIndex by remember { derivedStateOf { teamState.firstVisibleItemIndex } }
    val stateStatsOffset by remember { derivedStateOf { statsState.firstVisibleItemScrollOffset } }
    val stateStatsIndex by remember { derivedStateOf { statsState.firstVisibleItemIndex } }
    Row(modifier = modifier) {
        TeamStandingNameTable(
            teamState = teamState,
            teamStats = teamStats,
            onClickTeam = viewModel::openTeamStats
        )
        TeamStandingStatsTable(
            modifier = Modifier
                .testTag("TeamStanding_Column_StatsRoot")
                .fillMaxSize()
                .horizontalScroll(horizontalScrollState),
            viewModel = viewModel,
            statsState = statsState,
            teamStats = teamStats,
        )
    }
    LaunchedEffect(stateTeamOffset, stateTeamIndex) {
        statsState.scrollToItem(stateTeamIndex, stateTeamOffset)
    }
    LaunchedEffect(stateStatsOffset, stateStatsIndex) {
        teamState.scrollToItem(stateStatsIndex, stateStatsOffset)
    }
}

@Composable
private fun TeamStandingStatsTable(
    modifier: Modifier = Modifier,
    viewModel: StandingPageViewModel,
    statsState: LazyListState,
    teamStats: List<TeamStats>,
) {
    var dividerWidth by remember { mutableStateOf(0) }
    Column(modifier = modifier) {
        TeamStatsTabRow(
            modifier = Modifier
                .testTag("TeamStanding_Root_Label")
                .onSizeChanged {
                    dividerWidth = max(dividerWidth, it.width)
                }
                .fillMaxWidth()
                .wrapContentHeight(),
            viewModel = viewModel,
        )
        Divider(
            modifier = Modifier.width(dividerWidth.px2Dp()),
            color = dividerSecondaryColor(),
            thickness = 3.dp
        )
        DisableOverscroll {
            LazyColumn(
                modifier = Modifier
                    .testTag("TeamStanding_LC_Stats")
                    .fillMaxHeight()
                    .fillMaxWidth(),
                state = statsState
            ) {
                itemsIndexed(teamStats) { index, stats ->
                    TeamStatsRow(
                        modifier = Modifier.testTag("TeamStandingStatsTable_TeamStatsRow"),
                        viewModel = viewModel,
                        stats = stats,
                        dividerVisible = index < teamStats.size - 1,
                        dividerWidth = dividerWidth.px2Dp(),
                        dividerThickness = if (index + 1 == 10) 3.dp else 1.dp,
                    )
                }
            }
        }
    }
}

@Composable
private fun TeamStatsRow(
    modifier: Modifier = Modifier,
    viewModel: StandingPageViewModel,
    stats: TeamStats,
    dividerVisible: Boolean,
    dividerWidth: Dp,
    dividerThickness: Dp,
) {
    val labels by viewModel.standingLabel
    val sorting by viewModel.standingSort.collectAsState()
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            labels.forEach { label ->
                Text(
                    modifier = Modifier
                        .testTag("TeamStanding_Text_Stats")
                        .width(label.width)
                        .height(40.dp)
                        .background(
                            if (label.sort == sorting) {
                                MaterialTheme.colors.secondary.copy(Transparency25)
                            } else {
                                Color.Transparent
                            }
                        )
                        .padding(8.dp),
                    text = viewModel.getEvaluationTextByLabel(label, stats),
                    textAlign = if (label.sort == sorting) TextAlign.Center else label.textAlign,
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        if (dividerVisible) {
            Divider(
                modifier = Modifier.width(dividerWidth),
                color = dividerSecondaryColor(),
                thickness = dividerThickness
            )
        }
    }
}

@Composable
private fun TeamStatsTabRow(
    modifier: Modifier = Modifier,
    viewModel: StandingPageViewModel,
) {
    val labels by viewModel.standingLabel
    val sort by viewModel.standingSort.collectAsState()
    Row(modifier = modifier) {
        labels.forEach { label ->
            TeamStatsTab(
                label = label,
                isSelected = label.sort == sort,
                onClick = { viewModel.updateStandingSort(it.sort) }
            )
        }
    }
}

@Composable
private fun TeamStatsTab(
    label: StandingLabel,
    isSelected: Boolean,
    onClick: (StandingLabel) -> Unit
) {
    Box(
        modifier = Modifier
            .testTag("TeamStanding_Box_Label")
            .width(label.width)
            .height(40.dp)
            .background(
                if (isSelected) {
                    MaterialTheme.colors.secondary.copy(Transparency25)
                } else {
                    Color.Transparent
                }
            )
            .rippleClickable {
                onClick(label)
            }
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxSize(),
            text = label.text,
            textAlign = if (isSelected) TextAlign.Center else label.textAlign,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
    }
}

@Composable
private fun TeamStandingNameTable(
    teamState: LazyListState,
    teamStats: List<TeamStats>,
    onClickTeam: (NBATeam) -> Unit
) {
    var teamNameWidth by remember { mutableStateOf(0) }
    Column {
        Spacer(modifier = Modifier.height(40.dp))
        Divider(
            modifier = Modifier.width(teamNameWidth.px2Dp()),
            color = dividerSecondaryColor(),
            thickness = 3.dp
        )
        DisableOverscroll {
            LazyColumn(
                modifier = Modifier.testTag("TeamStanding_LC_Standing"),
                state = teamState
            ) {
                itemsIndexed(teamStats) { index, stats ->
                    TeamNameRow(
                        modifier = Modifier
                            .testTag("TeamStanding_Row_TeamName")
                            .onSizeChanged {
                                teamNameWidth = max(it.width, teamNameWidth)
                            }
                            .wrapContentWidth()
                            .rippleClickable { onClickTeam(stats.team) },
                        number = index + 1,
                        team = stats.team,
                        dividerVisible = index < teamStats.size - 1,
                        dividerWidth = teamNameWidth.px2Dp()
                    )
                }
            }
        }
    }
}

@Composable
private fun TeamNameRow(
    modifier: Modifier = Modifier,
    number: Int,
    team: NBATeam,
    dividerVisible: Boolean,
    dividerWidth: Dp
) {
    Column(modifier = modifier) {
        TeamNameText(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(24.dp),
            number = number,
            team = team
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (dividerVisible) {
            Divider(
                modifier = Modifier.width(dividerWidth),
                color = dividerSecondaryColor(),
                thickness = if (number == 10) 3.dp else 1.dp
            )
        }
    }
}

@Composable
private fun TeamNameText(
    modifier: Modifier = Modifier,
    number: Int,
    team: NBATeam
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .testTag("TeamStanding_Text_Index")
                .padding(start = 8.dp)
                .width(24.dp),
            text = number.toString(),
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            color = MaterialTheme.colors.secondary,
            maxLines = 1
        )
        AsyncImage(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f),
            model = ImageRequest.Builder(LocalContext.current)
                .data(NbaUtils.getTeamLogoUrlById(team.teamId))
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            error = painterResource(team.logoRes),
            placeholder = painterResource(team.logoRes),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .testTag("TeamStanding_Text_TeamName")
                .padding(start = 4.dp),
            text = team.teamName,
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            color = MaterialTheme.colors.secondary,
            maxLines = 1
        )
    }
}
