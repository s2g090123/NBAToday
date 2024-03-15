package com.jiachian.nbatoday.compose.screen.bet.turntable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jiachian.nbatoday.compose.screen.bet.models.TurnTableUIState
import com.jiachian.nbatoday.testing.testtag.BetTestTag
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

private const val PlusText1 = "X2\n-0"
private const val PlusText2 = "X5"
private const val MinusText1 = "+0"
private const val MinusText2 = "+0\n-0"

@OptIn(ExperimentalTextApi::class, ExperimentalComposeUiApi::class)
@Composable
fun BetTurnTable(
    modifier: Modifier = Modifier,
    uiState: TurnTableUIState.TurnTable,
    onStart: () -> Unit,
    onClose: () -> Unit
) {
    val textMeasure = rememberTextMeasurer()
    val plusTextSize1 = remember { textMeasure.measureSize(PlusText1) }
    val minusTextSize1 = remember { textMeasure.measureSize(MinusText1) }
    val plusTextSize2 = remember { textMeasure.measureSize(PlusText2) }
    val minusTextSize2 = remember { textMeasure.measureSize(MinusText2) }
    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(
            dismissOnBackPress = !uiState.running,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
        )
    ) {
        Box(modifier = modifier) {
            if (!uiState.running) {
                TurnTableCancelButton(
                    modifier = Modifier
                        .testTag(BetTestTag.BetTurnTable_Button_Cancel)
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
                    .graphicsLayer { rotationZ = uiState.angle },
                textMeasurer = textMeasure,
                plusTextSize1 = plusTextSize1,
                plusTextSize2 = plusTextSize2,
                minusTextSize1 = minusTextSize1,
                minusTextSize2 = minusTextSize2
            )
            TurnTableCursor(
                modifier = Modifier
                    .align(Alignment.Center)
                    .graphicsLayer {
                        translationY -= 28.dp.toPx() + 160.dp.toPx() - 8.dp.toPx()
                    }
                    .size(48.dp, 56.dp),
            )
            if (!uiState.running) {
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
    plusTextSize1: IntSize,
    plusTextSize2: IntSize,
    minusTextSize1: IntSize,
    minusTextSize2: IntSize
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
            text = PlusText1,
            textColor = Color.Black,
            textSize = plusTextSize1,
            tableSize = turnTableSize,
            borderWidth = borderWidth
        )
        drawText(
            textMeasurer = textMeasurer,
            rotation = SecondSectorRotation,
            text = MinusText1,
            textColor = Color.White,
            textSize = minusTextSize1,
            tableSize = turnTableSize,
            borderWidth = borderWidth
        )
        drawText(
            textMeasurer = textMeasurer,
            rotation = ThirdSectorRotation,
            text = PlusText2,
            textColor = Color.Black,
            textSize = plusTextSize2,
            tableSize = turnTableSize,
            borderWidth = borderWidth
        )
        drawText(
            textMeasurer = textMeasurer,
            rotation = ForthSectorRotation,
            text = MinusText2,
            textColor = Color.White,
            textSize = minusTextSize2,
            tableSize = turnTableSize,
            borderWidth = borderWidth
        )
    }
}

@Composable
private fun TurnTableCursor(modifier: Modifier = Modifier) {
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
                    drawPath(path, Color.Red)
                    drawPath(path, Color.Black, style = Stroke(width = 2.dp.toPx()))
                }
            }
        )
    )
}
