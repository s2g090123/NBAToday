package com.jiachian.nbatoday.bet.ui.turntable

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.testing.testtag.BetTestTag
import com.jiachian.nbatoday.utils.rippleClickable
import kotlin.math.abs

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TurnTableRewardedDialog(
    points: Long,
    onDismiss: () -> Unit
) {
    AlertDialog(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.secondary,
        onDismissRequest = onDismiss,
        title = {
            Text(
                modifier = Modifier.testTag(BetTestTag.RewardedPointsDialog_Text_Title),
                text = stringResource(
                    if (points >= 0) R.string.bet_reward_win_title
                    else R.string.bet_reward_lose_title
                ),
                color = MaterialTheme.colors.primary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
        },
        text = {
            Text(
                modifier = Modifier.testTag(BetTestTag.RewardedPointsDialog_Text_Body),
                text = pluralStringResource(
                    id = if (points >= 0) R.plurals.bet_reward_win_text else R.plurals.bet_reward_lose_text,
                    count = abs(points).toInt(),
                    abs(points)
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
                        .testTag(BetTestTag.RewardPointDialog_Text_OK)
                        .padding(bottom = 8.dp, end = 8.dp)
                        .rippleClickable { onDismiss() }
                        .padding(10.dp),
                    text = stringResource(R.string.bet_reward_ok),
                    color = MaterialTheme.colors.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}
