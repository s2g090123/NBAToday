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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.compose.screen.bet.BetViewModel
import com.jiachian.nbatoday.compose.widget.BackHandle
import com.jiachian.nbatoday.utils.drawText
import com.jiachian.nbatoday.utils.drawTurnTableArc
import com.jiachian.nbatoday.utils.drawTurnTableBottom
import com.jiachian.nbatoday.utils.drawTurnTableLine
import com.jiachian.nbatoday.utils.measureSize

private const val TurnTableBorderWidthScale = 3
private const val FirstSectorRotation = 45f
private const val SecondSectorRotation = 135f
private const val ThirdSectorRotation = 225f
private const val ForthSectorRotation = 315f

@OptIn(ExperimentalTextApi::class)
@Composable
fun BetTurnTable(
    modifier: Modifier = Modifier,
    viewModel: BetViewModel,
    onStart: () -> Unit,
    onClose: () -> Unit
) {
    val isStarting by viewModel.turnTableRunning.collectAsState()
    val currentAngle by viewModel.turnTableAngle.collectAsState()
    val textMeasure = rememberTextMeasurer()
    val plus1TextSize = remember { textMeasure.measureSize("X2\n-0") }
    val minus1TextSize = remember { textMeasure.measureSize("+0") }
    val plus2TextSize = remember { textMeasure.measureSize("X5") }
    val minus2TextSize = remember { textMeasure.measureSize("+0\n-0") }
    BackHandle(
        onBack = {
            if (!isStarting) {
                onClose()
            }
        }
    ) {
        Box(modifier = modifier) {
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
            TriangleCursor(
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
        val turnTableSize = size.width - borderWidth * TurnTableBorderWidthScale
        drawTurnTableBottom(borderWidth = borderWidth)
        drawTurnTableArc(
            borderWidth = borderWidth,
            turnTableSize = turnTableSize
        )
        drawTurnTableLine(borderWidth = borderWidth)
        drawText(
            textMeasurer = textMeasurer,
            rotation = FirstSectorRotation,
            text = "X2\n-0",
            textColor = Color.Black,
            textSize = plus1TextSize,
            tableSize = turnTableSize,
            borderWidth = borderWidth
        )
        drawText(
            textMeasurer = textMeasurer,
            rotation = SecondSectorRotation,
            text = "+0",
            textColor = Color.White,
            textSize = minus1TextSize,
            tableSize = turnTableSize,
            borderWidth = borderWidth
        )
        drawText(
            textMeasurer = textMeasurer,
            rotation = ThirdSectorRotation,
            text = "X5",
            textColor = Color.Black,
            textSize = plus2TextSize,
            tableSize = turnTableSize,
            borderWidth = borderWidth
        )
        drawText(
            textMeasurer = textMeasurer,
            rotation = ForthSectorRotation,
            text = "+0\n-0",
            textColor = Color.White,
            textSize = minus2TextSize,
            tableSize = turnTableSize,
            borderWidth = borderWidth
        )
    }
}

@Composable
private fun TriangleCursor(
    modifier: Modifier = Modifier,
    color: Color,
    borderColor: Color = Color.Black
) {
    val path = remember { Path() }
    Spacer(
        modifier = modifier.then(
            Modifier.drawWithCache {
                path.reset()
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
