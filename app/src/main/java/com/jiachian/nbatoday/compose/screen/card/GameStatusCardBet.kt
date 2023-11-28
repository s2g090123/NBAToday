package com.jiachian.nbatoday.compose.screen.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.account.LoginDialog
import com.jiachian.nbatoday.compose.screen.bet.BetDialog
import com.jiachian.nbatoday.compose.widget.IconButton
import com.jiachian.nbatoday.models.local.game.NbaGameAndBet
import com.jiachian.nbatoday.utils.showToast

@Composable
fun GameStatusCardBetScreen(viewModel: GameStatusCardViewModel) {
    val context = LocalContext.current
    val isLogin by viewModel.isLogin.collectAsState()
    val hasBet by viewModel.hasBet.collectAsState()
    if (!isLogin) {
        LoginDialog(
            onLoginClicked = viewModel::login,
            onRegisterClicked = viewModel::register,
            onDismiss = {}
        )
    } else if (hasBet) {
        showToast(context, R.string.bet_toast_already_bet_before)
    } else {
        BetDialog(
            viewModel = viewModel.createBetDialogViewModel(),
            onConfirm = viewModel::bet,
            onDismiss = viewModel::hideBetDialog
        )
    }
}

@Composable
fun GameStatusAndBetButton(
    modifier: Modifier = Modifier,
    gameAndBet: NbaGameAndBet,
    textColor: Color,
    betVisible: Boolean,
    onBetClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.testTag("GameStatusCard2_Text_GameStatus"),
            text = gameAndBet.game.gameStatusFormatText,
            textAlign = TextAlign.Center,
            color = textColor,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic
        )
        if (betVisible) {
            IconButton(
                modifier = Modifier
                    .testTag("GameStatusCard2_Btn_Bet")
                    .padding(top = 8.dp),
                drawableRes = R.drawable.ic_black_coin,
                tint = textColor,
                onClick = onBetClick
            )
        }
    }
}
