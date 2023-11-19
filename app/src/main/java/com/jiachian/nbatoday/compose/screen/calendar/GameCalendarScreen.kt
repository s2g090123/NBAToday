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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.accompanist.flowlayout.FlowRow
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.card.GameStatusCard
import com.jiachian.nbatoday.compose.widget.DisableOverscroll
import com.jiachian.nbatoday.compose.widget.RefreshingScreen
import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import com.jiachian.nbatoday.utils.NbaUtils
import com.jiachian.nbatoday.utils.noRippleClickable
import com.jiachian.nbatoday.utils.rippleClickable
import java.util.Date

@Composable
fun GameCalendarScreen(
    viewModel: GameCalendarViewModel,
    onClose: () -> Unit
) {
    val isRefreshing by viewModel.isProgressing.collectAsState()

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
    val currentDate by viewModel.currentDateString.collectAsState()

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
        CalendarNavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(MaterialTheme.colors.secondary)
                .padding(8.dp),
            currentDate = currentDate,
            hasPreviousMonth = hasPreviousMonth,
            hasNextMonth = hasNextMonth,
            onClickPrevious = viewModel::previousMonth,
            onClickNext = viewModel::nextMonth
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CalendarNavigationBar(
    modifier: Modifier = Modifier,
    currentDate: Pair<Int, String>,
    hasPreviousMonth: Boolean,
    hasNextMonth: Boolean,
    onClickPrevious: () -> Unit,
    onClickNext: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CalendarArrowButton(
            modifier = Modifier.testTag("CalendarTopBar_Btn_Prev"),
            enabled = hasPreviousMonth,
            isLeft = true,
            onClick = onClickPrevious
        )
        AnimatedContent(
            modifier = Modifier.weight(1f),
            targetState = currentDate,
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
        CalendarArrowButton(
            modifier = Modifier.testTag("CalendarTopBar_Btn_Next"),
            enabled = hasNextMonth,
            isLeft = false,
            onClick = onClickNext
        )
    }
}

@Composable
private fun CalendarArrowButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    isLeft: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(if (isLeft) R.drawable.ic_black_left_arrow else R.drawable.ic_black_right_arrow),
            contentDescription = null,
            tint = MaterialTheme.colors.secondaryVariant.copy(if (enabled) 1f else 0.25f)
        )
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
        DayAbbrTextRow(
            modifier = Modifier.fillMaxWidth()
        )
        CalendarTable(
            modifier = Modifier
                .testTag("CalendarContent_LVG_Games")
                .padding(top = 8.dp)
                .fillMaxWidth()
                .heightIn(max = LocalConfiguration.current.screenHeightDp.dp),
            calendarList = calendarList,
            gameList = gameList,
            selectDate = selectDateData,
            isLoading = isLoadingGames,
            onSelectDate = viewModel::selectDate
        )
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
private fun CalendarTable(
    modifier: Modifier = Modifier,
    calendarList: List<CalendarData>,
    gameList: List<List<NbaGameAndBet>>,
    selectDate: CalendarData?,
    isLoading: Boolean,
    onSelectDate: (Date) -> Unit
) {
    DisableOverscroll {
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(7)
        ) {
            itemsIndexed(calendarList) { index, dateData ->
                val games = gameList.getOrNull(index)
                if (games != null) {
                    DateBox(
                        dateData = dateData,
                        games = games,
                        isSelected = dateData == selectDate,
                        isLoadingGames = isLoading,
                        onClick = onSelectDate
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarGames(
    modifier: Modifier = Modifier,
    viewModel: GameCalendarViewModel,
    games: List<NbaGameAndBet>
) {
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(games) { index, game ->
            val cardViewModel = remember(game) {
                viewModel.createGameStatusCardViewModel(game)
            }
            GameStatusCard(
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
                            viewModel.openTeamStats(game.game.homeTeam.team)
                        } else {
                            viewModel.openGameBoxScore(game.game)
                        }
                    }
                    .padding(bottom = 8.dp),
                viewModel = cardViewModel,
                expandable = false,
                color = MaterialTheme.colors.primary,
            )
        }
    }
}

@Composable
private fun DayAbbrTextRow(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        repeat(7) {
            Text(
                modifier = Modifier.weight(1f),
                text = when (it) {
                    1 -> stringResource(R.string.day_monday_abbr)
                    2 -> stringResource(R.string.day_tuesday_abbr)
                    3 -> stringResource(R.string.day_wednesday_abbr)
                    4 -> stringResource(R.string.day_thursday_abbr)
                    5 -> stringResource(R.string.day_friday_abbr)
                    6 -> stringResource(R.string.day_saturday_abbr)
                    else -> stringResource(R.string.day_sunday_abbr)
                },
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun DateBox(
    dateData: CalendarData,
    games: List<NbaGameAndBet>,
    isSelected: Boolean,
    isLoadingGames: Boolean,
    onClick: (Date) -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .border(
                BorderStroke(
                    2.dp,
                    if (isSelected) {
                        MaterialTheme.colors.secondary
                    } else {
                        MaterialTheme.colors.secondaryVariant
                    }
                )
            )
            .rippleClickable {
                onClick(dateData.date)
            }
    ) {
        Text(
            modifier = Modifier
                .testTag("CalendarContent_Text_Date")
                .align(Alignment.TopEnd)
                .padding(4.dp),
            text = dateData.day.toString(),
            color = if (isSelected && dateData.isCurrentMonth) {
                MaterialTheme.colors.secondary
            } else {
                MaterialTheme.colors.secondaryVariant.copy(if (dateData.isCurrentMonth) 1f else 0.25f)
            },
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        if (!isLoadingGames) {
            TeamIconThumbnailsRow(
                modifier = Modifier
                    .testTag("CalendarContent_FlowRow_Games")
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.BottomStart)
                    .padding(2.dp),
                games = games
            )
        }
    }
}

@Composable
private fun TeamIconThumbnailsRow(
    modifier: Modifier = Modifier,
    games: List<NbaGameAndBet>
) {
    FlowRow(modifier = modifier) {
        games.forEach {
            AsyncImage(
                modifier = Modifier
                    .testTag("CalendarContent_Image_Team")
                    .size(12.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(NbaUtils.getTeamSmallLogoUrlById(it.game.homeTeam.team.teamId))
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
                error = painterResource(it.game.homeTeam.team.logoRes),
                contentDescription = null
            )
        }
    }
}
