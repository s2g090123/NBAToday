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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.bet.BetsTurnTableData
import com.jiachian.nbatoday.utils.rippleClickable

@Composable
fun AskTurnTableDialog(
    turnTableData: BetsTurnTableData,
    onContinue: (data: BetsTurnTableData) -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        modifier = Modifier.testTag("AskTurnTableDialog_Dialog"),
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.secondary,
        onDismissRequest = onCancel,
        title = {
            Text(
                modifier = Modifier.testTag("AskTurnTableDialog_Text_Title"),
                text = stringResource(R.string.bet_ask_turn_table_title),
                color = MaterialTheme.colors.primary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
        },
        text = {
            Text(
                modifier = Modifier.testTag("AskTurnTableDialog_Text_Description"),
                text = stringResource(
                    R.string.bet_ask_turn_table_text,
                    turnTableData.winPoints,
                    turnTableData.losePoints
                ),
                color = MaterialTheme.colors.primary,
                fontSize = 16.sp
            )
        },
        buttons = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .rippleClickable { onCancel() }
                        .padding(10.dp),
                    text = stringResource(R.string.bet_ask_turn_table_cancel),
                    color = MaterialTheme.colors.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    modifier = Modifier
                        .testTag("AskTurnTableDialog_Btn_Continue")
                        .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
                        .rippleClickable { onContinue(turnTableData) }
                        .padding(10.dp),
                    text = stringResource(R.string.bet_ask_turn_table_continue),
                    color = MaterialTheme.colors.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}
