package com.jiachian.nbatoday.compose.screen.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.widget.FocusableColumn
import com.jiachian.nbatoday.compose.widget.RefreshingScreen
import com.jiachian.nbatoday.utils.noRippleClickable
import com.jiachian.nbatoday.utils.rippleClickable

@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    val isRefreshing by viewModel.isProgressing.collectAsState()
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
    val homePage by viewModel.homePage.collectAsState()
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

    LaunchedEffect(homePage) {
        lazyState.animateScrollToItem(
            when (homePage) {
                HomePage.SCHEDULE -> 0
                HomePage.STANDING -> 1
                HomePage.USER -> 2
            }
        )
    }
}

@Composable
private fun HomeBottom(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {
    val selectIndex by viewModel.homePage.collectAsState()
    val scheduleScale by animateFloatAsState(
        targetValue = if (selectIndex == HomePage.SCHEDULE) 1.2f else 1f,
        label = "ScheduleScale"
    )
    val standingScale by animateFloatAsState(
        targetValue = if (selectIndex == HomePage.STANDING) 1.2f else 1f,
        label = "StandingScale"
    )
    val userScale by animateFloatAsState(
        targetValue = if (selectIndex == HomePage.USER) 1.2f else 1f,
        label = "UserScale"
    )
    Row(modifier = modifier) {
        NavigationButton(
            modifier = Modifier
                .testTag("HomeBottom_Btn_Schedule")
                .weight(1f)
                .fillMaxHeight()
                .scale(scheduleScale)
                .rippleClickable { viewModel.updateHomePage(HomePage.SCHEDULE) }
                .padding(horizontal = 12.dp, vertical = 4.dp),
            page = HomePage.SCHEDULE
        )
        NavigationButton(
            modifier = Modifier
                .testTag("HomeBottom_Btn_Standing")
                .weight(1f)
                .fillMaxHeight()
                .scale(standingScale)
                .rippleClickable { viewModel.updateHomePage(HomePage.STANDING) }
                .padding(horizontal = 12.dp, vertical = 4.dp),
            page = HomePage.STANDING
        )
        NavigationButton(
            modifier = Modifier
                .testTag("HomeBottom_Btn_User")
                .weight(1f)
                .fillMaxHeight()
                .scale(userScale)
                .rippleClickable { viewModel.updateHomePage(HomePage.USER) }
                .padding(horizontal = 12.dp, vertical = 4.dp),
            page = HomePage.USER
        )
    }
}

@Composable
private fun NavigationButton(
    modifier: Modifier = Modifier,
    page: HomePage
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(
                when (page) {
                    HomePage.SCHEDULE -> R.drawable.ic_black_schedule
                    HomePage.STANDING -> R.drawable.ic_black_ranking
                    HomePage.USER -> R.drawable.ic_black_person
                }
            ),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
        )
        Text(
            text = stringResource(
                when (page) {
                    HomePage.SCHEDULE -> R.string.home_bottom_schedule
                    HomePage.STANDING -> R.string.home_bottom_standings
                    HomePage.USER -> R.string.home_bottom_user
                }
            ),
            color = MaterialTheme.colors.primary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
