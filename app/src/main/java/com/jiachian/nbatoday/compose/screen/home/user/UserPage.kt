package com.jiachian.nbatoday.compose.screen.home.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.jiachian.nbatoday.compose.screen.account.LoginDialog
import com.jiachian.nbatoday.compose.theme.NBAColors
import com.jiachian.nbatoday.compose.widget.IconButton
import com.jiachian.nbatoday.compose.widget.LoadingScreen
import com.jiachian.nbatoday.compose.widget.TeamLogoImage
import com.jiachian.nbatoday.compose.widget.UIStateScreen
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.user.User
import com.jiachian.nbatoday.testing.testtag.UserTestTag
import com.jiachian.nbatoday.utils.rippleClickable
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserPage(
    modifier: Modifier = Modifier,
    viewModel: UserPageViewModel = koinViewModel(),
    navigateToBet: (account: String) -> Unit,
) {
    val userState by viewModel.userState.collectAsState()
    UIStateScreen(
        state = userState,
        loading = {
            LoadingScreen(
                modifier = modifier,
                color = MaterialTheme.colors.secondary
            )
        },
        ifNull = {
            LoginScreen(
                modifier = Modifier
                    .testTag(UserTestTag.UserPage_LoginScreen)
                    .then(modifier),
                onLogin = viewModel::login,
                onRegister = viewModel::register
            )
        }
    ) { user ->
        UserScreen(
            modifier = modifier,
            viewModel = viewModel,
            user = user,
            onBetClick = { navigateToBet(user.account) }
        )
    }
}

@Composable
private fun UserScreen(
    modifier: Modifier = Modifier,
    viewModel: UserPageViewModel,
    user: User,
    onBetClick: () -> Unit,
) {
    Column(modifier = modifier) {
        UserTopBar(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.secondary),
            name = user.name,
            points = user.points,
            onBet = onBetClick,
            onLogout = viewModel::logout
        )
        ThemeTable(
            modifier = Modifier.testTag(UserTestTag.UserScreen_ThemesTable),
            teams = viewModel.teams,
            onPalette = viewModel::updateTheme
        )
    }
}

@Composable
private fun ThemeTable(
    modifier: Modifier = Modifier,
    teams: List<NBATeam>,
    onPalette: (NBATeam) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(teams) { team ->
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
    modifier: Modifier = Modifier,
    onLogin: (String, String) -> Unit,
    onRegister: (String, String) -> Unit
) {
    var dialogVisible by remember { mutableStateOf(false) }
    Column(
        modifier = modifier,
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
            onClick = { dialogVisible = true },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.secondary
            )
        ) {
            Text(
                text = stringResource(R.string.user_login),
                color = MaterialTheme.colors.secondaryVariant
            )
        }
        if (dialogVisible) {
            LoginDialog(
                onLogin = { account, password ->
                    onLogin(account, password)
                    dialogVisible = false
                },
                onRegister = { account, password ->
                    onRegister(account, password)
                    dialogVisible = false
                },
                onDismiss = { dialogVisible = false }
            )
        }
    }
}

@Composable
private fun ThemeCard(
    modifier: Modifier = Modifier,
    team: NBATeam,
    colors: NBAColors,
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
    modifier: Modifier = Modifier,
    colors: NBAColors,
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
    modifier: Modifier = Modifier,
    name: String,
    points: Long,
    onBet: () -> Unit,
    onLogout: () -> Unit
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
    modifier: Modifier = Modifier,
    color: Color
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
