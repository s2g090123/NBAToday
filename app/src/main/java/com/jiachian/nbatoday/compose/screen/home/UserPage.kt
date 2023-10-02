package com.jiachian.nbatoday.compose.screen.home

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
import com.jiachian.nbatoday.compose.widget.IconButton
import com.jiachian.nbatoday.compose.widget.TeamLogoImage
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.remote.user.User
import com.jiachian.nbatoday.utils.getOrNA
import com.jiachian.nbatoday.utils.getOrZero
import com.jiachian.nbatoday.utils.isPhone
import com.jiachian.nbatoday.utils.isPortrait
import com.jiachian.nbatoday.utils.rippleClickable

@Composable
fun UserPage(
    modifier: Modifier,
    viewModel: HomeViewModel
) {
    val user by viewModel.user.collectAsState()
    if (user == null) {
        RequireLoginScreen(
            modifier = modifier,
            login = viewModel::login,
            register = viewModel::register
        )
    } else {
        user?.let {
            UserScreen(
                modifier = modifier,
                user = it,
                teams = viewModel.nbaTeams,
                openBetScreen = viewModel::openBetScreen,
                logout = viewModel::logout,
                updateTheme = viewModel::updateTheme
            )
        }
    }
}

@Composable
private fun UserScreen(
    modifier: Modifier = Modifier,
    user: User,
    teams: List<NBATeam>,
    openBetScreen: () -> Unit,
    logout: () -> Unit,
    updateTheme: (NBATeam) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        UserTopBar(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.secondary),
            name = user.name.getOrNA(),
            points = user.points.getOrZero(),
            onRewardClick = openBetScreen,
            onLogoutClick = logout
        )
        ThemesTable(
            modifier = Modifier
                .testTag("UserPage_LVG_Palette")
                .fillMaxSize(),
            teams = teams,
            onTeamClick = updateTheme
        )
    }
}

@Composable
private fun ThemesTable(
    modifier: Modifier = Modifier,
    teams: List<NBATeam>,
    onTeamClick: (NBATeam) -> Unit
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
        items(teams) { team ->
            ThemeCard(
                modifier = Modifier
                    .testTag("UserPage_ThemeCard")
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .border(1.dp, Color.White, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(7.dp))
                    .background(MaterialTheme.colors.secondary)
                    .rippleClickable {
                        onTeamClick(team)
                    }
                    .padding(bottom = 8.dp),
                team = team,
                firstColor = team.colors.primary,
                secondColor = team.colors.secondary,
                thirdColor = team.colors.extra1,
                forthColor = team.colors.extra2
            )
        }
    }
}

@Composable
private fun RequireLoginScreen(
    modifier: Modifier = Modifier,
    login: (String, String) -> Unit,
    register: (String, String) -> Unit
) {
    var showLoginDialog by remember { mutableStateOf(false) }
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
                .testTag("UserPage_Btn_Login")
                .padding(top = 8.dp),
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
        if (showLoginDialog) {
            LoginDialog(
                onLogin = { account, password ->
                    login(account, password)
                    showLoginDialog = false
                },
                onRegister = { account, password ->
                    register(account, password)
                    showLoginDialog = false
                },
                onDismiss = { showLoginDialog = false }
            )
        }
    }
}

@Composable
private fun ThemeCard(
    modifier: Modifier = Modifier,
    team: NBATeam,
    firstColor: Color,
    secondColor: Color,
    thirdColor: Color,
    forthColor: Color
) {
    Row(modifier = modifier) {
        TeamLogoImage(
            modifier = Modifier
                .padding(top = 8.dp, start = 8.dp)
                .size(48.dp),
            team = team
        )
        Column(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Text(
                modifier = Modifier.testTag("ThemeCard_Text_Name"),
                text = team.teamName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.primaryVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            ThemeColorPreviewRow(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                firstColor = firstColor,
                secondColor = secondColor,
                thirdColor = thirdColor,
                forthColor = forthColor
            )
        }
    }
}

@Composable
private fun ThemeColorPreviewRow(
    modifier: Modifier = Modifier,
    firstColor: Color,
    secondColor: Color,
    thirdColor: Color,
    forthColor: Color
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ThemeColorPreview(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f),
            color = firstColor
        )
        ThemeColorPreview(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f),
            color = secondColor
        )
        ThemeColorPreview(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f),
            color = thirdColor
        )
        ThemeColorPreview(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f),
            color = forthColor
        )
    }
}

@Composable
private fun UserTopBar(
    modifier: Modifier = Modifier,
    name: String,
    points: Long,
    onRewardClick: () -> Unit,
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
                modifier = Modifier.testTag("AccountInfo_Text_AccountName"),
                text = name,
                fontSize = 16.sp,
                color = MaterialTheme.colors.primaryVariant,
                fontWeight = FontWeight.Medium
            )
            Text(
                modifier = Modifier.testTag("AccountInfo_Text_Credit"),
                text = stringResource(R.string.user_points, points),
                fontSize = 16.sp,
                color = MaterialTheme.colors.primaryVariant
            )
        }
        Row {
            IconButton(
                modifier = Modifier.testTag("AccountInfo_Btn_Bet"),
                drawableRes = R.drawable.ic_black_coin,
                onClick = onRewardClick
            )
            IconButton(
                modifier = Modifier
                    .testTag("AccountInfo_Btn_Logout")
                    .padding(start = 8.dp),
                drawableRes = R.drawable.ic_black_logout,
                onClick = onLogoutClick
            )
        }
    }
}

@Composable
private fun ThemeColorPreview(
    modifier: Modifier = Modifier,
    color: Color
) {
    Box(
        modifier = modifier
            .size(18.dp)
            .border(1.dp, Color.White, CircleShape)
            .shadow(4.dp, CircleShape)
            .background(color)
    )
}
