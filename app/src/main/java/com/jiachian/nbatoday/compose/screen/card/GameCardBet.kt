package com.jiachian.nbatoday.compose.screen.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.account.LoginDialog
import com.jiachian.nbatoday.compose.screen.bet.dialog.BetDialog
import com.jiachian.nbatoday.compose.widget.IconButton
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.testing.testtag.GameCardTestTag
import com.jiachian.nbatoday.utils.showToast

@Composable
fun GameCardBetScreen(viewModel: GameCardUIData) {
    val login by viewModel.login.collectAsState()
    val hasBet by viewModel.hasBet.collectAsState()
    when {
        !login -> {
            LoginDialog(
                onLogin = viewModel::login,
                onRegister = viewModel::register,
                onDismiss = { viewModel.setBetDialogVisible(false) }
            )
        }
        hasBet -> {
            LaunchedEffect(Unit) {
                showToast(R.string.bet_toast_already_bet_before)
                viewModel.setBetDialogVisible(false)
            }
        }
        else -> {
            val userPoints by viewModel.userPoints.collectAsState()
            BetDialog(
                game = viewModel.gameAndBets.game,
                userPoints = userPoints,
                onConfirm = viewModel::bet,
                onDismiss = { viewModel.setBetDialogVisible(false) },
            )
        }
    }
}

@Composable
fun GameStatusAndBetButton(
    modifier: Modifier = Modifier,
    gameAndBets: GameAndBets,
    textColor: Color,
    betAvailable: Boolean,
    onBet: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.testTag(GameCardTestTag.GameStatusAndBetButton_Text_Status),
            text = gameAndBets.game.statusFormattedText,
            textAlign = TextAlign.Center,
            color = textColor,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic
        )
        if (betAvailable) {
            IconButton(
                modifier = Modifier
                    .testTag(GameCardTestTag.GameStatusAndBetButton_Button_Bet)
                    .padding(top = 8.dp),
                drawableRes = R.drawable.ic_black_coin,
                tint = textColor,
                onClick = onBet
            )
        }
    }
}
