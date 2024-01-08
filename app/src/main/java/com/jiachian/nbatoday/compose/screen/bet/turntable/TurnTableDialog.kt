package com.jiachian.nbatoday.compose.screen.bet.turntable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.bet.models.TurnTablePoints
import com.jiachian.nbatoday.testing.testtag.BetTestTag
import com.jiachian.nbatoday.utils.rippleClickable

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AskTurnTableDialog(
    points: TurnTablePoints,
    onContinue: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        modifier = Modifier.testTag(BetTestTag.AskTurnTableDialog),
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.secondary,
        onDismissRequest = onCancel,
        title = {
            Text(
                text = stringResource(R.string.bet_ask_turn_table_title),
                color = MaterialTheme.colors.primary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
        },
        text = {
            Text(
                modifier = Modifier.testTag(BetTestTag.AskTurnTableDialog_Text_Body),
                text = pluralStringResource(
                    id = R.plurals.bet_ask_turn_table_text,
                    count = if (points.win <= 1 && points.lose <= 1) 1 else 0,
                    points.win,
                    points.lose
                ),
                color = MaterialTheme.colors.primary,
                fontSize = 16.sp
            )
        },
        buttons = {
            AskTurnTableButtons(
                modifier = Modifier.fillMaxWidth(),
                onContinue = onContinue,
                onCancel = onCancel
            )
        }
    )
}

@Composable
private fun AskTurnTableButtons(
    modifier: Modifier = Modifier,
    onContinue: () -> Unit,
    onCancel: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            modifier = Modifier
                .testTag(BetTestTag.AskTurnTableButtons_Text_Cancel)
                .padding(bottom = 8.dp)
                .rippleClickable { onCancel() }
                .padding(10.dp),
            text = stringResource(R.string.bet_ask_turn_table_cancel).toUpperCase(Locale.current),
            color = MaterialTheme.colors.primary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            modifier = Modifier
                .testTag(BetTestTag.AskTurnTableButtons_Text_Continue)
                .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
                .rippleClickable { onContinue() }
                .padding(10.dp),
            text = stringResource(R.string.bet_ask_turn_table_continue),
            color = MaterialTheme.colors.primary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
