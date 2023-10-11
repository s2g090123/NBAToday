package com.jiachian.nbatoday.compose.screen.score

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.score.data.ScoreLeaderRowData
import com.jiachian.nbatoday.compose.screen.score.data.ScoreRowData
import com.jiachian.nbatoday.compose.screen.score.data.ScoreTeamRowData
import com.jiachian.nbatoday.compose.screen.score.label.ScoreLabel
import com.jiachian.nbatoday.compose.screen.score.label.ScoreLabelType
import com.jiachian.nbatoday.compose.screen.score.tab.BoxScoreTab
import com.jiachian.nbatoday.compose.widget.BackHandle
import com.jiachian.nbatoday.compose.widget.DisableOverscroll
import com.jiachian.nbatoday.compose.widget.IconButton
import com.jiachian.nbatoday.compose.widget.NullCheckScreen
import com.jiachian.nbatoday.compose.widget.PlayerImage
import com.jiachian.nbatoday.compose.widget.RefreshScreen
import com.jiachian.nbatoday.compose.widget.TeamLogoImage
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.utils.dividerSecondaryColor
import com.jiachian.nbatoday.utils.getOrNA
import com.jiachian.nbatoday.utils.modifyIf
import com.jiachian.nbatoday.utils.noRippleClickable
import com.jiachian.nbatoday.utils.px2Dp
import com.jiachian.nbatoday.utils.rippleClickable
import kotlin.math.max

@Composable
fun BoxScoreScreen(
    viewModel: BoxScoreViewModel,
    onBack: () -> Unit
) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val date by viewModel.date.collectAsState()
    val isNotFound by viewModel.isNotFound.collectAsState()
    BackHandle(onBack = onBack) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primary)
                .noRippleClickable { }
        ) {
            ScoreTopBar(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                date = date,
                onBack = onBack
            )
            when {
                isRefreshing -> {
                    RefreshScreen(modifier = Modifier.fillMaxSize())
                }

                isNotFound -> {
                    EmptyScreen(modifier = Modifier.fillMaxSize())
                }

                else -> {
                    ScoreScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun ScoreScreen(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel
) {
    val boxScore by viewModel.boxScore.collectAsState()
    val labels by viewModel.periodLabel.collectAsState()
    NullCheckScreen(
        data = boxScore,
        ifNull = {
            EmptyScreen(modifier = modifier)
        },
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
                        .testTag("ScoreScreen_ScorePeriod")
                        .padding(top = 16.dp, start = 12.dp, end = 12.dp)
                        .fillMaxWidth(),
                    score = score,
                    labels = labels
                )
                ScoreDetail(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    viewModel = viewModel,
                    score = score
                )
            }
        }
    )
}

@Composable
private fun ScoreTopBar(
    modifier: Modifier = Modifier,
    date: String,
    onBack: () -> Unit
) {
    Row(modifier = modifier) {
        IconButton(
            modifier = Modifier
                .testTag("ScoreScreen_Btn_Back")
                .padding(start = 8.dp),
            drawableRes = R.drawable.ic_black_back,
            tint = MaterialTheme.colors.secondary,
            onClick = onBack
        )
        Text(
            modifier = Modifier
                .testTag("ScoreScreen_Text_Date")
                .align(Alignment.CenterVertically)
                .padding(start = 16.dp),
            text = date,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
    }
}

@Composable
private fun ScoreTotal(
    modifier: Modifier = Modifier,
    score: GameBoxScore
) {
    val homeTeam = remember(score) { score.homeTeam }
    val awayTeam = remember(score) { score.awayTeam }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TeamInfo(
            modifier = Modifier
                .testTag("ScoreTotal_TeamInfo_Home")
                .padding(start = 24.dp),
            team = homeTeam.team
        )
        GameScoreStatus(
            homeScore = homeTeam.score,
            awayScore = awayTeam.score,
            gameStatus = score.statusText
        )
        TeamInfo(
            modifier = Modifier
                .testTag("ScoreTotal_TeamInfo_Away")
                .padding(end = 24.dp),
            team = awayTeam.team
        )
    }
}

@Composable
private fun TeamInfo(
    modifier: Modifier = Modifier,
    team: NBATeam
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TeamLogoImage(
            modifier = Modifier.size(100.dp),
            team = team
        )
        Text(
            modifier = Modifier
                .testTag("TeamInfo_Text_Name")
                .padding(top = 8.dp),
            text = team.teamName,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            color = MaterialTheme.colors.secondary
        )
    }
}

