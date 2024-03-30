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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.jiachian.nbatoday.compose.screen.calendar.event.CalendarUIEvent
import com.jiachian.nbatoday.compose.screen.calendar.models.CalendarDate
import com.jiachian.nbatoday.compose.screen.calendar.state.CalendarDatesState
import com.jiachian.nbatoday.compose.screen.calendar.state.CalendarTopBarState
import com.jiachian.nbatoday.compose.screen.card.GameCard
import com.jiachian.nbatoday.compose.screen.card.models.GameCardData
import com.jiachian.nbatoday.compose.widget.IconButton
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.testing.testtag.CalendarTestTag
import com.jiachian.nbatoday.utils.rippleClickable
import com.jiachian.nbatoday.utils.slideSpec
import java.util.Date
import org.koin.androidx.compose.koinViewModel

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = koinViewModel(),
    navigationController: NavigationController,
) {
    val topBarState = viewModel.topBarState
    val datesState = viewModel.datesState
    val gamesState = viewModel.gamesState
    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
        topBar = {
            CalendarTopBar(
                state = topBarState,
                onEvent = viewModel::onEvent,
                onClose = navigationController::back,
            )
        }
    ) { padding ->
        if (datesState.loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .testTag(CalendarTestTag.CalendarContent_LoadingScreen_Calendar)
                    .fillMaxSize(),
                color = MaterialTheme.colors.secondary,
            )
        } else {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                columns = GridCells.Fixed(DaysPerWeek),
            ) {
                dateBoxes(
                    state = datesState,
                    selectDate = { viewModel.onEvent(CalendarUIEvent.SelectDate(it)) }
                )
                if (gamesState.visible) {
                    if (gamesState.loading) {
                        item(span = { GridItemSpan(DaysPerWeek) }) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .testTag(CalendarTestTag.CalendarContent_LoadingScreen_Games)
                                    .padding(top = 24.dp),
                                color = MaterialTheme.colors.secondary,
                            )
                        }
                    } else {
                        calendarGameCards(
                            games = gamesState.games,
                            onClickGame = { game ->
                                if (game.gamePlayed) {
                                    navigationController.navigateToBoxScore(game.gameId)
                                } else {
                                    navigationController.navigateToTeam(game.homeTeamId)
                                }
                            },
                            showLoginDialog = navigationController::showLoginDialog,
                            showBetDialog = navigationController::showBetDialog,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarTopBar(
    modifier: Modifier = Modifier,
    state: CalendarTopBarState,
    onEvent: (CalendarUIEvent) -> Unit,
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
            index = state.index,
            dateString = state.dateString,
            hasPrev = state.hasPrevious,
            hasNext = state.hasNext,
            onEvent = onEvent
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
    index: Int,
    dateString: String,
    hasPrev: Boolean,
    hasNext: Boolean,
    onEvent: (CalendarUIEvent) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier.testTag(CalendarTestTag.CalendarNavigationBar_CalendarArrowButton_Last),
            enabled = hasPrev,
            drawableRes = R.drawable.ic_black_left_arrow,
            tint = MaterialTheme.colors.secondaryVariant.copy(if (hasPrev) 1f else Transparency25),
            onClick = { onEvent(CalendarUIEvent.PrevMonth) }
        )
        AnimatedContent(
            modifier = Modifier.weight(1f),
            targetState = index,
            transitionSpec = { slideSpec(targetState > initialState) }
        ) {
            Text(
                modifier = Modifier.testTag(CalendarTestTag.CalendarNavigationBar_Text_Date),
                text = dateString,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.secondaryVariant
            )
        }
        IconButton(
            modifier = Modifier.testTag(CalendarTestTag.CalendarNavigationBar_CalendarArrowButton_Next),
            enabled = hasNext,
            drawableRes = R.drawable.ic_black_right_arrow,
            tint = MaterialTheme.colors.secondaryVariant.copy(if (hasNext) 1f else Transparency25),
            onClick = { onEvent(CalendarUIEvent.NextMonth) }
        )
    }
}

private fun LazyGridScope.dateBoxes(
    state: CalendarDatesState,
    selectDate: (Date) -> Unit,
) = items(
    items = state.calendarDates,
    span = { GridItemSpan(1) }
) { calendarDate ->
    DateBox(
        calendarDate = calendarDate,
        selected = calendarDate.date == state.selectedDate,
        onClick = selectDate
    )
}

private fun LazyGridScope.calendarGameCards(
    games: List<GameCardData>,
    onClickGame: (game: Game) -> Unit,
    showLoginDialog: () -> Unit,
    showBetDialog: (String) -> Unit,
) = itemsIndexed(
    items = games,
    key = { _, item -> item.data.game.gameId },
    span = { _, _ -> GridItemSpan(DaysPerWeek) }
) { index, game ->
    GameCard(
        modifier = Modifier
            .testTag(CalendarTestTag.CalendarGameCard)
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
            .rippleClickable { onClickGame(game.data.game) }
            .padding(bottom = 8.dp),
        data = game,
        expandable = false,
        color = MaterialTheme.colors.primary,
        showLoginDialog = showLoginDialog,
        showBetDialog = showBetDialog,
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
