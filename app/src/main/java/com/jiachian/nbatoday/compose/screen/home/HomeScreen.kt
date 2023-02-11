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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
import com.jiachian.nbatoday.compose.widget.CustomOutlinedTextField
import com.jiachian.nbatoday.compose.widget.RefreshingScreen
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.local.team.DefaultTeam
import com.jiachian.nbatoday.data.local.team.TeamStats
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import com.jiachian.nbatoday.data.remote.leader.GameLeaders
import com.jiachian.nbatoday.data.remote.user.User
import com.jiachian.nbatoday.utils.*
import kotlin.math.max
import kotlin.math.pow

@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()

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
    if (isRefreshing) {
        RefreshingScreen(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.secondary
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
            UserPage(
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
    val pagerState = rememberPagerState()
    val dateStrings = viewModel.scheduleDates
    val index by viewModel.scheduleIndex.collectAsState()
    val scheduleGames by viewModel.scheduleGames.collectAsState()
    val isRefreshing by viewModel.isRefreshingSchedule.collectAsState()
    val user by viewModel.user.collectAsState()
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
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(
                                    modifier = Modifier.padding(top = 8.dp, end = 4.dp),
                                    onClick = { viewModel.openCalendar(dateString) }
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_black_calendar),
                                        contentDescription = null,
                                        tint = MaterialTheme.colors.secondary
                                    )
                                }
                            }
                        }
                        itemsIndexed(games) { index, game ->
                            GameStatusCard2(
                                modifier = Modifier
                                    .padding(
                                        top = if (index == 0) 8.dp else 16.dp,
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
                                        if (game.game.gameStatus == GameStatusCode.COMING_SOON) {
                                            viewModel.openTeamStats(game.game.homeTeam.teamId)
                                        } else {
                                            viewModel.openGameBoxScore(game.game)
                                        }
                                    },
                                userData = user,
                                gameAndBet = game,
                                color = MaterialTheme.colors.primary,
                                expandable = true,
                                onLogin = viewModel::login,
                                onRegister = viewModel::register,
                                onConfirm = viewModel::bet
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
private fun UserPage(
    modifier: Modifier,
    viewModel: HomeViewModel
) {
    val isPhone = isPhone()
    val isPortrait = isPortrait()
    val user by viewModel.user.collectAsState()
    var showLoginDialog by remember { mutableStateOf(false) }

    if (user == null) {
        Box(modifier = modifier) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.user_login_hint),
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.secondaryVariant,
                    fontWeight = FontWeight.Medium
                )
                Button(
                    modifier = Modifier.padding(top = 8.dp),
                    onClick = { showLoginDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.user_login),
                        color = MaterialTheme.colors.secondaryVariant
                    )
                }
            }
        }
        if (showLoginDialog) {
            LoginDialog(
                onLogin = { account, password -> viewModel.login(account, password) },
                onRegister = { account, password -> viewModel.register(account, password) },
                onDismiss = { showLoginDialog = false }
            )
        }
    } else {
        Column(
            modifier = modifier
        ) {
            AccountInfo(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.secondary),
                name = user?.name ?: "",
                points = user?.points ?: 0,
                onLogoutClick = { viewModel.logout() }
            )
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
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
                painter = painterResource(R.drawable.ic_black_person),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
            )
            Text(
                text = stringResource(R.string.home_bottom_user),
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun GameStatusCard(
    modifier: Modifier = Modifier,
    game: NbaGame,
    color: Color,
    expandable: Boolean,
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
            color = color,
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
            color = color,
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
            color = color,
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
            color = color,
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
            color = color,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic
        )
        if (expandable) {
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
                    colorFilter = ColorFilter.tint(color),
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
                            awayLeader = awayLeader,
                            color = color
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
                        colorFilter = ColorFilter.tint(color),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun GameStatusCard2(
    modifier: Modifier = Modifier,
    gameAndBet: NbaGameAndBet,
    userData: User?,
    color: Color,
    expandable: Boolean,
    onLogin: (account: String, password: String) -> Unit,
    onRegister: (account: String, password: String) -> Unit,
    onConfirm: (gameId: String, homePoints: Long, awayPoints: Long) -> Unit
) {
    var isExpand by rememberSaveable { mutableStateOf(false) }
    val canBet by remember(gameAndBet) {
        derivedStateOf {
            gameAndBet.game.gameStatus == GameStatusCode.COMING_SOON && gameAndBet.bets == null
        }
    }
    var showBetsDialog by rememberSaveable { mutableStateOf(false) }

    ConstraintLayout(
        modifier = modifier
    ) {
        val (
            homeTeamText, homeLogo, homeScoreText,
            awayTeamText, awayLogo, awayScoreText,
            gameStatusText, expandBtn, playersDetail,
            coinsBtn
        ) = createRefs()

        Text(
            modifier = Modifier
                .constrainAs(homeTeamText) {
                    top.linkTo(parent.top, 16.dp)
                    linkTo(homeLogo.start, homeLogo.end)
                },
            text = gameAndBet.game.homeTeam.teamTricode,
            color = color,
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
                .data(NbaUtils.getTeamLogoUrlById(gameAndBet.game.homeTeam.teamId))
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            error = painterResource(NbaUtils.getTeamLogoResById(gameAndBet.game.homeTeam.teamId)),
            placeholder = painterResource(NbaUtils.getTeamLogoResById(gameAndBet.game.homeTeam.teamId)),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .constrainAs(homeScoreText) {
                    top.linkTo(homeLogo.bottom, 8.dp)
                    linkTo(homeLogo.start, homeLogo.end)
                },
            text = gameAndBet.game.homeTeam.score.toString(),
            color = color,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .constrainAs(awayTeamText) {
                    top.linkTo(parent.top, 16.dp)
                    linkTo(awayLogo.start, awayLogo.end)
                },
            text = gameAndBet.game.awayTeam.teamTricode,
            color = color,
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
                .data(NbaUtils.getTeamLogoUrlById(gameAndBet.game.awayTeam.teamId))
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            error = painterResource(NbaUtils.getTeamLogoResById(gameAndBet.game.awayTeam.teamId)),
            placeholder = painterResource(NbaUtils.getTeamLogoResById(gameAndBet.game.awayTeam.teamId)),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .constrainAs(awayScoreText) {
                    top.linkTo(awayLogo.bottom, 8.dp)
                    linkTo(awayLogo.start, awayLogo.end)
                },
            text = gameAndBet.game.awayTeam.score.toString(),
            color = color,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .constrainAs(gameStatusText) {
                    if (canBet) {
                        linkTo(homeLogo.top, coinsBtn.top, 24.dp)
                    } else {
                        linkTo(homeLogo.top, awayLogo.bottom)
                    }
                    linkTo(homeLogo.end, awayLogo.start)
                },
            text = if (gameAndBet.game.gameStatus == GameStatusCode.COMING_SOON) {
                gameAndBet.game.gameStatusText.replaceFirst(" ", "\n")
            } else {
                gameAndBet.game.gameStatusText
            }.trim(),
            textAlign = TextAlign.Center,
            color = color,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic
        )
        if (canBet) {
            IconButton(
                modifier = Modifier
                    .constrainAs(coinsBtn) {
                        linkTo(gameStatusText.bottom, homeLogo.bottom)
                        linkTo(homeLogo.end, awayLogo.start)
                    }
                    .size(48.dp)
                    .padding(12.dp),
                onClick = { showBetsDialog = true }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_black_coin),
                    contentDescription = null,
                    tint = color
                )
            }
        }
        if (expandable) {
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
                    colorFilter = ColorFilter.tint(color),
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
                    val isComingSoon = gameAndBet.game.gameStatus == GameStatusCode.COMING_SOON
                    val leaders =
                        if (isComingSoon) gameAndBet.game.teamLeaders else gameAndBet.game.gameLeaders
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
                            awayLeader = awayLeader,
                            color = color
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
                        colorFilter = ColorFilter.tint(color),
                        contentDescription = null
                    )
                }
            }
        }
    }
    if (showBetsDialog) {
        BetDialog(
            userData = userData,
            gameAndBet = gameAndBet,
            onLogin = onLogin,
            onRegister = onRegister,
            onConfirm = onConfirm,
            onDismiss = { showBetsDialog = false }
        )
    }
}

