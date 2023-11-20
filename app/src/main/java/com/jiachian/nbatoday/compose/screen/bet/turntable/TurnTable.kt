package com.jiachian.nbatoday.compose.screen.bet.turntable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.compose.screen.bet.BetViewModel
import com.jiachian.nbatoday.compose.widget.BackHandle

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
