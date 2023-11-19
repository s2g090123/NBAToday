package com.jiachian.nbatoday.compose.screen.bet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.account.LoginDialog
import com.jiachian.nbatoday.compose.widget.CustomOutlinedTextField
import com.jiachian.nbatoday.compose.widget.TeamLogoImage
import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.remote.team.GameTeam
import com.jiachian.nbatoday.data.remote.user.User
import com.jiachian.nbatoday.utils.getOrZero
import com.jiachian.nbatoday.utils.rippleClickable
import com.jiachian.nbatoday.utils.showToast

@Composable
private fun BetDialog(
    userPoints: Long,
    gameAndBet: NbaGameAndBet,
    onConfirm: (gameId: String, homePoints: Long, awayPoints: Long) -> Unit,
    onDismiss: () -> Unit
) {
    var showWarning by rememberSaveable { mutableStateOf(false) }
    var homePoints by rememberSaveable { mutableStateOf(0L) }
    var awayPoints by rememberSaveable { mutableStateOf(0L) }
    val remainPoints by remember {
        derivedStateOf { userPoints - homePoints - awayPoints }
    }
    val hasBet by remember(homePoints, awayPoints) {
        derivedStateOf { homePoints > 0 || awayPoints > 0 }
    }
    val isConfirmEnable by remember(remainPoints, hasBet) {
        derivedStateOf { remainPoints >= 0 && hasBet }
    }
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .testTag("BetDialog_Dialog")
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colors.secondary),
            horizontalAlignment = Alignment.End
        ) {
            BetDialogContent(
                gameAndBet = gameAndBet,
                homePoints = homePoints,
                awayPoints = awayPoints,
                remainPoints = remainPoints,
                onPointsChanged = { home, away ->
                    homePoints = home
                    awayPoints = away
                }
            )
            BetDialogBottomButtons(
                modifier = Modifier
                    .padding(top = 8.dp, end = 8.dp),
                isConfirmEnable = isConfirmEnable,
                onClickConfirm = { showWarning = true }
            )
        }
    }
    if (showWarning) {
        BetWarningDialog(
            onConfirm = {
                onConfirm(gameAndBet.game.gameId, homePoints, awayPoints)
                onDismiss()
            },
            onDismiss = { showWarning = false }
        )
    }
}

@Composable
private fun BetDialogBottomButtons(
    modifier: Modifier = Modifier,
    isConfirmEnable: Boolean,
    onClickConfirm: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            modifier = Modifier
                .testTag("BetDialog_Btn_Confirm")
                .rippleClickable(isConfirmEnable) { onClickConfirm() }
                .padding(10.dp),
            text = stringResource(R.string.bet_confirm),
            color = MaterialTheme.colors.primary.copy(if (isConfirmEnable) 1f else 0.25f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun BetDialogContent(
    modifier: Modifier = Modifier,
    gameAndBet: NbaGameAndBet,
    homePoints: Long,
    awayPoints: Long,
    remainPoints: Long,
    onPointsChanged: (Long, Long) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BetDialogTeamEdit(
                modifier = Modifier
                    .testTag("BetDialogContent_BetDialogTeamEdit_Home")
                    .padding(start = 16.dp)
                    .width(IntrinsicSize.Min),
                team = gameAndBet.game.homeTeam,
                value = homePoints,
                onValueChanged = { onPointsChanged(it, awayPoints) }
            )
            OddsText(
                modifier = Modifier.padding(horizontal = 16.dp),
                homeOdds = 1,
                awayOdds = 1
            )
            BetDialogTeamEdit(
                modifier = Modifier
                    .testTag("BetDialogContent_BetDialogTeamEdit_Away")
                    .padding(end = 16.dp)
                    .width(IntrinsicSize.Min),
                team = gameAndBet.game.awayTeam,
                value = awayPoints,
                onValueChanged = { onPointsChanged(homePoints, it) }
            )
        }
        Text(
            modifier = Modifier
                .testTag("BetDialog_Text_Remainder")
                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
            text = stringResource(R.string.bet_remain, remainPoints),
            color = MaterialTheme.colors.primary,
            fontSize = 12.sp,
        )
    }
}

@Composable
private fun OddsText(
    modifier: Modifier = Modifier,
    homeOdds: Int,
    awayOdds: Int
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.bet_vs),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic
        )
        Text(
            text = stringResource(R.string.bet_odds, homeOdds, awayOdds),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun BetDialogTeamEdit(
    modifier: Modifier = Modifier,
    team: GameTeam,
    value: Long,
    onValueChanged: (Long) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .testTag("BetDialogTeamEdit_Text_Record")
                .padding(top = 16.dp),
            text = stringResource(
                R.string.bet_win_lose_record,
                team.wins,
                team.losses
            ),
            color = MaterialTheme.colors.primary,
            fontSize = 20.sp
        )
        TeamLogoImage(
            modifier = Modifier
                .padding(top = 8.dp)
                .size(100.dp),
            team = team.team
        )
        CustomOutlinedTextField(
            modifier = Modifier
                .testTag("BetDialogTeamEdit_TextField_Bet")
                .padding(top = 8.dp)
                .width(100.dp)
                .height(32.dp),
            value = if (value <= 0) "" else value.toString(),
            onValueChange = { onValueChanged(it.toLongOrNull().getOrZero()) },
            textStyle = TextStyle(
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            maxLines = 1,
        )
    }
}

@Composable
private fun BetWarningDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = Modifier.testTag("BetDialog_Alert_Warning"),
        onDismissRequest = onDismiss,
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
            BetWarningBottomButtons(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                onConfirm = {
                    onConfirm()
                    onDismiss()
                },
                onCancel = {
                    onDismiss()
                }
            )
        },
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.secondary
    )
}

@Composable
private fun BetWarningBottomButtons(
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .rippleClickable { onCancel() }
                .padding(10.dp),
            text = stringResource(R.string.bet_warning_cancel),
            color = MaterialTheme.colors.primary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            modifier = Modifier
                .testTag("BetDialog_Alert_Confirm")
                .padding(bottom = 8.dp)
                .rippleClickable { onConfirm() }
                .padding(10.dp),
            text = stringResource(R.string.bet_warning_confirm),
            color = MaterialTheme.colors.primary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun RequestBetScreen(
    userData: User?,
    gameAndBet: NbaGameAndBet,
    onLogin: (account: String, password: String) -> Unit,
    onRegister: (account: String, password: String) -> Unit,
    onConfirm: (gameId: String, homePoints: Long, awayPoints: Long) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val hasBet by remember(userData) {
        derivedStateOf { gameAndBet.bets.find { it.account == userData?.account } != null }
    }
    if (userData == null) {
        LoginDialog(
            onLogin = onLogin,
            onRegister = onRegister,
            onDismiss = onDismiss
        )
    } else if (hasBet) {
        showToast(context, R.string.bet_toast_already_bet_before)
        onDismiss()
    } else {
        BetDialog(
            userPoints = userData.points.getOrZero(),
            gameAndBet = gameAndBet,
            onConfirm = onConfirm,
            onDismiss = onDismiss
        )
    }
}
