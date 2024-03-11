package com.jiachian.nbatoday.compose.screen.calendar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.DaysPerWeek
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.Transparency25
import com.jiachian.nbatoday.compose.screen.calendar.models.CalendarDate
import com.jiachian.nbatoday.compose.screen.card.GameCard
import com.jiachian.nbatoday.compose.screen.card.GameCardViewModel
import com.jiachian.nbatoday.compose.widget.IconButton
import com.jiachian.nbatoday.compose.widget.LoadingScreen
import com.jiachian.nbatoday.compose.widget.UIStateScreen
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.testing.testtag.CalendarTestTag
import com.jiachian.nbatoday.utils.rippleClickable
import com.jiachian.nbatoday.utils.slideSpec
import java.util.Date
import org.koin.androidx.compose.koinViewModel

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = koinViewModel(),
    navigateToBoxScore: (gameId: String) -> Unit,
    navigateToTeam: (teamId: Int) -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
    ) {
        CalendarTopBar(
            modifier = Modifier.fillMaxWidth(),
            viewModel = viewModel,
            onClose = onBack,
        )
        CalendarContent(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxSize(),
            viewModel = viewModel,
            onClickGame = { game ->
                if (game.gamePlayed) {
                    navigateToBoxScore(game.gameId)
                } else {
                    navigateToTeam(game.homeTeamId)
                }
            },
        )
    }
}

@Composable
private fun CalendarTopBar(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel,
    onClose: () -> Unit
) {
    Column(modifier = modifier) {
        IconButton(
            modifier = Modifier
                .testTag(CalendarTestTag.CalendarTopBar_Btn_Close)
                .padding(top = 8.dp, start = 8.dp),
            drawableRes = R.drawable.ic_black_close,
            tint = MaterialTheme.colors.secondary,
            onClick = onClose
        )
        CalendarNavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.secondary),
            viewModel = viewModel
        )
        DayAbbrTextRow(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CalendarNavigationBar(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel,
) {
    val numberAndDateString by viewModel.numberAndDateString.collectAsState()
    val hasLastMonth by viewModel.hasLastMonth.collectAsState()
    val hasNextMonth by viewModel.hasNextMonth.collectAsState()
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CalendarArrowButton(
            modifier = Modifier.testTag(CalendarTestTag.CalendarNavigationBar_CalendarArrowButton_Last),
            enabled = hasLastMonth,
            isLeft = true,
            onClick = viewModel::lastMonth
        )
        AnimatedContent(
            modifier = Modifier.weight(1f),
            targetState = numberAndDateString,
            transitionSpec = { slideSpec(targetState.first > initialState.first) }
        ) { numberAndDateString ->
            Text(
                modifier = Modifier.testTag(CalendarTestTag.CalendarNavigationBar_Text_Date),
                text = numberAndDateString.second,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.secondaryVariant
            )
        }
        CalendarArrowButton(
            modifier = Modifier.testTag(CalendarTestTag.CalendarNavigationBar_CalendarArrowButton_Next),
            enabled = hasNextMonth,
            isLeft = false,
            onClick = viewModel::nextMonth
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
        drawableRes = if (isLeft) R.drawable.ic_black_left_arrow else R.drawable.ic_black_right_arrow,
        tint = MaterialTheme.colors.secondaryVariant.copy(if (enabled) 1f else Transparency25),
        onClick = onClick
    )
}

@Composable
private fun CalendarContent(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel,
    onClickGame: (game: Game) -> Unit,
) {
    val selectedGames by viewModel.selectedGames.collectAsState()
    val selectedGamesVisible by viewModel.selectedGamesVisible.collectAsState()
    val loadingGames by viewModel.loadingGames.collectAsState()
    val calendarDatesState by viewModel.calendarDatesState.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    UIStateScreen(
        state = calendarDatesState,
        loading = {
            LoadingScreen(
                modifier = Modifier
                    .testTag(CalendarTestTag.CalendarContent_LoadingScreen_Calendar)
                    .then(modifier),
                color = MaterialTheme.colors.secondary,
            )
        },
        ifNull = null
    ) { calendarDates ->
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(DaysPerWeek),
        ) {
            dateBoxes(
                viewModel = viewModel,
                calendarDates = calendarDates,
                selectedDate = selectedDate
            )
            if (selectedGamesVisible) {
                if (loadingGames) {
                    item(span = { GridItemSpan(DaysPerWeek) }) {
                        LoadingScreen(
                            modifier = Modifier
                                .testTag(CalendarTestTag.CalendarContent_LoadingScreen_Games)
                                .padding(top = 24.dp),
                            color = MaterialTheme.colors.secondary,
                        )
                    }
                } else {
                    calendarGameCards(
                        viewModel = viewModel,
                        games = selectedGames,
                        onClickGame = onClickGame,
                    )
                }
            }
        }
    }
}

private fun LazyGridScope.dateBoxes(
    viewModel: CalendarViewModel,
    calendarDates: List<CalendarDate>,
    selectedDate: Date,
) = items(
    items = calendarDates,
    span = { GridItemSpan(1) }
) { calendarDate ->
    DateBox(
        calendarDate = calendarDate,
        selected = calendarDate.date == selectedDate,
        onClick = viewModel::selectDate
    )
}

private fun LazyGridScope.calendarGameCards(
    viewModel: CalendarViewModel,
    games: List<GameAndBets>,
    onClickGame: (game: Game) -> Unit,
) = itemsIndexed(
    items = games,
    span = { _, _ -> GridItemSpan(DaysPerWeek) }
) { index, game ->
    CalendarGameCard(
        modifier = Modifier
            .testTag(CalendarTestTag.CalendarGameCard)
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = if (index == 0) 16.dp else 8.dp,
                bottom = if (index == games.size - 1) 16.dp else 0.dp
            ),
        viewModel = viewModel.getGameCardViewModel(game),
        onClick = { onClickGame(game.game) }
    )
}

@Composable
private fun CalendarGameCard(
    modifier: Modifier = Modifier,
    viewModel: GameCardViewModel,
    onClick: () -> Unit,
) {
    GameCard(
        modifier = modifier.then(
            Modifier
                .clip(RoundedCornerShape(16.dp))
                .shadow(8.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .background(MaterialTheme.colors.secondary)
                .rippleClickable { onClick() }
                .padding(bottom = 8.dp)
        ),
        viewModel = viewModel,
        expandable = false,
        color = MaterialTheme.colors.primary,
    )
}

@Composable
private fun DayAbbrTextRow(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        repeat(DaysPerWeek) {
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
    calendarDate: CalendarDate,
    selected: Boolean,
    onClick: (Date) -> Unit
) {
    Box(
        modifier = Modifier
            .testTag(CalendarTestTag.DateBox)
            .aspectRatio(1f)
            .border(
                BorderStroke(
                    2.dp,
                    if (selected) MaterialTheme.colors.secondary else MaterialTheme.colors.secondaryVariant
                )
            )
            .rippleClickable { onClick(calendarDate.date) }
    ) {
        Text(
            modifier = Modifier
                .testTag(CalendarTestTag.DateBox_Text_Date)
                .align(Alignment.TopEnd)
                .padding(4.dp),
            text = calendarDate.day.toString(),
            color = if (selected && calendarDate.currentMonth) {
                MaterialTheme.colors.secondary
            } else {
                MaterialTheme.colors.secondaryVariant.copy(if (calendarDate.currentMonth) 1f else Transparency25)
            },
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
