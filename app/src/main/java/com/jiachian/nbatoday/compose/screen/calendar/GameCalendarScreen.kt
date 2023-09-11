package com.jiachian.nbatoday.compose.screen.calendar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.accompanist.flowlayout.FlowRow
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.home.GameStatusCard2
import com.jiachian.nbatoday.compose.widget.RefreshingScreen
import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import com.jiachian.nbatoday.utils.NbaUtils
import com.jiachian.nbatoday.utils.noRippleClickable
import com.jiachian.nbatoday.utils.rippleClickable

@Composable
fun GameCalendarScreen(
    viewModel: GameCalendarViewModel,
    onClose: () -> Unit
) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
            .verticalScroll(rememberScrollState())
            .noRippleClickable { }
    ) {
        CalendarTopBar(
            modifier = Modifier.fillMaxWidth(),
            viewModel = viewModel,
            onClose = onClose
        )
        CalendarContent(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxSize(),
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CalendarTopBar(
    modifier: Modifier = Modifier,
    viewModel: GameCalendarViewModel,
    onClose: () -> Unit
) {
    val hasPreviousMonth by viewModel.hasPreviousMonth.collectAsState()
    val hasNextMonth by viewModel.hasNextMonth.collectAsState()
    val currentMonthString by viewModel.currentDateString.collectAsState()

    Column(modifier = modifier) {
        IconButton(
            modifier = Modifier
                .testTag("CalendarTopBar_Btn_Close")
                .padding(top = 8.dp, start = 8.dp),
            onClick = onClose
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_black_close),
                contentDescription = null,
                tint = MaterialTheme.colors.secondary
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(MaterialTheme.colors.secondary)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.testTag("CalendarTopBar_Btn_Prev"),
                enabled = hasPreviousMonth,
                onClick = viewModel::previousMonth
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_black_left_arrow),
                    contentDescription = null,
                    tint = MaterialTheme.colors.secondaryVariant.copy(if (hasPreviousMonth) 1f else 0.25f)
                )
            }
            AnimatedContent(
                modifier = Modifier.weight(1f),
                targetState = currentMonthString,
                transitionSpec = {
                    if (targetState.first > initialState.first) {
                        slideInHorizontally { width -> width } + fadeIn() with
                            slideOutHorizontally { width -> -width } + fadeOut()
                    } else {
                        slideInHorizontally { width -> -width } + fadeIn() with
                            slideOutHorizontally { width -> width } + fadeOut()
                    }.using(
                        SizeTransform(clip = false)
                    )
                }
            ) { text ->
                Text(
                    modifier = Modifier.testTag("CalendarTopBar_Text_Date"),
                    text = text.second,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondaryVariant
                )
            }
            IconButton(
                modifier = Modifier.testTag("CalendarTopBar_Btn_Next"),
                enabled = hasNextMonth,
                onClick = viewModel::nextMonth
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_black_right_arrow),
                    contentDescription = null,
                    tint = MaterialTheme.colors.secondaryVariant.copy(if (hasNextMonth) 1f else 0.25f)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CalendarContent(
    modifier: Modifier = Modifier,
    viewModel: GameCalendarViewModel
) {
    val calendarList by viewModel.calendarData.collectAsState()
    val gameList by viewModel.gamesData.collectAsState()
    val selectDateData by viewModel.selectDateData.collectAsState()
    val selectGames by viewModel.selectGames.collectAsState()
    val isLoadingGames by viewModel.isLoadingGames.collectAsState()

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(7) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = when (it) {
                        1 -> "MON"
                        2 -> "TUE"
                        3 -> "WED"
                        4 -> "THU"
                        5 -> "FRI"
                        6 -> "SAT"
                        else -> "SUN"
                    },
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .testTag("CalendarContent_LVG_Games")
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .heightIn(max = LocalConfiguration.current.screenHeightDp.dp),
                columns = GridCells.Fixed(7)
            ) {
                itemsIndexed(calendarList) { index, dateData ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .border(
                                BorderStroke(
                                    2.dp,
                                    if (dateData == selectDateData) {
                                        MaterialTheme.colors.secondary
                                    } else {
                                        MaterialTheme.colors.secondaryVariant
                                    }
                                )
                            )
                            .rippleClickable {
                                viewModel.selectDate(dateData.date)
                            }
                    ) {
                        Text(
                            modifier = Modifier
                                .testTag("CalendarContent_Text_Date")
                                .align(Alignment.TopEnd)
                                .padding(4.dp),
                            text = dateData.day.toString(),
                            color = if (selectDateData?.day == dateData.day && dateData.isCurrentMonth) {
                                MaterialTheme.colors.secondary
                            } else {
                                MaterialTheme.colors.secondaryVariant.copy(if (dateData.isCurrentMonth) 1f else 0.25f)
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        if (!isLoadingGames) {
                            FlowRow(
                                modifier = Modifier
                                    .testTag("CalendarContent_FlowRow_Games")
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .align(Alignment.BottomStart)
                                    .padding(2.dp)
                            ) {
                                gameList.getOrNull(index)?.let { games ->
                                    games.forEach {
                                        AsyncImage(
                                            modifier = Modifier
                                                .testTag("CalendarContent_Image_Team")
                                                .size(12.dp),
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(NbaUtils.getTeamSmallLogoUrlById(it.game.homeTeam.teamId))
                                                .decoderFactory(SvgDecoder.Factory())
                                                .build(),
                                            error = painterResource(NbaUtils.getTeamLogoResById(it.game.homeTeam.teamId)),
                                            placeholder = painterResource(
                                                NbaUtils.getTeamLogoResById(
                                                    it.game.homeTeam.teamId
                                                )
                                            ),
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        selectGames?.let { games ->
            CalendarGames(
                modifier = Modifier
                    .testTag("CalendarContent_CalendarGames")
                    .fillMaxWidth()
                    .heightIn(max = LocalConfiguration.current.screenHeightDp.dp),
                viewModel = viewModel,
                games = games
            )
        }
    }
}

@Composable
private fun CalendarGames(
    modifier: Modifier = Modifier,
    viewModel: GameCalendarViewModel,
    games: List<NbaGameAndBet>
) {
    val user by viewModel.user.collectAsState()
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(games) { index, game ->
            GameStatusCard2(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = if (index == 0) 16.dp else 8.dp,
                        bottom = if (index == games.size - 1) 16.dp else 0.dp
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
                    }
                    .padding(bottom = 8.dp),
                gameAndBet = game,
                userData = user,
                expandable = false,
                color = MaterialTheme.colors.primary,
                onLogin = viewModel::login,
                onRegister = viewModel::register,
                onConfirm = viewModel::bet
            )
        }
    }
}
