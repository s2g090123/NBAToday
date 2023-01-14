package com.jiachian.nbatoday.compose.screen.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.*
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.local.team.DefaultTeam
import com.jiachian.nbatoday.data.local.team.TeamStats
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import com.jiachian.nbatoday.data.remote.leader.GameLeaders
import com.jiachian.nbatoday.utils.*
import kotlin.math.max
import kotlin.math.pow

@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    FocusableColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
    ) {
        HomeBody(
            modifier = Modifier.weight(1f),
            viewModel = viewModel
        )
        HomeBottom(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .noRippleClickable { }
                .background(MaterialTheme.colors.secondary),
            viewModel = viewModel
        )
    }
}

@Composable
private fun HomeBody(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {
    val homeIndex by viewModel.homeIndex.collectAsState()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val lazyState = rememberLazyListState()

    LazyRow(
        modifier = modifier.width(screenWidth),
        state = lazyState,
        userScrollEnabled = false
    ) {
        item {
            SchedulePage(
                modifier = Modifier
                    .width(screenWidth)
                    .fillMaxHeight(),
                viewModel = viewModel
            )
        }
        item {
            StandingPage(
                modifier = Modifier
                    .width(screenWidth)
                    .fillMaxHeight(),
                viewModel = viewModel
            )
        }
        item {
            ThemePage(
                modifier = Modifier
                    .width(screenWidth)
                    .fillMaxHeight(),
                viewModel = viewModel
            )
        }
    }

    LaunchedEffect(homeIndex) {
        lazyState.animateScrollToItem(homeIndex)
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
private fun SchedulePage(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState()
    val dateStrings = viewModel.scheduleDates
    val index by viewModel.scheduleIndex.collectAsState()
    val scheduleGames by viewModel.scheduleGames.collectAsState()
    val isRefreshing by viewModel.isRefreshingSchedule.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.updateTodaySchedule() }
    )

    Box(
        modifier = modifier
    ) {
        HorizontalPager(
            modifier = Modifier
                .padding(top = 48.dp)
                .fillMaxSize(),
            state = pagerState,
            count = dateStrings.size
        ) { page ->
            val dateString = dateStrings.getOrNull(page)
            Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
                if (dateString != null) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        val games = scheduleGames[dateString] ?: listOf()
                        itemsIndexed(games) { index, game ->
                            GameStatusCard(
                                modifier = Modifier
                                    .padding(
                                        top = 16.dp,
                                        bottom = if (index >= games.size - 1) 16.dp else 0.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    )
                                    .clip(RoundedCornerShape(16.dp))
                                    .shadow(8.dp)
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .background(MaterialTheme.colors.secondary)
                                    .rippleClickable {
                                        if (game.gameStatus == GameStatusCode.COMING_SOON) {
                                            Toast
                                                .makeText(
                                                    context,
                                                    context.getString(R.string.game_is_coming_soon),
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        } else {
                                            viewModel.openGameBoxScore(game)
                                        }
                                    },
                                game = game
                            )
                        }
                    }
                }
                PullRefreshIndicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    refreshing = isRefreshing,
                    state = pullRefreshState
                )
            }
        }
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            contentColor = MaterialTheme.colors.primaryVariant,
            backgroundColor = MaterialTheme.colors.secondary,
            edgePadding = 0.dp,
            indicator = @Composable { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    color = MaterialTheme.colors.primaryVariant
                )
            }
        ) {
            dateStrings.forEachIndexed { dateIndex, date ->
                Tab(
                    text = {
                        Text(
                            text = date.substringAfter("/"),
                            color = MaterialTheme.colors.primary,
                            fontSize = 14.sp
                        )
                    },
                    selected = dateIndex == index,
                    onClick = { viewModel.updateScheduleIndex(dateIndex) }
                )
            }
        }
    }
    LaunchedEffect(index) {
        pagerState.scrollToPage(index)
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
private fun StandingPage(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {
    val teamStats by viewModel.teamStats.collectAsState()
    val selectIndex by viewModel.standingIndex.collectAsState()
    val isRefreshing by viewModel.isRefreshingTeamStats.collectAsState()
    val pagerState = rememberPagerState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.updateTeamStats() }
    )

    Box(
        modifier = modifier
    ) {
        HorizontalPager(
            modifier = Modifier
                .padding(top = 48.dp)
                .fillMaxSize(),
            state = pagerState,
            count = 2,
            userScrollEnabled = false
        ) { page ->
            val stats =
                if (page == 0) teamStats[DefaultTeam.Conference.EAST] else teamStats[DefaultTeam.Conference.WEST]
            if (stats != null) {
                TeamStanding(
                    modifier = Modifier
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
        TabRow(
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
            repeat(2) { index ->
                Tab(
                    text = {
                        Text(
                            text = stringResource(if (index == 0) R.string.standing_conference_east else R.string.standing_conference_west),
                            color = MaterialTheme.colors.primary,
                            fontSize = 14.sp
                        )
                    },
                    selected = selectIndex == index,
                    onClick = { viewModel.updateStandingIndex(index) }
                )
            }
        }
    }
    LaunchedEffect(selectIndex) {
        pagerState.scrollToPage(selectIndex)
    }
}

@Composable
private fun ThemePage(
    modifier: Modifier,
    viewModel: HomeViewModel
) {
    val isPhone = isPhone()
    val isPortrait = isPortrait()

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(
            when {
                isPhone && isPortrait -> 2
                isPhone && !isPortrait -> 3
                !isPhone && isPortrait -> 4
                else -> 6
            }
        ),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(31) { index ->
            val (teamId, color) = when (index) {
                1 -> 1610612757 to BlazersColors
                2 -> 1610612749 to BucksColors
                3 -> 1610612741 to BullsColors
                4 -> 1610612739 to CavaliersColors
                5 -> 1610612738 to CelticsColors
                6 -> 1610612746 to ClippersColors
                7 -> 1610612763 to GrizzliesColors
                8 -> 1610612737 to HawksColors
                9 -> 1610612748 to HeatColors
                10 -> 1610612766 to HornetsColors
                11 -> 1610612762 to JazzColors
                12 -> 1610612758 to KingsColors
                13 -> 1610612752 to KnicksColors
                14 -> 1610612747 to LakersColors
                15 -> 1610612753 to MagicColors
                16 -> 1610612742 to MavericksColors
                17 -> 1610612751 to NetsColors
                18 -> 1610612743 to NuggetsColors
                19 -> 1610612754 to PacersColors
                20 -> 1610612740 to PelicansColors
                21 -> 1610612765 to PistonsColors
                22 -> 1610612761 to RaptorsColors
                23 -> 1610612745 to RocketsColors
                24 -> 1610612759 to SpursColors
                25 -> 1610612755 to p76ersColors
                26 -> 1610612756 to SunsColors
                27 -> 1610612760 to ThunderColors
                28 -> 1610612750 to TimberwolvesColors
                29 -> 1610612744 to WarriorsColors
                30 -> 1610612764 to WizardsColors
                else -> 0 to OfficialColors
            }
            ThemeCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .border(1.dp, Color.White, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(7.dp))
                    .background(MaterialTheme.colors.secondary)
                    .rippleClickable {
                        viewModel.updateTheme(teamId, color)
                    }
                    .padding(bottom = 8.dp),
                team = DefaultTeam.getTeamById(teamId),
                firstColor = color.primary,
                secondColor = color.secondary,
                thirdColor = color.extra1,
                forthColor = color.extra2
            )
        }
    }
}

@Composable
private fun ThemeCard(
    modifier: Modifier = Modifier,
    team: DefaultTeam,
    firstColor: Color,
    secondColor: Color,
    thirdColor: Color,
    forthColor: Color
) {
    ConstraintLayout(
        modifier = modifier
    ) {
        val (teamImage, nameText, color1, color2, color3, color4) = createRefs()

        AsyncImage(
            modifier = Modifier
                .constrainAs(teamImage) {
                    top.linkTo(parent.top, 8.dp)
                    start.linkTo(parent.start, 8.dp)
                }
                .size(48.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(NbaUtils.getTeamLogoUrlById(team.teamId))
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            error = painterResource(NbaUtils.getTeamLogoResById(team.teamId)),
            placeholder = painterResource(NbaUtils.getTeamLogoResById(team.teamId)),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .constrainAs(nameText) {
                    top.linkTo(parent.top, 8.dp)
                    linkTo(teamImage.end, parent.end, 8.dp)
                    width = Dimension.fillToConstraints
                },
            text = team.teamName,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.primaryVariant
        )
        Box(
            modifier = Modifier
                .constrainAs(color1) {
                    top.linkTo(nameText.bottom, 8.dp)
                    start.linkTo(teamImage.end, 8.dp)
                }
                .size(18.dp)
                .border(1.dp, Color.White, CircleShape)
                .shadow(4.dp, CircleShape)
                .background(firstColor)
        )
        Box(
            modifier = Modifier
                .constrainAs(color2) {
                    top.linkTo(nameText.bottom, 8.dp)
                    start.linkTo(color1.end, 8.dp)
                }
                .size(18.dp)
                .border(1.dp, Color.White, CircleShape)
                .shadow(4.dp, CircleShape)
                .background(secondColor)
        )
        Box(
            modifier = Modifier
                .constrainAs(color3) {
                    top.linkTo(nameText.bottom, 8.dp)
                    start.linkTo(color2.end, 8.dp)
                }
                .size(18.dp)
                .border(1.dp, Color.White, CircleShape)
                .shadow(4.dp, CircleShape)
                .background(thirdColor)
        )
        Box(
            modifier = Modifier
                .constrainAs(color4) {
                    top.linkTo(nameText.bottom, 8.dp)
                    start.linkTo(color3.end, 8.dp)
                }
                .size(18.dp)
                .border(1.dp, Color.White, CircleShape)
                .shadow(4.dp, CircleShape)
                .background(forthColor)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TeamStanding(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    teamStats: List<TeamStats>
) {
    val teamState = rememberLazyListState()
    val statsState = rememberLazyListState()
    val horizontalScrollState = rememberScrollState()
    var dividerWidth by remember { mutableStateOf(0) }
    var teamNameWidth by remember { mutableStateOf(0) }
    val stateTeamOffset by remember { derivedStateOf { teamState.firstVisibleItemScrollOffset } }
    val stateTeamIndex by remember { derivedStateOf { teamState.firstVisibleItemIndex } }
    val stateStatsOffset by remember { derivedStateOf { statsState.firstVisibleItemScrollOffset } }
    val stateStatsIndex by remember { derivedStateOf { statsState.firstVisibleItemIndex } }
    val labels by viewModel.standingLabel
    val sort by viewModel.standingSort.collectAsState()

    Row(modifier = modifier) {
        Column {
            Spacer(modifier = Modifier.height(40.dp))
            Divider(
                modifier = Modifier.width(teamNameWidth.px2Dp()),
                color = MaterialTheme.colors.dividerSecondary(),
                thickness = 3.dp
            )
            CompositionLocalProvider(
                LocalOverscrollConfiguration provides null
            ) {
                LazyColumn(state = teamState) {
                    itemsIndexed(teamStats) { index, stat ->
                        Column(
                            modifier = Modifier
                                .wrapContentWidth()
                                .rippleClickable { viewModel.openTeamStats(stat.teamId) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .onSizeChanged {
                                        teamNameWidth = max(it.width, teamNameWidth)
                                    }
                                    .padding(top = 8.dp)
                                    .height(24.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .width(24.dp),
                                    text = (index + 1).toString(),
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
                                        .data(NbaUtils.getTeamLogoUrlById(stat.teamId))
                                        .decoderFactory(SvgDecoder.Factory())
                                        .build(),
                                    error = painterResource(NbaUtils.getTeamLogoResById(stat.teamId)),
                                    placeholder = painterResource(
                                        NbaUtils.getTeamLogoResById(
                                            stat.teamId
                                        )
                                    ),
                                    contentDescription = null
                                )
                                Text(
                                    modifier = Modifier.padding(start = 4.dp),
                                    text = stat.teamName,
                                    textAlign = TextAlign.Start,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colors.secondary,
                                    maxLines = 1
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            if (index < teamStats.size - 1) {
                                Divider(
                                    modifier = Modifier.width(teamNameWidth.px2Dp()),
                                    color = MaterialTheme.colors.dividerSecondary(),
                                    thickness = if (index == 9) 3.dp else 1.dp
                                )
                            }
                        }
                    }
                }
            }
        }
        Column(modifier = modifier.horizontalScroll(horizontalScrollState)) {
            Row(
                modifier = Modifier
                    .onSizeChanged {
                        dividerWidth = max(dividerWidth, it.width)
                    }
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                labels.forEach { label ->
                    Box(
                        modifier = Modifier
                            .width(label.width)
                            .height(40.dp)
                            .background(
                                if (label.sort == sort) {
                                    MaterialTheme.colors.secondary.copy(0.25f)
                                } else {
                                    Color.Transparent
                                }
                            )
                            .rippleClickable {
                                viewModel.updateStandingSort(label)
                            }
                            .padding(8.dp)
                    ) {
                        Text(
                            modifier = Modifier.fillMaxSize(),
                            text = label.text,
                            textAlign = if (label.sort == sort) TextAlign.Center else label.textAlign,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colors.secondary
                        )
                    }
                }
            }
            Divider(
                modifier = Modifier.width(dividerWidth.px2Dp()),
                color = MaterialTheme.colors.dividerSecondary(),
                thickness = 3.dp
            )
            CompositionLocalProvider(
                LocalOverscrollConfiguration provides null
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    state = statsState
                ) {
                    itemsIndexed(teamStats) { index, stats ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            labels.forEach { label ->
                                Text(
                                    modifier = Modifier
                                        .width(label.width)
                                        .height(40.dp)
                                        .background(
                                            if (label.sort == sort) {
                                                MaterialTheme.colors.secondary.copy(0.25f)
                                            } else {
                                                Color.Transparent
                                            }
                                        )
                                        .padding(8.dp),
                                    text = when (label.text) {
                                        "GP" -> stats.gamePlayed.toString()
                                        "W" -> stats.win.toString()
                                        "L" -> stats.lose.toString()
                                        "WIN%" -> stats.winPercentage.decimalFormat()
                                        "PTS" -> (stats.points.toDouble() / stats.gamePlayed).decimalFormat()
                                        "FGM" -> (stats.fieldGoalsMade.toDouble() / stats.gamePlayed).decimalFormat()
                                        "FGA" -> (stats.fieldGoalsAttempted.toDouble() / stats.gamePlayed).decimalFormat()
                                        "FG%" -> stats.fieldGoalsPercentage.decimalFormat()
                                        "3PM" -> (stats.threePointersMade.toDouble() / stats.gamePlayed).decimalFormat()
                                        "3PA" -> (stats.threePointersAttempted.toDouble() / stats.gamePlayed).decimalFormat()
                                        "3P%" -> stats.threePointersPercentage.decimalFormat()
                                        "FTM" -> (stats.freeThrowsMade.toDouble() / stats.gamePlayed).decimalFormat()
                                        "FTA" -> (stats.freeThrowsAttempted.toDouble() / stats.gamePlayed).decimalFormat()
                                        "FT%" -> stats.freeThrowsPercentage.decimalFormat()
                                        "OREB" -> (stats.reboundsOffensive.toDouble() / stats.gamePlayed).decimalFormat()
                                        "DREB" -> (stats.reboundsDefensive.toDouble() / stats.gamePlayed).decimalFormat()
                                        "REB" -> (stats.reboundsTotal.toDouble() / stats.gamePlayed).decimalFormat()
                                        "AST" -> (stats.assists.toDouble() / stats.gamePlayed).decimalFormat()
                                        "TOV" -> (stats.turnovers.toDouble() / stats.gamePlayed).decimalFormat()
                                        "STL" -> (stats.steals.toDouble() / stats.gamePlayed).decimalFormat()
                                        "BLK" -> (stats.blocks.toDouble() / stats.gamePlayed).decimalFormat()
                                        "PF" -> (stats.foulsPersonal.toDouble() / stats.gamePlayed).decimalFormat()
                                        else -> ""
                                    },
                                    textAlign = if (label.sort == sort) TextAlign.Center else label.textAlign,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colors.secondary
                                )
                            }
                        }
                        if (index < teamStats.size - 1) {
                            Divider(
                                modifier = Modifier.width(dividerWidth.px2Dp()),
                                color = MaterialTheme.colors.dividerSecondary(),
                                thickness = if (index == 9) 3.dp else 1.dp
                            )
                        }
                    }
                }
            }
        }
    }
    LaunchedEffect(stateTeamOffset, stateTeamIndex) {
        statsState.scrollToItem(stateTeamIndex, stateTeamOffset)
    }
    LaunchedEffect(stateStatsOffset, stateStatsIndex) {
        teamState.scrollToItem(stateStatsIndex, stateStatsOffset)
    }
}

@Composable
private fun HomeBottom(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {
    val selectIndex by viewModel.homeIndex.collectAsState()
    val scheduleScale by animateFloatAsState(targetValue = if (selectIndex == 0) 1.2f else 1f)
    val standingScale by animateFloatAsState(targetValue = if (selectIndex == 1) 1.2f else 1f)
    val themeScale by animateFloatAsState(targetValue = if (selectIndex == 2) 1.2f else 1f)

    Row(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .scale(scheduleScale)
                .rippleClickable { viewModel.updateHomeIndex(0) }
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_black_schedule),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
            )
            Text(
                text = stringResource(R.string.home_bottom_schedule),
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .scale(standingScale)
                .rippleClickable { viewModel.updateHomeIndex(1) }
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_black_ranking),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
            )
            Text(
                text = stringResource(R.string.home_bottom_standings),
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .scale(themeScale)
                .rippleClickable { viewModel.updateHomeIndex(2) }
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_black_palette),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
            )
            Text(
                text = stringResource(R.string.home_bottom_theme),
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun GameStatusCard(
    modifier: Modifier = Modifier,
    game: NbaGame
) {
    var isExpand by rememberSaveable { mutableStateOf(false) }

    ConstraintLayout(
        modifier = modifier
    ) {
        val (
            homeTeamText, homeLogo, homeScoreText,
            awayTeamText, awayLogo, awayScoreText,
            gameStatusText, expandBtn, playersDetail
        ) = createRefs()

        Text(
            modifier = Modifier
                .constrainAs(homeTeamText) {
                    top.linkTo(parent.top, 16.dp)
                    linkTo(homeLogo.start, homeLogo.end)
                },
            text = game.homeTeam.teamTricode,
            color = MaterialTheme.colors.primary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        AsyncImage(
            modifier = Modifier
                .constrainAs(homeLogo) {
                    top.linkTo(homeTeamText.bottom, 8.dp)
                    start.linkTo(parent.start, 16.dp)
                }
                .size(100.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(NbaUtils.getTeamLogoUrlById(game.homeTeam.teamId))
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            error = painterResource(NbaUtils.getTeamLogoResById(game.homeTeam.teamId)),
            placeholder = painterResource(NbaUtils.getTeamLogoResById(game.homeTeam.teamId)),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .constrainAs(homeScoreText) {
                    top.linkTo(homeLogo.bottom, 8.dp)
                    linkTo(homeLogo.start, homeLogo.end)
                },
            text = game.homeTeam.score.toString(),
            color = MaterialTheme.colors.primary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .constrainAs(awayTeamText) {
                    top.linkTo(parent.top, 16.dp)
                    linkTo(awayLogo.start, awayLogo.end)
                },
            text = game.awayTeam.teamTricode,
            color = MaterialTheme.colors.primary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        AsyncImage(
            modifier = Modifier
                .constrainAs(awayLogo) {
                    top.linkTo(awayTeamText.bottom, 8.dp)
                    end.linkTo(parent.end, 16.dp)
                }
                .size(100.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(NbaUtils.getTeamLogoUrlById(game.awayTeam.teamId))
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            error = painterResource(NbaUtils.getTeamLogoResById(game.awayTeam.teamId)),
            placeholder = painterResource(NbaUtils.getTeamLogoResById(game.awayTeam.teamId)),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .constrainAs(awayScoreText) {
                    top.linkTo(awayLogo.bottom, 8.dp)
                    linkTo(awayLogo.start, awayLogo.end)
                },
            text = game.awayTeam.score.toString(),
            color = MaterialTheme.colors.primary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .constrainAs(gameStatusText) {
                    linkTo(homeLogo.top, awayLogo.bottom)
                    linkTo(homeLogo.end, awayLogo.start)
                },
            text = if (game.gameStatus == GameStatusCode.COMING_SOON) {
                game.gameStatusText.replaceFirst(" ", "\n")
            } else {
                game.gameStatusText
            }.trim(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic
        )
        AnimatedVisibility(
            modifier = Modifier
                .constrainAs(expandBtn) {
                    top.linkTo(homeScoreText.bottom)
                    linkTo(parent.start, parent.end)
                    width = Dimension.fillToConstraints
                }
                .height(24.dp)
                .rippleClickable { isExpand = true }
                .padding(vertical = 2.dp),
            visible = !isExpand,
            enter = expandIn(),
            exit = shrinkOut()
        ) {
            Image(
                painter = painterResource(R.drawable.ic_black_expand_more),
                alpha = 0.6f,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                contentDescription = null
            )
        }
        AnimatedVisibility(
            modifier = Modifier
                .constrainAs(playersDetail) {
                    linkTo(parent.start, parent.end, 24.dp, 24.dp)
                    top.linkTo(homeScoreText.bottom)
                    width = Dimension.fillToConstraints
                },
            visible = isExpand,
            enter = expandIn(),
            exit = shrinkOut()
        ) {
            Column {
                val isComingSoon = game.gameStatus == GameStatusCode.COMING_SOON
                val leaders = if (isComingSoon) game.teamLeaders else game.gameLeaders
                val homeLeader = leaders?.homeLeaders
                val awayLeader = leaders?.awayLeaders
                if (homeLeader != null && awayLeader != null) {
                    LeaderInfo(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        isGameFinal = !isComingSoon,
                        homeLeader = homeLeader,
                        awayLeader = awayLeader
                    )
                }
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .rippleClickable { isExpand = false }
                        .padding(vertical = 2.dp),
                    painter = painterResource(R.drawable.ic_black_collpase_more),
                    alpha = 0.6f,
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun LeaderInfo(
    modifier: Modifier = Modifier,
    isGameFinal: Boolean,
    homeLeader: GameLeaders.GameLeader,
    awayLeader: GameLeaders.GameLeader
) {
    ConstraintLayout(modifier = modifier) {
        val (divider, leaderGroup, ptsTitle, ptsText, rebTitle, rebText, astTitle, astText) = createRefs()

        Text(
            modifier = Modifier
                .constrainAs(astTitle) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
                .width(36.dp),
            text = stringResource(R.string.player_info_ast_abbr),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .constrainAs(rebTitle) {
                    end.linkTo(astTitle.start)
                    top.linkTo(parent.top)
                }
                .width(36.dp),
            text = stringResource(R.string.player_info_reb_abbr),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .constrainAs(ptsTitle) {
                    end.linkTo(rebTitle.start)
                    top.linkTo(parent.top)
                }
                .width(36.dp),
            text = stringResource(R.string.player_info_pts_abbr),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Divider(
            modifier = Modifier
                .constrainAs(divider) {
                    top.linkTo(ptsTitle.bottom, 4.dp)
                }
                .fillMaxWidth(),
            color = MaterialTheme.colors.dividerPrimary()
        )
        Column(
            modifier = Modifier
                .constrainAs(leaderGroup) {
                    top.linkTo(divider.bottom)
                }
                .fillMaxWidth()
        ) {
            arrayOf(homeLeader, awayLeader).forEach { player ->
                ConstraintLayout(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val (playerImage, playerNameText, playerInfoText) = createRefs()
                    AsyncImage(
                        modifier = Modifier
                            .constrainAs(playerImage) {
                                top.linkTo(parent.top, 8.dp)
                                start.linkTo(parent.start)
                            }
                            .size(width = 52.dp, height = 38.dp),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(NbaUtils.getPlayerImageUrlById(player.personId))
                            .decoderFactory(SvgDecoder.Factory())
                            .build(),
                        error = painterResource(R.drawable.ic_black_person),
                        placeholder = painterResource(R.drawable.ic_black_person),
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier
                            .constrainAs(playerNameText) {
                                start.linkTo(playerImage.end, 4.dp)
                                linkTo(playerImage.top, playerInfoText.top)
                            },
                        text = player.name,
                        color = MaterialTheme.colors.primary,
                        fontSize = 12.sp
                    )
                    Text(
                        modifier = Modifier
                            .constrainAs(playerInfoText) {
                                start.linkTo(playerImage.end, 4.dp)
                                linkTo(playerNameText.bottom, playerImage.bottom)
                            },
                        text = player.teamTricode + " | #" + player.jerseyNum + " | " + player.position,
                        color = MaterialTheme.colors.primary,
                        fontSize = 12.sp
                    )
                    Text(
                        modifier = Modifier
                            .constrainAs(astText) {
                                end.linkTo(parent.end)
                                linkTo(playerImage.top, playerImage.bottom)
                            }
                            .width(36.dp),
                        text = (if (isGameFinal) player.assists.toInt() else player.assists).toString(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.primary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        modifier = Modifier
                            .constrainAs(rebText) {
                                end.linkTo(astText.start)
                                linkTo(playerImage.top, playerImage.bottom)
                            }
                            .width(36.dp),
                        text = (if (isGameFinal) player.rebounds.toInt() else player.rebounds).toString(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.primary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        modifier = Modifier
                            .constrainAs(ptsText) {
                                end.linkTo(rebText.start)
                                linkTo(playerImage.top, playerImage.bottom)
                            }
                            .width(36.dp),
                        text = (if (isGameFinal) player.points.toInt() else player.points).toString(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.primary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun Double.decimalFormat(radix: Int = 1): String {
    val value = (this * 10.0.pow(radix)).toInt() / 10.0.pow(radix)
    return value.toString()
}