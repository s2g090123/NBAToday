package com.jiachian.nbatoday.common.ui.bet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
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
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.data.Transparency25
import com.jiachian.nbatoday.common.ui.CustomOutlinedTextField
import com.jiachian.nbatoday.common.ui.TeamLogoImage
import com.jiachian.nbatoday.common.ui.bet.event.BetDialogDataEvent
import com.jiachian.nbatoday.common.ui.bet.event.BetDialogUIEvent
import com.jiachian.nbatoday.common.ui.bet.state.BetDialogState
import com.jiachian.nbatoday.game.data.model.local.GameTeam
import com.jiachian.nbatoday.testing.testtag.BetTestTag
import com.jiachian.nbatoday.utils.getOrZero
import com.jiachian.nbatoday.utils.rippleClickable
import com.jiachian.nbatoday.utils.showToast
import org.koin.androidx.compose.koinViewModel

@Composable
fun BetDialog(
    viewModel: BetDialogViewModel = koinViewModel(),
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.state
    val dismiss by rememberUpdatedState(onDismiss)
    BetDialogContent(
        state = state,
        onEvent = { viewModel.onEvent(it) }
    )
    if (state.warning) {
        BetWarningDialog(
            onConfirm = { viewModel.onEvent(BetDialogUIEvent.Bet) },
            onDismiss = { viewModel.onEvent(BetDialogUIEvent.CancelConfirm) }
        )
    }
    LaunchedEffect(state.event, viewModel) {
        state.event?.let { event ->
            when (event) {
                BetDialogDataEvent.Done -> dismiss()
                is BetDialogDataEvent.Error -> {
                    showToast(context, event.error.message)
                    dismiss()
                }
            }
            viewModel.onEvent(BetDialogUIEvent.EventReceived)
        }
    }
}

@Composable
private fun BetDialogContent(
    state: BetDialogState,
    onEvent: (BetDialogUIEvent) -> Unit,
) {
    Box(
        modifier = Modifier
            .size(300.dp, 280.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colors.secondary),
        contentAlignment = Alignment.Center
    ) {
        if (state.loading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        } else {
            state.game?.let { game ->
                Column(
                    modifier = Modifier.testTag(BetTestTag.BetDialog),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BetDialogDetail(
                        userPoints = state.userPoints,
                        homePoints = state.homePoints,
                        awayPoints = state.awayPoints,
                        home = game.game.homeTeam,
                        away = game.game.awayTeam,
                        onHomePointsChange = { onEvent(BetDialogUIEvent.TextHomePoints(it)) },
                        onAwayPointsChange = { onEvent(BetDialogUIEvent.TextAwayPoints(it)) }
                    )
                    BetDialogConfirmButton(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 8.dp, end = 8.dp),
                        enabled = state.valid,
                        onConfirm = { onEvent(BetDialogUIEvent.Confirm) }
                    )
                }
            }
        }
    }
}

@Composable
private fun BetDialogConfirmButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onConfirm: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            modifier = Modifier
                .testTag(BetTestTag.BetDialogConfirmButton_Text_Confirm)
                .rippleClickable(enabled) { onConfirm() }
                .padding(10.dp),
            text = stringResource(R.string.bet_confirm),
            color = MaterialTheme.colors.primary.copy(if (enabled) 1f else Transparency25),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun BetDialogDetail(
    modifier: Modifier = Modifier,
    userPoints: Long,
    homePoints: Long,
    awayPoints: Long,
    home: GameTeam,
    away: GameTeam,
    onHomePointsChange: (Long) -> Unit,
    onAwayPointsChange: (Long) -> Unit,
) {
    val remainingPoints = userPoints - homePoints - awayPoints
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BetDialogTeamInfo(
                modifier = Modifier
                    .testTag(BetTestTag.BetDialogDetail_TeamInfo_Home)
                    .padding(start = 16.dp)
                    .width(IntrinsicSize.Min),
                team = home,
                value = homePoints,
                onValueChange = onHomePointsChange,
            )
            OddsText(modifier = Modifier.padding(horizontal = 16.dp))
            BetDialogTeamInfo(
                modifier = Modifier
                    .testTag(BetTestTag.BetDialogDetail_TeamInfo_Away)
                    .padding(end = 16.dp)
                    .width(IntrinsicSize.Min),
                team = away,
                value = awayPoints,
                onValueChange = onAwayPointsChange,
            )
        }
        Text(
            modifier = Modifier
                .testTag(BetTestTag.BetDialogDetail_Text_Remainder)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            text = stringResource(R.string.bet_remain, remainingPoints),
            color = MaterialTheme.colors.primary,
            fontSize = 12.sp,
        )
    }
}

@Composable
private fun OddsText(
    modifier: Modifier = Modifier,
    homeOdds: Int = 1,
    awayOdds: Int = 1
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.bet_vs),
            color = MaterialTheme.colors.primary,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic
        )
        Text(
            text = stringResource(R.string.bet_odds, homeOdds, awayOdds),
            color = MaterialTheme.colors.primary,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun BetDialogTeamInfo(
    modifier: Modifier = Modifier,
    team: GameTeam,
    value: Long,
    onValueChange: (Long) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .testTag(BetTestTag.BetDialogTeamInfo_Text_Record)
                .padding(top = 16.dp),
            text = stringResource(R.string.bet_win_lose_record, team.wins, team.losses),
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
                .testTag(BetTestTag.BetDialogTeamInfo_TextField_Bet)
                .padding(top = 8.dp)
                .width(100.dp)
                .height(32.dp),
            value = if (value <= 0) "" else value.toString(),
            onValueChange = { onValueChange(it.toLongOrNull().getOrZero()) },
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
        modifier = Modifier.testTag(BetTestTag.BetWarningDialog),
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
            BetWarningDialogButtons(
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
private fun BetWarningDialogButtons(
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
                .testTag(BetTestTag.BetWarningDialogButtons_Text_Cancel)
                .padding(bottom = 8.dp)
                .rippleClickable { onCancel() }
                .padding(10.dp),
            text = stringResource(R.string.bet_warning_cancel).toUpperCase(Locale.current),
            color = MaterialTheme.colors.primary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            modifier = Modifier
                .testTag(BetTestTag.BetWarningDialogButtons_Text_Confirm)
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