@Composable
private fun GameScoreStatus(
    modifier: Modifier = Modifier,
    homeScore: Int,
    awayScore: Int,
    gameStatus: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.testTag("ScoreTotal_Text_ScoreComparison"),
            text = stringResource(R.string.box_score_comparison, homeScore, awayScore),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = MaterialTheme.colors.secondary
        )
        Text(
            modifier = Modifier
                .testTag("ScoreTotal_Text_Status")
                .padding(top = 16.dp),
            text = gameStatus,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            fontSize = 24.sp,
            color = MaterialTheme.colors.secondary
        )
    }
}

@Composable
private fun ScorePeriod(
    modifier: Modifier = Modifier,
    score: GameBoxScore,
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
    homeTeam: GameBoxScore.BoxScoreTeam,
    awayTeam: GameBoxScore.BoxScoreTeam
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
    team: GameBoxScore.BoxScoreTeam
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

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ScoreDetail(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel,
    score: GameBoxScore
) {
    val tabs = remember { BoxScoreTab.values() }
    val selectPage by viewModel.selectPage.collectAsState()
    val selectIndex by remember {
        derivedStateOf { tabs.indexOf(selectPage) }
    }
    val pagerState = rememberPagerState(initialPage = selectIndex)
    Column(modifier = modifier) {
        StatisticsTabRow(
            modifier = Modifier.fillMaxWidth(),
            pagerState = pagerState,
            selectIndex = selectIndex,
            homeTeamName = score.homeTeam.team.teamName.getOrNA(),
            awayTeamName = score.awayTeam.team.teamName.getOrNA(),
            onTabClick = { viewModel.updateSelectPage(it) }
        )
        ScoreDetailPager(
            modifier = Modifier
                .testTag("ScoreDetail_Pager")
                .fillMaxWidth(),
            viewModel = viewModel,
            pagerState = pagerState,
            count = tabs.size
        )
    }
    LaunchedEffect(selectIndex) {
        pagerState.animateScrollToPage(selectIndex)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ScoreDetailPager(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel,
    pagerState: PagerState,
    count: Int
) {
    val homeScoreRowData by viewModel.homeScoreRowData.collectAsState()
    val awayScoreRowData by viewModel.awayScoreRowData.collectAsState()
    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        count = count,
        userScrollEnabled = false
    ) { index ->
        when (index) {
            0 -> {
                PlayerStatistics(
                    modifier = Modifier
                        .testTag("ScoreDetail_PlayerStatistics_Home")
                        .height((LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                        .fillMaxWidth(),
                    scoreRowData = homeScoreRowData,
                    labels = viewModel.statsLabels,
                    showPlayerCareer = { viewModel.showPlayerCareer(it) }
                )
            }

            1 -> {
                PlayerStatistics(
                    modifier = Modifier
                        .testTag("ScoreDetail_PlayerStatistics_Away")
                        .height((LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                        .fillMaxWidth(),
                    scoreRowData = awayScoreRowData,
                    labels = viewModel.statsLabels,
                    showPlayerCareer = { viewModel.showPlayerCareer(it) }
                )
            }

            2 -> {
                TeamStatistics(
                    modifier = Modifier
                        .testTag("ScoreDetail_TeamStatistics")
                        .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    viewModel = viewModel
                )
            }

            3 -> {
                LeaderStatistics(
                    modifier = Modifier
                        .testTag("ScoreDetail_LeaderStatistics")
                        .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    viewModel = viewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun StatisticsTabRow(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    selectIndex: Int,
    homeTeamName: String,
    awayTeamName: String,
    onTabClick: (BoxScoreTab) -> Unit
) {
    TabRow(
        modifier = modifier,
        selectedTabIndex = selectIndex,
        backgroundColor = MaterialTheme.colors.secondary,
        contentColor = MaterialTheme.colors.primaryVariant,
        indicator = @Composable { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                color = MaterialTheme.colors.primaryVariant
            )
        }
    ) {
        StatisticsTab(
            modifier = Modifier.testTag("ScoreDetail_Tab_Home"),
            text = homeTeamName,
            isSelected = selectIndex == 0,
            onClick = { onTabClick(BoxScoreTab.HOME) }
        )
        StatisticsTab(
            modifier = Modifier.testTag("ScoreDetail_Tab_Away"),
            text = awayTeamName,
            isSelected = selectIndex == 1,
            onClick = { onTabClick(BoxScoreTab.AWAY) }
        )
        StatisticsTab(
            modifier = Modifier.testTag("ScoreDetail_Tab_TeamStats"),
            text = stringResource(R.string.box_score_tab_statistics),
            isSelected = selectIndex == 2,
            onClick = { onTabClick(BoxScoreTab.STATS) }
        )
        StatisticsTab(
            modifier = Modifier.testTag("ScoreDetail_Tab_LeaderStats"),
            text = stringResource(R.string.box_score_tab_leaders),
            isSelected = selectIndex == 3,
            onClick = { onTabClick(BoxScoreTab.LEADER) }
        )
    }
}

@Composable
private fun StatisticsTab(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
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
        selected = isSelected,
        onClick = onClick
    )
}

@Composable
private fun PlayerStatistics(
    modifier: Modifier = Modifier,
    scoreRowData: List<ScoreRowData>,
    labels: Array<ScoreLabel>,
    showPlayerCareer: (Int) -> Unit
) {
    val statisticsState = rememberLazyListState()
    val playerState = rememberLazyListState()
    val stateStatisticsOffset by remember { derivedStateOf { statisticsState.firstVisibleItemScrollOffset } }
    val stateStatisticsIndex by remember { derivedStateOf { statisticsState.firstVisibleItemIndex } }
    val statePlayerOffset by remember { derivedStateOf { playerState.firstVisibleItemScrollOffset } }
    val statePlayerIndex by remember { derivedStateOf { playerState.firstVisibleItemIndex } }
    Row(modifier = modifier) {
        PlayerNameColumn(
            modifier = Modifier.width(124.dp),
            state = playerState,
            scoreRowData = scoreRowData,
            onClickPlayer = showPlayerCareer
        )
        PlayerStatsColumn(
            modifier = Modifier
                .testTag("PlayerStatistics_Column_Stats")
                .horizontalScroll(rememberScrollState()),
            state = statisticsState,
            scoreRowData = scoreRowData,
            labels = labels
        )
    }
    LaunchedEffect(stateStatisticsOffset, stateStatisticsIndex) {
        playerState.scrollToItem(stateStatisticsIndex, stateStatisticsOffset)
    }
    LaunchedEffect(statePlayerOffset, statePlayerIndex) {
        statisticsState.scrollToItem(statePlayerIndex, statePlayerOffset)
    }
}

@Composable
private fun PlayerNameColumn(
    modifier: Modifier = Modifier,
    state: LazyListState,
    scoreRowData: List<ScoreRowData>,
    onClickPlayer: (playerId: Int) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(8.dp),
            text = stringResource(R.string.box_score_label_player),
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = dividerSecondaryColor(),
            thickness = 3.dp
        )
        PlayerColumn(
            playerState = state,
            scoreRowData = scoreRowData,
            onClickPlayer = onClickPlayer
        )
    }
}

@Composable
private fun PlayerStatsColumn(
    modifier: Modifier = Modifier,
    state: LazyListState,
    scoreRowData: List<ScoreRowData>,
    labels: Array<ScoreLabel>
) {
    var dividerWidth by remember { mutableStateOf(0) }
    Column(modifier = modifier) {
        LabelRow(
            modifier = Modifier
                .testTag("PlayerStatistics_Row_Labels")
                .onSizeChanged {
                    dividerWidth = max(dividerWidth, it.width)
                }
                .fillMaxWidth()
                .padding(start = 16.dp),
            labels = labels
        )
        Divider(
            modifier = Modifier.width(dividerWidth.px2Dp()),
            color = dividerSecondaryColor(),
            thickness = 3.dp
        )
        PlayerStatsTable(
            modifier = Modifier
                .testTag("PlayerStatistics_LC_PlayerStats")
                .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                .fillMaxWidth(),
            state = state,
            scoreRowData = scoreRowData,
            dividerWidth = dividerWidth.px2Dp()
        )
    }
}

@Composable
private fun PlayerColumn(
    playerState: LazyListState,
    scoreRowData: List<ScoreRowData>,
    onClickPlayer: (playerId: Int) -> Unit
) {
    DisableOverscroll {
        LazyColumn(
            modifier = Modifier
                .testTag("PlayerStatistics_LC_Players")
                .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                .fillMaxWidth(),
            state = playerState
        ) {
            itemsIndexed(scoreRowData) { index, rowData ->
                Column(
                    modifier = Modifier
                        .testTag("PlayerStatistics_Column_Player")
                        .fillMaxWidth()
                        .rippleClickable {
                            onClickPlayer(rowData.playerId)
                        }
                ) {
                    Text(
                        modifier = Modifier
                            .testTag("PlayerStatistics_Text_PlayerName")
                            .fillMaxWidth()
                            .padding(top = 8.dp, start = 8.dp),
                        text = rowData.nameAbbr,
                        textAlign = TextAlign.Start,
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.secondary.copy(if (rowData.notPlaying) 0.5f else 1f),
                        maxLines = 1,
                        softWrap = false
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (index < scoreRowData.size - 1) {
                        Divider(
                            modifier = Modifier.fillMaxWidth(),
                            color = dividerSecondaryColor(),
                            thickness = 1.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayerStatsTable(
    modifier: Modifier = Modifier,
    state: LazyListState,
    scoreRowData: List<ScoreRowData>,
    dividerWidth: Dp
) {
    DisableOverscroll {
        LazyColumn(
            modifier = modifier,
            state = state
        ) {
            itemsIndexed(scoreRowData) { index, rowData ->
                PlayerStatsRow(
                    modifier = Modifier
                        .testTag("PlayerStatistics_Row_PlayerStats")
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    rowData = rowData
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (index < scoreRowData.size - 1) {
                    Divider(
                        modifier = Modifier.width(dividerWidth),
                        color = dividerSecondaryColor(),
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerStatsRow(
    modifier: Modifier = Modifier,
    rowData: ScoreRowData
) {
    Row(modifier = modifier) {
        Text(
            modifier = Modifier
                .testTag("PlayerStatistics_Text_PlayerPosition")
                .width(16.dp),
            text = rowData.position,
            textAlign = TextAlign.End,
            fontSize = 16.sp,
            color = MaterialTheme.colors.secondary
        )
        if (rowData.notPlaying) {
            PlayerNotPlayReasonText(rowData.notPlayingReason)
        } else {
            rowData.rowData.forEach { statsRowData ->
                PlayerStatsText(
                    width = statsRowData.textWidth,
                    text = statsRowData.value,
                    textAlign = statsRowData.textAlign
                )
            }

        }
    }
}

@Composable
private fun PlayerStatsText(
    width: Dp,
    text: String,
    textAlign: TextAlign
) {
    Text(
        modifier = Modifier
            .testTag("PlayerStatistics_Text_PlayerStats")
            .width(width)
            .padding(horizontal = 8.dp),
        text = text,
        textAlign = textAlign,
        fontSize = 16.sp,
        color = MaterialTheme.colors.secondary
    )
}

@Composable
private fun PlayerNotPlayReasonText(reason: String) {
    Text(
        modifier = Modifier
            .testTag("PlayerStatistics_Text_NotPlay")
            .width(72.dp),
        text = stringResource(R.string.box_score_player_dnp),
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
        color = MaterialTheme.colors.secondary.copy(0.5f)
    )
    Text(
        modifier = Modifier
            .testTag("PlayerStatistics_Text_NotPlayReason")
            .padding(start = 16.dp),
        text = reason,
        fontSize = 16.sp,
        color = MaterialTheme.colors.secondary.copy(0.5f)
    )
}

@Composable
private fun LabelRow(
    modifier: Modifier = Modifier,
    labels: Array<ScoreLabel>
) {
    var selectedType by remember { mutableStateOf<ScoreLabelType?>(null) }
    Row(modifier = modifier) {
        labels.forEach { label ->
            LabelText(
                label = label,
                isPopupVisible = label.type == selectedType,
                onClick = { selectedType = it.type },
                onClickOutside = { selectedType = null }
            )
        }
    }
}

@Composable
private fun LabelText(
    label: ScoreLabel,
    isPopupVisible: Boolean,
    onClick: (label: ScoreLabel) -> Unit,
    onClickOutside: () -> Unit
) {
    Box(
        modifier = Modifier
            .testTag("PlayerStatistics_Box_Label")
            .width(label.width)
            .height(40.dp)
            .rippleClickable { onClick(label) }
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxSize(),
            text = stringResource(label.textRes),
            textAlign = label.textAlign,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
        if (isPopupVisible) {
            LabelAboutPopup(
                text = stringResource(label.infoRes),
                onDismiss = { onClickOutside() }
            )
        }
    }
}

@Composable
private fun TeamStatsTitleRow(
    modifier: Modifier = Modifier,
    homeTeam: NBATeam,
    awayTeam: NBATeam
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TeamLogoImage(
            modifier = Modifier.size(56.dp),
            team = homeTeam
        )
        Text(
            text = stringResource(R.string.box_score_team_statistics_title),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
        TeamLogoImage(
            modifier = Modifier.size(56.dp),
            team = awayTeam
        )
    }
}

@Composable
private fun TeamStatsRow(
    modifier: Modifier = Modifier,
    rowData: ScoreTeamRowData
) {
    val label = rowData.label
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = if (label.topMargin) 8.dp else 0.dp, start = 4.dp, end = 4.dp),

            ) {
            Text(
                modifier = Modifier
                    .testTag("TeamStatsRow_Text_Home")
                    .align(Alignment.CenterStart),
                text = rowData.homeValue,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.secondary
            )
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(label.textRes),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.secondary
            )
            Text(
                modifier = Modifier
                    .testTag("TeamStatsRow_Text_Away")
                    .align(Alignment.CenterEnd),
                text = rowData.awayValue,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.secondary
            )
        }
        if (label.divider) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 12.dp),
                color = dividerSecondaryColor()
            )
        }
    }
}

@Composable
private fun TeamStatistics(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel
) {
    val homeTeam by viewModel.homeTeam.collectAsState()
    val awayTeam by viewModel.awayTeam.collectAsState()
    val teamRowData by viewModel.teamStatsRowData.collectAsState()
    LazyColumn(modifier = modifier) {
        item {
            TeamStatsTitleRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                homeTeam = homeTeam,
                awayTeam = awayTeam
            )
        }
        items(teamRowData) { rowData ->
            TeamStatsRow(
                modifier = Modifier
                    .testTag("TeamStatistics_TeamStatsRow")
                    .fillMaxWidth(),
                rowData = rowData
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun LeaderStatisticsTitleRow(
    modifier: Modifier = Modifier,
    homeLeader: GameBoxScore.BoxScoreTeam.Player?,
    awayLeader: GameBoxScore.BoxScoreTeam.Player?
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlayerImage(
            modifier = Modifier.size(56.dp),
            playerId = homeLeader?.personId
        )
        Text(
            text = stringResource(R.string.box_score_leader_statistics_title),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
        PlayerImage(
            modifier = Modifier.size(56.dp),
            playerId = awayLeader?.personId
        )
    }
}

@Composable
private fun LeaderStatisticsRow(
    modifier: Modifier = Modifier,
    rowData: ScoreLeaderRowData
) {
    val label = rowData.label
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .modifyIf(
                    condition = label.topMargin,
                    modify = { padding(top = 8.dp, start = 4.dp, end = 4.dp) },
                    elseModify = { padding(horizontal = 4.dp) }
                )
        ) {
            Text(
                modifier = Modifier
                    .testTag("LeaderStatisticsRow_Text_Home")
                    .align(Alignment.CenterStart),
                text = rowData.homeValue,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.secondary
            )
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(label.textRes),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.secondary
            )
            Text(
                modifier = Modifier
                    .testTag("LeaderStatisticsRow_Text_Away")
                    .align(Alignment.CenterEnd),
                text = rowData.awayValue,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.secondary
            )
        }
        if (label.divider) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 12.dp),
                color = dividerSecondaryColor()
            )
        }
    }
}

@Composable
private fun LeaderStatistics(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel
) {
    val homeLeader by viewModel.homeLeader.collectAsState()
    val awayLeader by viewModel.awayLeader.collectAsState()
    val leaderRowData by viewModel.leaderStatsRowData.collectAsState()
    LazyColumn(modifier = modifier) {
        item {
            LeaderStatisticsTitleRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                homeLeader = homeLeader,
                awayLeader = awayLeader
            )
        }
        items(leaderRowData) { rowData ->
            LeaderStatisticsRow(
                modifier = Modifier
                    .testTag("LeaderStatistics_LeaderStatisticsRow")
                    .fillMaxWidth(),
                rowData = rowData
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun LabelAboutPopup(
    text: String,
    onDismiss: () -> Unit
) {
    Popup(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .shadow(8.dp)
                .wrapContentSize()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colors.secondaryVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier
                    .testTag("LabelAboutPopup_Text_About")
                    .padding(8.dp),
                text = text,
                color = MaterialTheme.colors.primaryVariant
            )
        }
    }
}

@Composable
private fun EmptyScreen(
    modifier: Modifier = Modifier
) {
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
