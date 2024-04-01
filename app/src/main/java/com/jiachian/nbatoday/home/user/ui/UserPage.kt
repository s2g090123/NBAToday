package com.jiachian.nbatoday.home.user.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.IconButton
import com.jiachian.nbatoday.common.ui.TeamLogoImage
import com.jiachian.nbatoday.common.ui.theme.NBAColors
import com.jiachian.nbatoday.home.user.ui.event.UserUIEvent
import com.jiachian.nbatoday.main.ui.navigation.NavigationController
import com.jiachian.nbatoday.team.data.model.local.teams.NBATeam
import com.jiachian.nbatoday.testing.testtag.UserTestTag
import com.jiachian.nbatoday.utils.rippleClickable
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserPage(
    navigationController: NavigationController,
    viewModel: UserPageViewModel = koinViewModel(),
) {
    val state = viewModel.state
    Box(modifier = Modifier.fillMaxSize()) {
        if (state.loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colors.secondary
            )
        } else if (!state.login) {
            LoginScreen(onClickLogin = navigationController::showLoginDialog)
        }
        state.user?.let { user ->
            Scaffold(
                backgroundColor = MaterialTheme.colors.primary,
                topBar = {
                    UserTopBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.secondary),
                        name = user.name,
                        points = user.points,
                        onBet = { navigationController.navigateToBet(user.account) },
                        onLogout = { viewModel.onEvent(UserUIEvent.Logout) }
                    )
                }
            ) { padding ->
                ThemeTable(
                    modifier = Modifier
                        .testTag(UserTestTag.UserScreen_ThemesTable)
                        .padding(padding),
                    teams = state.teams,
                    onPalette = { viewModel.onEvent(UserUIEvent.UpdateTheme(it.teamId)) }
                )
            }
        }
    }
}

@Composable
private fun ThemeTable(
    teams: List<NBATeam>,
    onPalette: (NBATeam) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            teams,
            key = { it.teamId }
        ) { team ->
            ThemeCard(
                modifier = Modifier
                    .testTag(UserTestTag.ThemeTable_ThemeCard)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .border(1.dp, Color.White, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(7.dp))
                    .background(MaterialTheme.colors.secondary)
                    .rippleClickable {
                        onPalette(team)
                    }
                    .padding(bottom = 8.dp),
                team = team,
                colors = team.colors,
            )
        }
    }
}

@Composable
private fun LoginScreen(
    onClickLogin: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.user_login_hint),
            fontSize = 18.sp,
            color = MaterialTheme.colors.secondaryVariant,
            fontWeight = FontWeight.Medium
        )
        Button(
            modifier = Modifier
                .testTag(UserTestTag.LoginScreen_Button_Login)
                .padding(top = 8.dp),
            onClick = onClickLogin,
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

@Composable
private fun ThemeCard(
    team: NBATeam,
    colors: NBAColors,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        TeamLogoImage(
            modifier = Modifier
                .padding(top = 8.dp, start = 8.dp)
                .size(48.dp),
            team = team
        )
        Column(modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)) {
            Text(
                text = team.teamName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.primaryVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            ThemePalette(
                modifier = Modifier.padding(top = 8.dp),
                colors = colors,
            )
        }
    }
}

@Composable
private fun ThemePalette(
    colors: NBAColors,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ColorCircle(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f),
            color = colors.primary
        )
        ColorCircle(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f),
            color = colors.secondary
        )
        ColorCircle(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f),
            color = colors.extra1
        )
        ColorCircle(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f),
            color = colors.extra2
        )
    }
}

@Composable
private fun UserTopBar(
    name: String,
    points: Long,
    onBet: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
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
                modifier = Modifier.testTag(UserTestTag.UserTopBar_Text_AccountName),
                text = name,
                fontSize = 16.sp,
                color = MaterialTheme.colors.primaryVariant,
                fontWeight = FontWeight.Medium
            )
            Text(
                modifier = Modifier.testTag(UserTestTag.UserTopBar_Text_Credits),
                text = stringResource(R.string.user_points, points),
                fontSize = 16.sp,
                color = MaterialTheme.colors.primaryVariant
            )
        }
        IconButton(
            modifier = Modifier.testTag(UserTestTag.UserTopBar_Button_Bet),
            drawableRes = R.drawable.ic_black_coin,
            tint = MaterialTheme.colors.primaryVariant,
            onClick = onBet,
        )
        IconButton(
            modifier = Modifier
                .testTag(UserTestTag.UserTopBar_Button_Logout)
                .padding(start = 8.dp),
            drawableRes = R.drawable.ic_black_logout,
            tint = MaterialTheme.colors.primaryVariant,
            onClick = onLogout,
        )
    }
}

@Composable
private fun ColorCircle(
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.then(
            Modifier
                .size(18.dp)
                .border(1.dp, Color.White, CircleShape)
                .shadow(4.dp, CircleShape)
                .background(color)
        )
    )
}
