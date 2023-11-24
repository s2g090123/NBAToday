package com.jiachian.nbatoday.compose.screen.bet.turntable

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val TableBorderWidthScale = 3
private const val TableQuarter = 4f

fun DrawScope.drawTurnTableBottom(borderWidth: Float) {
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

fun DrawScope.drawTurnTableArc(
    borderWidth: Float,
    turnTableSize: Float
) {
    drawArc(
        color = Color.White,
        startAngle = -90f,
        sweepAngle = 90f,
        useCenter = true,
        topLeft = Offset(borderWidth * TableBorderWidthScale / 2, borderWidth * TableBorderWidthScale / 2),
        size = Size(turnTableSize, turnTableSize)
    )
    drawArc(
        color = Color.Black,
        startAngle = 0f,
        sweepAngle = 90f,
        useCenter = true,
        topLeft = Offset(borderWidth * TableBorderWidthScale / 2, borderWidth * TableBorderWidthScale / 2),
        size = Size(turnTableSize, turnTableSize)
    )
    drawArc(
        color = Color.White,
        startAngle = 90f,
        sweepAngle = 90f,
        useCenter = true,
        topLeft = Offset(borderWidth * TableBorderWidthScale / 2, borderWidth * TableBorderWidthScale / 2),
        size = Size(turnTableSize, turnTableSize)
    )
    drawArc(
        color = Color.Black,
        startAngle = 180f,
        sweepAngle = 90f,
        useCenter = true,
        topLeft = Offset(borderWidth * TableBorderWidthScale / 2, borderWidth * TableBorderWidthScale / 2),
        size = Size(turnTableSize, turnTableSize)
    )
}

fun DrawScope.drawTurnTableLine(
    borderWidth: Float
) {
    drawLine(
        color = Color.Red,
        start = Offset(borderWidth * TableBorderWidthScale / 2, size.height / 2f),
        end = Offset(size.width - borderWidth * TableBorderWidthScale / 2, size.height / 2f),
        strokeWidth = 4.dp.toPx()
    )
    drawLine(
        color = Color.Red,
        start = Offset(size.width / 2f, borderWidth * TableBorderWidthScale / 2),
        end = Offset(size.width / 2f, size.height - borderWidth * TableBorderWidthScale / 2),
        strokeWidth = 4.dp.toPx()
    )
}

@OptIn(ExperimentalTextApi::class)
fun DrawScope.drawText(
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
            borderWidth * TableBorderWidthScale / 2 + tableSize / TableQuarter - textSize.height / 2f - 12.dp.toPx()
        )
    )
}

@OptIn(ExperimentalTextApi::class)
fun TextMeasurer.measureSize(
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