@Composable
private fun LeaderInfo(
    modifier: Modifier = Modifier,
    isGameFinal: Boolean,
    homeLeader: GameLeaders.GameLeader,
    awayLeader: GameLeaders.GameLeader,
    color: Color,
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
            color = color,
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
            color = color,
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
            color = color,
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
                        color = color,
                        fontSize = 12.sp
                    )
                    Text(
                        modifier = Modifier
                            .constrainAs(playerInfoText) {
                                start.linkTo(playerImage.end, 4.dp)
                                linkTo(playerNameText.bottom, playerImage.bottom)
                            },
                        text = player.teamTricode + " | #" + player.jerseyNum + " | " + player.position,
                        color = color,
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
                        color = color,
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
                        color = color,
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
                        color = color,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun LoginDialog(
    onLogin: (account: String, password: String) -> Unit,
    onRegister: (account: String, password: String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var account by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .width(IntrinsicSize.Min)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicTextField(
                modifier = Modifier
                    .padding(top = 24.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                value = account,
                onValueChange = { account = it },
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                textStyle = TextStyle(
                    color = "#de000000".color,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Start
                ),
                decorationBox = { innerTextField ->
                    if (account.isEmpty()) {
                        Text(
                            text = stringResource(R.string.user_login_account_hint),
                            color = "#40000000".color,
                            fontSize = 18.sp
                        )
                    }
                    innerTextField()
                }
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background("#de000000".color)
            )
            BasicTextField(
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                textStyle = TextStyle(
                    color = "#de000000".color,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Start
                ),
                decorationBox = { innerTextField ->
                    if (account.isEmpty()) {
                        Text(
                            text = stringResource(R.string.user_login_password_hint),
                            color = "#40000000".color,
                            fontSize = 18.sp
                        )
                    }
                    innerTextField()
                }
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background("#de000000".color)
            )
            Row {
                Button(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 16.dp, start = 16.dp)
                        .width(120.dp),
                    onClick = {
                        if (account.isBlank() || password.isBlank()) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.user_login_warning),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            onRegister(account, password)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.user_register),
                        color = MaterialTheme.colors.primaryVariant
                    )
                }
                Button(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                        .width(120.dp),
                    onClick = {
                        if (account.isBlank() || password.isBlank()) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.user_login_warning),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            onLogin(account, password)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.user_login),
                        color = MaterialTheme.colors.secondaryVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun AccountInfo(
    modifier: Modifier = Modifier,
    name: String,
    points: Long,
    onLogoutClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Image(
            modifier = Modifier
                .padding(start = 16.dp)
                .size(48.dp),
            painter = painterResource(R.drawable.ic_black_person),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colors.primaryVariant)
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = name,
                fontSize = 16.sp,
                color = MaterialTheme.colors.primaryVariant,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = stringResource(R.string.user_points, points),
                fontSize = 16.sp,
                color = MaterialTheme.colors.primaryVariant
            )
        }
        Row {
            IconButton(onClick = onLogoutClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_black_logout),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primaryVariant
                )
            }
        }
    }
}

