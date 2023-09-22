package com.jiachian.nbatoday.compose.screen.bet

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.utils.BackHandle
import com.jiachian.nbatoday.utils.rippleClickable

@OptIn(ExperimentalTextApi::class)
@Composable
fun BetTurnTable(
    modifier: Modifier = Modifier,
    viewModel: BetViewModel,
    onStart: () -> Unit,
    onClose: () -> Unit
) {
    val isStarting by viewModel.isTurnTableStarting.collectAsState()
    BackHandle(
        onBack = {
            if (!isStarting) {
                onClose()
            }
        }
    ) {
        val currentAngle by viewModel.currentAngle.collectAsState()
        val textMeasure = rememberTextMeasurer()
        val plus1TextSize = remember { textMeasure.measureSize("X2\n-0") }
        val minus1TextSize = remember { textMeasure.measureSize("+0") }
        val plus2TextSize = remember { textMeasure.measureSize("X5") }
        val minus2TextSize = remember { textMeasure.measureSize("+0\n-0") }
        Box(
            modifier = modifier
        ) {
            if (!isStarting) {
                TurnTableCancelButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopEnd)
                        .size(48.dp),
                    onClick = onClose
                )
            }
            TurnTableContent(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(320.dp)
                    .graphicsLayer {
                        rotationZ = currentAngle
                    },
                textMeasurer = textMeasure,
                plus1TextSize = plus1TextSize,
                plus2TextSize = plus2TextSize,
                minus1TextSize = minus1TextSize,
                minus2TextSize = minus2TextSize
            )
            BetTriangleCursor(
                modifier = Modifier
                    .align(Alignment.Center)
                    .graphicsLayer {
                        translationY -= 28.dp.toPx() + 160.dp.toPx() - 8.dp.toPx()
                    }
                    .size(48.dp, 56.dp),
                color = Color.Red
            )
            if (!isStarting) {
                TurnTableStartButton(
                    modifier = Modifier.align(Alignment.Center),
                    onClick = onStart
                )
            }
        }
    }
}

@Composable
private fun TurnTableCancelButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(48.dp),
            painter = painterResource(R.drawable.ic_black_cancel),
            contentDescription = null
        )
    }
}

@Composable
private fun TurnTableStartButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .testTag("BetTurnTable_Text_Start")
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

@OptIn(ExperimentalTextApi::class)
@Composable
private fun TurnTableContent(
    modifier: Modifier = Modifier,
    textMeasurer: TextMeasurer,
    plus1TextSize: IntSize,
    plus2TextSize: IntSize,
    minus1TextSize: IntSize,
    minus2TextSize: IntSize
) {
    Canvas(modifier = modifier) {
        val borderWidth = 2.dp.toPx()
        val turnTableSize = size.width - borderWidth * 3
        drawTurnTableBottom(borderWidth = borderWidth)
        drawTurnTableArc(
            borderWidth = borderWidth,
            turnTableSize = turnTableSize
        )
        drawTurnTableLine(borderWidth = borderWidth)
        rotate(45f) {
            drawText(
                textMeasurer = textMeasurer,
                text = "X2\n-0",
                textColor = Color.Black,
                textSize = plus1TextSize,
                tableSize = turnTableSize,
                borderWidth = borderWidth
            )
        }
        rotate(135f) {
            drawText(
                textMeasurer = textMeasurer,
                text = "+0",
                textColor = Color.White,
                textSize = minus1TextSize,
                tableSize = turnTableSize,
                borderWidth = borderWidth
            )
        }
        rotate(225f) {
            drawText(
                textMeasurer = textMeasurer,
                text = "X5",
                textColor = Color.Black,
                textSize = plus2TextSize,
                tableSize = turnTableSize,
                borderWidth = borderWidth
            )
        }
        rotate(315f) {
            drawText(
                textMeasurer = textMeasurer,
                text = "+0\n-0",
                textColor = Color.White,
                textSize = minus2TextSize,
                tableSize = turnTableSize,
                borderWidth = borderWidth
            )
        }
    }
}

private fun DrawScope.drawTurnTableBottom(
    borderWidth: Float
) {
    drawCircle(
        color = Color.Black,
        style = Stroke(width = borderWidth)
    )
    drawCircle(
        color = Color.Red,
        radius = size.width / 2f - borderWidth,
        style = Stroke(width = borderWidth)
    )
}

private fun DrawScope.drawTurnTableArc(
    borderWidth: Float,
    turnTableSize: Float
) {
    drawArc(
        color = Color.White,
        startAngle = -90f,
        sweepAngle = 90f,
        useCenter = true,
        topLeft = Offset(borderWidth * 3 / 2, borderWidth * 3 / 2),
        size = Size(turnTableSize, turnTableSize)
    )
    drawArc(
        color = Color.Black,
        startAngle = 0f,
        sweepAngle = 90f,
        useCenter = true,
        topLeft = Offset(borderWidth * 3 / 2, borderWidth * 3 / 2),
        size = Size(turnTableSize, turnTableSize)
    )
    drawArc(
        color = Color.White,
        startAngle = 90f,
        sweepAngle = 90f,
        useCenter = true,
        topLeft = Offset(borderWidth * 3 / 2, borderWidth * 3 / 2),
        size = Size(turnTableSize, turnTableSize)
    )
    drawArc(
        color = Color.Black,
        startAngle = 180f,
        sweepAngle = 90f,
        useCenter = true,
        topLeft = Offset(borderWidth * 3 / 2, borderWidth * 3 / 2),
        size = Size(turnTableSize, turnTableSize)
    )
}

private fun DrawScope.drawTurnTableLine(
    borderWidth: Float
) {
    drawLine(
        color = Color.Red,
        start = Offset(borderWidth * 3 / 2, size.height / 2f),
        end = Offset(size.width - borderWidth * 3 / 2, size.height / 2f),
        strokeWidth = 4.dp.toPx()
    )
    drawLine(
        color = Color.Red,
        start = Offset(size.width / 2f, borderWidth * 3 / 2),
        end = Offset(size.width / 2f, size.height - borderWidth * 3 / 2),
        strokeWidth = 4.dp.toPx()
    )
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.drawText(
    textMeasurer: TextMeasurer,
    text: String,
    textColor: Color,
    textSize: IntSize,
    tableSize: Float,
    borderWidth: Float
) {
    drawText(
        textMeasurer = textMeasurer,
        text = text,
        style = TextStyle(
            color = textColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        ),
        topLeft = Offset(
            center.x - textSize.width / 2f,
            borderWidth * 3 / 2 + tableSize / 4f - textSize.height / 2f - 12.dp.toPx()
        )
    )
}

@Composable
private fun BetTriangleCursor(
    modifier: Modifier = Modifier,
    color: Color,
    borderColor: Color = Color.Black
) {
    Spacer(
        modifier = modifier.then(
            Modifier.drawWithCache {
                val path = Path()
                path.moveTo(0f, 0f)
                path.lineTo(size.width, 0f)
                path.lineTo(size.width / 2f, size.height)
                path.close()
                onDrawBehind {
                    drawPath(path, color)
                    drawPath(path, borderColor, style = Stroke(width = 2.dp.toPx()))
                }
            }
        )
    )
}

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

@OptIn(ExperimentalTextApi::class)
private fun TextMeasurer.measureSize(
    text: String
): IntSize {
    return measure(
        text = AnnotatedString(text),
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
        ),
    ).size
}
