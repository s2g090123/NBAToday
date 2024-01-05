package com.jiachian.nbatoday.compose.screen.bet.turntable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.widget.IconButton
import com.jiachian.nbatoday.testing.testtag.BetTestTag
import com.jiachian.nbatoday.utils.rippleClickable

@Composable
fun TurnTableCancelButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        drawableRes = R.drawable.ic_black_cancel,
        tint = Color.White,
        padding = 0.dp,
        onClick = onClick,
    )
}

@Composable
fun TurnTableStartButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(modifier = modifier) {
        Text(
            modifier = Modifier
                .testTag(BetTestTag.TurnTableStartButton_Text_Start)
                .clip(CircleShape)
                .shadow(8.dp)
                .background(Color.Red)
                .rippleClickable { onClick() }
                .padding(12.dp),
            text = stringResource(R.string.bet_turn_table_start),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}