@Composable
private fun BetDialog(
    userData: User?,
    gameAndBet: NbaGameAndBet,
    onLogin: (account: String, password: String) -> Unit,
    onRegister: (account: String, password: String) -> Unit,
    onConfirm: (gameId: String, homePoints: Long, awayPoints: Long) -> Unit,
    onDismiss: () -> Unit
) {
    if (userData == null) {
        LoginDialog(
            onLogin = onLogin,
            onRegister = onRegister,
            onDismiss = onDismiss
        )
    } else {
        val context = LocalContext.current
        var homePoints by rememberSaveable { mutableStateOf("") }
        var awayPoints by rememberSaveable { mutableStateOf("") }
        var showWarning by rememberSaveable { mutableStateOf(false) }
        val remainPoints by remember(userData, homePoints, awayPoints) {
            derivedStateOf {
                val points = userData.points ?: 0
                points - (homePoints.toLongOrNull() ?: 0) - (awayPoints.toLongOrNull() ?: 0)
            }
        }

        Dialog(
            onDismissRequest = onDismiss
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colors.secondary)
            ) {
                val (
                    vsText, remainPointText, oddText,
                    homeLogo, awayLogo, homeRecordText, awayRecordText,
                    homeTextFiled, awayTextField, confirmBtn
                ) = createRefs()

                Text(
                    modifier = Modifier
                        .constrainAs(homeRecordText) {
                            top.linkTo(parent.top, 16.dp)
                            linkTo(homeLogo.start, homeLogo.end)
                        },
                    text = "(${gameAndBet.game.homeTeam.wins}/${gameAndBet.game.homeTeam.losses})",
                    color = MaterialTheme.colors.primary,
                    fontSize = 20.sp
                )
                Text(
                    modifier = Modifier
                        .constrainAs(awayRecordText) {
                            top.linkTo(parent.top, 16.dp)
                            linkTo(awayLogo.start, awayLogo.end)
                        },
                    text = "(${gameAndBet.game.awayTeam.wins}/${gameAndBet.game.awayTeam.losses})",
                    color = MaterialTheme.colors.primary,
                    fontSize = 20.sp
                )
                AsyncImage(
                    modifier = Modifier
                        .constrainAs(homeLogo) {
                            top.linkTo(homeRecordText.bottom, 8.dp)
                            start.linkTo(parent.start, 16.dp)
                        }
                        .size(100.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(NbaUtils.getTeamLogoUrlById(gameAndBet.game.homeTeam.teamId))
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    error = painterResource(NbaUtils.getTeamLogoResById(gameAndBet.game.homeTeam.teamId)),
                    placeholder = painterResource(NbaUtils.getTeamLogoResById(gameAndBet.game.homeTeam.teamId)),
                    contentDescription = null
                )
                AsyncImage(
                    modifier = Modifier
                        .constrainAs(awayLogo) {
                            top.linkTo(awayRecordText.bottom, 8.dp)
                            end.linkTo(parent.end, 16.dp)
                        }
                        .size(100.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(NbaUtils.getTeamLogoUrlById(gameAndBet.game.awayTeam.teamId))
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    error = painterResource(NbaUtils.getTeamLogoResById(gameAndBet.game.awayTeam.teamId)),
                    placeholder = painterResource(NbaUtils.getTeamLogoResById(gameAndBet.game.awayTeam.teamId)),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier
                        .constrainAs(vsText) {
                            linkTo(homeLogo.top, awayLogo.bottom)
                            linkTo(homeLogo.end, awayLogo.start, 16.dp, 16.dp)
                        },
                    text = stringResource(R.string.bet_vs),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.primary,
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic
                )
                Text(
                    modifier = Modifier
                        .constrainAs(oddText) {
                            top.linkTo(vsText.bottom)
                            linkTo(homeLogo.end, awayLogo.start, 16.dp, 16.dp)
                        },
                    text = "1:1",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.primary,
                    fontSize = 16.sp
                )
                CustomOutlinedTextField(
                    modifier = Modifier
                        .constrainAs(homeTextFiled) {
                            linkTo(homeLogo.start, homeLogo.end)
                            top.linkTo(homeLogo.bottom, 8.dp)
                            width = Dimension.fillToConstraints
                        }
                        .height(32.dp),
                    value = homePoints,
                    onValueChange = {
                        homePoints = if (it.isEmpty()) {
                            it
                        } else {
                            it.toLongOrNull()?.coerceIn(0, Long.MAX_VALUE)?.toString() ?: homePoints
                        }
                    },
                    textStyle = TextStyle(
                        color = MaterialTheme.colors.primary,
                        textAlign = TextAlign.Center
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    maxLines = 1
                )
                CustomOutlinedTextField(
                    modifier = Modifier
                        .constrainAs(awayTextField) {
                            linkTo(awayLogo.start, awayLogo.end)
                            top.linkTo(awayLogo.bottom, 8.dp)
                            width = Dimension.fillToConstraints
                        }
                        .height(32.dp),
                    value = awayPoints,
                    onValueChange = {
                        awayPoints = if (it.isEmpty()) {
                            it
                        } else {
                            it.toLongOrNull()?.coerceIn(0, Long.MAX_VALUE)?.toString() ?: awayPoints
                        }
                    },
                    textStyle = TextStyle(
                        color = MaterialTheme.colors.primary,
                        textAlign = TextAlign.Center
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    maxLines = 1
                )
                Text(
                    modifier = Modifier
                        .constrainAs(remainPointText) {
                            top.linkTo(homeTextFiled.bottom, 8.dp)
                            linkTo(parent.start, parent.end, 16.dp, 16.dp)
                        },
                    text = stringResource(R.string.bet_remain, remainPoints),
                    color = MaterialTheme.colors.primary,
                    fontSize = 12.sp,
                )
                Text(
                    modifier = Modifier
                        .constrainAs(confirmBtn) {
                            top.linkTo(remainPointText.bottom, 8.dp)
                            end.linkTo(parent.end, 8.dp)
                        }
                        .padding(bottom = 8.dp)
                        .wrapContentSize()
                        .rippleClickable {
                            if (remainPoints < 0) {
                                Toast
                                    .makeText(
                                        context,
                                        context.getString(R.string.bet_not_enough),
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            } else if ((homePoints.toLongOrNull()
                                    ?: 0) <= 0 && (awayPoints.toLongOrNull() ?: 0) <= 0
                            ) {
                                Toast
                                    .makeText(
                                        context,
                                        context.getString(R.string.bet_point_require),
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            } else {
                                showWarning = true
                            }
                        }
                        .padding(10.dp),
                    text = stringResource(R.string.bet_confirm),
                    color = MaterialTheme.colors.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        if (showWarning) {
            AlertDialog(
                onDismissRequest = { showWarning = false },
                title = {
                    Text(
                        text = stringResource(R.string.bet_warning_title),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colors.primary
                    )
                },
                text = {
                    Text(
                        text = stringResource(R.string.bet_warning_text),
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.primary
                    )
                },
                buttons = {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .wrapContentSize()
                                .rippleClickable {
                                    onConfirm(
                                        gameAndBet.game.gameId,
                                        homePoints.toLongOrNull() ?: 0,
                                        awayPoints.toLongOrNull() ?: 0
                                    )
                                    onDismiss()
                                }
                                .padding(10.dp),
                            text = stringResource(R.string.bet_warning_confirm),
                            color = MaterialTheme.colors.primary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .wrapContentSize()
                                .rippleClickable {
                                    showWarning = false
                                }
                                .padding(10.dp),
                            text = stringResource(R.string.bet_warning_cancel),
                            color = MaterialTheme.colors.primary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                shape = RoundedCornerShape(8.dp),
                backgroundColor = MaterialTheme.colors.secondary
            )
        }
    }
}

private fun Double.decimalFormat(radix: Int = 1): String {
    val value = (this * 10.0.pow(radix)).toInt() / 10.0.pow(radix)
    return value.toString()
}