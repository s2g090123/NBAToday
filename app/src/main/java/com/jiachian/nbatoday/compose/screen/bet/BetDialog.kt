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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.jiachian.nbatoday.Transparency25
import com.jiachian.nbatoday.compose.widget.CustomOutlinedTextField
import com.jiachian.nbatoday.compose.widget.TeamLogoImage
import com.jiachian.nbatoday.models.local.game.GameTeam
import com.jiachian.nbatoday.testing.testtag.BetTestTag
import com.jiachian.nbatoday.utils.getOrZero
import com.jiachian.nbatoday.utils.rippleClickable

@Composable
fun BetDialog(
    viewModel: BetDialogViewModel,
    onConfirm: (homePoints: Long, awayPoints: Long) -> Unit,
    onDismiss: () -> Unit
) {
    val showWarning by viewModel.showWarning.collectAsState()
    val homePoints by viewModel.homePoints.collectAsState()
    val awayPoints by viewModel.awayPoints.collectAsState()
    val confirmedEnabled by viewModel.confirmEnabled.collectAsState()
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .testTag(BetTestTag.BetDialog)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colors.secondary),
            horizontalAlignment = Alignment.End
        ) {
            Detail(
                viewModel = viewModel,
                homePoints = homePoints,
                awayPoints = awayPoints,
            )
            ConfirmButton(
                modifier = Modifier
                    .padding(top = 8.dp, end = 8.dp),
                enabled = confirmedEnabled,
                onConfirm = viewModel::showWarning
            )
        }
    }
    if (showWarning) {
        WarningDialog(
            onConfirm = {
                onConfirm(homePoints, awayPoints)
                onDismiss()
            },
            onDismiss = viewModel::hideWarning
        )
    }
}

@Composable
private fun ConfirmButton(
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
                .testTag(BetTestTag.BetDialog_ConfirmButton_Text)
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
private fun Detail(
    modifier: Modifier = Modifier,
    viewModel: BetDialogViewModel,
    homePoints: Long,
    awayPoints: Long
) {
    val remainedPoints by viewModel.remainedPoints.collectAsState()
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            TeamInfo(
                modifier = Modifier
                    .testTag(BetTestTag.BetDialog_Detail_TeamInfo_Home)
                    .padding(start = 16.dp)
                    .width(IntrinsicSize.Min),
                team = viewModel.gameAndBet.game.homeTeam,
                value = homePoints,
                onValueChanged = viewModel::updateHomePoints
            )
            OddsText(modifier = Modifier.padding(horizontal = 16.dp))
            TeamInfo(
                modifier = Modifier
                    .testTag(BetTestTag.BetDialog_Detail_TeamInfo_Away)
                    .padding(end = 16.dp)
                    .width(IntrinsicSize.Min),
                team = viewModel.gameAndBet.game.awayTeam,
                value = awayPoints,
                onValueChanged = viewModel::updateAwayPoints
            )
        }
        Text(
            modifier = Modifier
                .testTag(BetTestTag.BetDialog_Detail_Text_Remainder)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            text = stringResource(R.string.bet_remain, remainedPoints),
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
private fun TeamInfo(
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
                .testTag(BetTestTag.BetDialog_TeamInfo_Text_Record)
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
                .testTag(BetTestTag.BetDialog_TeamInfo_TextField_Bet)
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
private fun WarningDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = Modifier.testTag(BetTestTag.BetDialog_WarningDialog),
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
            WarningDialogButton(
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
private fun WarningDialogButton(
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
                .testTag(BetTestTag.BetDialog_WarningDialog_Text_Confirm)
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
