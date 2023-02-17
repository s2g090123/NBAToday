package com.jiachian.nbatoday.compose.screen.bet

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.data.local.BetAndNbaGame
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import com.jiachian.nbatoday.utils.NbaUtils
import com.jiachian.nbatoday.utils.color
import com.jiachian.nbatoday.utils.noRippleClickable
import com.jiachian.nbatoday.utils.rippleClickable
import kotlinx.coroutines.delay
import java.util.*
import kotlin.math.abs

@Composable
fun BetScreen(
    viewModel: BetViewModel,
    onBackClick: () -> Unit
) {
    val askTurnTable by viewModel.askTurnTable.collectAsState()
    val showTurnTable by viewModel.showTryTurnTable.collectAsState()
    var showRewardPoints by remember { mutableStateOf<Long?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
            .noRippleClickable { }
    ) {
        IconButton(
            modifier = Modifier.padding(top = 8.dp, start = 8.dp),
            onClick = onBackClick
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_black_back),
                contentDescription = null,
                tint = MaterialTheme.colors.secondary
            )
        }
        BetContent(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel
        )
    }
    askTurnTable?.let { betData ->
        AskTurnTableDialog(
            turnTableData = betData,
            onContinue = {
                viewModel.startTurnTable(it)
                viewModel.closeAskTurnTable()
            },
            onCancel = { viewModel.closeAskTurnTable() }
        )
    }
    showTurnTable?.let { betData ->
        BetTurnTable(
            modifier = Modifier
                .fillMaxSize()
                .background("#66000000".color),
            winPoint = betData.winPoints,
            losePoint = betData.losePoints,
            onReward = {
                viewModel.addPoints(it)
                viewModel.closeTurnTable()
                showRewardPoints = it
            },
            onClose = { viewModel.closeTurnTable() }
        )
    }
    showRewardPoints?.let { reward ->
        RewardPointDialog(
            rewardPoints = reward,
            onDismiss = { showRewardPoints = null }
        )
    }
    BackHandler {
        onBackClick()
    }
}

@Composable
private fun BetContent(
    modifier: Modifier = Modifier,
    viewModel: BetViewModel
) {
    val betsAndGames by viewModel.betAndGame.collectAsState()

    if (betsAndGames.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.bet_no_record),
                fontSize = 18.sp,
                color = MaterialTheme.colors.secondary
            )
        }
    } else {
        LazyColumn(modifier = modifier) {
            itemsIndexed(betsAndGames) { index, betAndGame ->
                BetCard(
                    modifier = Modifier
                        .padding(
                            top = if (index == 0) 8.dp else 16.dp,
                            bottom = if (index >= betsAndGames.size - 1) 16.dp else 0.dp,
                            start = 16.dp,
                            end = 16.dp
                        )
                        .clip(RoundedCornerShape(16.dp))
                        .shadow(8.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.secondary)
                        .padding(bottom = 8.dp)
                        .rippleClickable {
                            viewModel.clickBetAndGame(betAndGame)
                        },
                    betAndGame = betAndGame
                )
            }
        }
    }
}

@Composable
private fun BetCard(
    modifier: Modifier = Modifier,
    betAndGame: BetAndNbaGame
) {
    val isGameFinal by remember(betAndGame) {
        derivedStateOf { betAndGame.game.gameStatus == GameStatusCode.FINAL }
    }
    val isHomeWin by remember(betAndGame) {
        derivedStateOf { betAndGame.game.homeTeam.score > betAndGame.game.awayTeam.score }
    }

    ConstraintLayout(
        modifier = modifier
    ) {
        val (
            homePointsText, homeLogo, homeScoreText,
            awayPointsText, awayLogo, awayScoreText,
            gameStatusText, winnerIcon
        ) = createRefs()

        Text(
            modifier = Modifier
                .constrainAs(homePointsText) {
                    top.linkTo(parent.top, 8.dp)
                    linkTo(homeLogo.start, homeLogo.end)
                },
            text = if (isGameFinal) {
                if (isHomeWin) {
                    "+" + (betAndGame.bets.homePoints * 2).toString()
                } else {
                    "-" + (betAndGame.bets.homePoints).toString()
                }
            } else {
                betAndGame.bets.homePoints.toString()
            },
            color = if (isGameFinal) {
                if (isHomeWin) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.secondaryVariant
            } else {
                MaterialTheme.colors.primary
            },
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        AsyncImage(
            modifier = Modifier
                .constrainAs(homeLogo) {
                    top.linkTo(homePointsText.bottom, 8.dp)
                    start.linkTo(parent.start, 16.dp)
                }
                .size(100.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(NbaUtils.getTeamLogoUrlById(betAndGame.game.homeTeam.teamId))
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            error = painterResource(NbaUtils.getTeamLogoResById(betAndGame.game.homeTeam.teamId)),
            placeholder = painterResource(NbaUtils.getTeamLogoResById(betAndGame.game.homeTeam.teamId)),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .constrainAs(awayPointsText) {
                    top.linkTo(parent.top, 8.dp)
                    linkTo(awayLogo.start, awayLogo.end)
                },
            text = if (isGameFinal) {
                if (!isHomeWin) {
                    "+" + (betAndGame.bets.awayPoints * 2).toString()
                } else {
                    "-" + (betAndGame.bets.awayPoints).toString()
                }
            } else {
                betAndGame.bets.awayPoints.toString()
            },
            color = if (isGameFinal) {
                if (!isHomeWin) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.secondaryVariant
            } else {
                MaterialTheme.colors.primary
            },
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        AsyncImage(
            modifier = Modifier
                .constrainAs(awayLogo) {
                    top.linkTo(awayPointsText.bottom, 8.dp)
                    end.linkTo(parent.end, 16.dp)
                }
                .size(100.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(NbaUtils.getTeamLogoUrlById(betAndGame.game.awayTeam.teamId))
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            error = painterResource(NbaUtils.getTeamLogoResById(betAndGame.game.awayTeam.teamId)),
            placeholder = painterResource(NbaUtils.getTeamLogoResById(betAndGame.game.awayTeam.teamId)),
            contentDescription = null
        )
        if (betAndGame.game.gameStatus != GameStatusCode.COMING_SOON) {
            Text(
                modifier = Modifier
                    .constrainAs(homeScoreText) {
                        top.linkTo(homeLogo.bottom, 8.dp)
                        linkTo(homeLogo.start, homeLogo.end)
                    },
                text = betAndGame.game.homeTeam.score.toString(),
                color = MaterialTheme.colors.primary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier
                    .constrainAs(awayScoreText) {
                        top.linkTo(awayLogo.bottom, 8.dp)
                        linkTo(awayLogo.start, awayLogo.end)
                    },
                text = betAndGame.game.awayTeam.score.toString(),
                color = MaterialTheme.colors.primary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            modifier = Modifier
                .constrainAs(gameStatusText) {
                    linkTo(homeLogo.top, awayLogo.bottom)
                    linkTo(homeLogo.end, awayLogo.start)
                },
            text = if (betAndGame.game.gameStatus == GameStatusCode.COMING_SOON) {
                betAndGame.game.gameStatusText.replaceFirst(" ", "\n") + "\n1:1"
            } else {
                betAndGame.game.gameStatusText + "\n1:1"
            }.trim(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic
        )
        if (isGameFinal) {
            Icon(
                modifier = Modifier
                    .constrainAs(winnerIcon) {
                        if (isHomeWin) {
                            start.linkTo(homeLogo.start)
                            top.linkTo(homeLogo.top)
                        } else {
                            start.linkTo(awayLogo.start)
                            top.linkTo(awayLogo.top)
                        }
                    }
                    .size(32.dp)
                    .graphicsLayer {
                        translationX = -6.dp.toPx()
                        translationY = -14.dp.toPx()
                        rotationZ = -45f
                    },
                painter = painterResource(R.drawable.ic_black_crown),
                contentDescription = null,
                tint = MaterialTheme.colors.primary
            )
        }
    }
}

@Composable
private fun AskTurnTableDialog(
    turnTableData: BetsTurnTableData,
    onContinue: (data: BetsTurnTableData) -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
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
@Composable
private fun BetTurnTable(
    modifier: Modifier = Modifier,
    winPoint: Long,
    losePoint: Long,
    onReward: (points: Long) -> Unit,
    onClose: () -> Unit
) {
    var isStarting by rememberSaveable { mutableStateOf(false) }
    val finishAngle = rememberSaveable { Random().nextInt(359).toFloat() }
    var currentAngle by rememberSaveable { mutableStateOf(0f) }
    var remainTime = rememberSaveable { 8000 }
    val textMeasure = rememberTextMeasurer()

    val plus1TextSize = remember {
        textMeasure.measure(
            text = AnnotatedString("X2\n-0"),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
            ),
        ).size
    }
    val minus1TextSize = remember {
        textMeasure.measure(
            text = AnnotatedString("+0"),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
            ),
        ).size
    }
    val plus2TextSize = remember {
        textMeasure.measure(
            text = AnnotatedString("X5"),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
            ),
        ).size
    }
    val minus2TextSize = remember {
        textMeasure.measure(
            text = AnnotatedString("+0\n-0"),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
            ),
        ).size
    }

    Box(
        modifier = modifier
    ) {
        if (!isStarting) {
            IconButton(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopEnd)
                    .size(48.dp),
                onClick = onClose
            ) {
                Icon(
                    modifier = Modifier.size(48.dp),
                    painter = painterResource(R.drawable.ic_black_cancel),
                    contentDescription = null
                )
            }
        }
        Canvas(
            modifier = Modifier
                .align(Alignment.Center)
                .size(320.dp)
                .graphicsLayer {
                    rotationZ = currentAngle
                }
        ) {
            val borderWidth = 2.dp.toPx()
            val turnTableSize = size.width - borderWidth * 3
            drawCircle(
                color = Color.Black,
                style = Stroke(width = borderWidth)
            )
            drawCircle(
                color = Color.Red,
                radius = size.width / 2f - borderWidth,
                style = Stroke(width = borderWidth)
            )
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
            rotate(45f) {
                drawText(
                    textMeasurer = textMeasure,
                    text = "X2\n-0",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    ),
                    topLeft = Offset(
                        center.x - plus1TextSize.width / 2f,
                        borderWidth * 3 / 2 + turnTableSize / 4f - plus1TextSize.height / 2f - 12.dp.toPx()
                    )
                )
            }
            rotate(135f) {
                drawText(
                    textMeasurer = textMeasure,
                    text = "+0",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    ),
                    topLeft = Offset(
                        center.x - minus1TextSize.width / 2f,
                        borderWidth * 3 / 2 + turnTableSize / 4f - minus1TextSize.height / 2f - 12.dp.toPx()
                    )
                )
            }
            rotate(225f) {
                drawText(
                    textMeasurer = textMeasure,
                    text = "X5",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    ),
                    topLeft = Offset(
                        center.x - plus2TextSize.width / 2f,
                        borderWidth * 3 / 2 + turnTableSize / 4f - plus2TextSize.height / 2f - 12.dp.toPx()
                    )
                )
            }
            rotate(315f) {
                drawText(
                    textMeasurer = textMeasure,
                    text = "+0\n-0",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    ),
                    topLeft = Offset(
                        center.x - minus2TextSize.width / 2f,
                        borderWidth * 3 / 2 + turnTableSize / 4f - minus2TextSize.height / 2f - 12.dp.toPx()
                    )
                )
            }
        }
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
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(CircleShape)
                    .shadow(8.dp)
                    .background(Color.Red)
                    .rippleClickable { isStarting = true }
                    .padding(12.dp),
                text = stringResource(R.string.bet_turn_table_start),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }

    LaunchedEffect(isStarting) {
        if (isStarting) {
            while (remainTime > 0 || currentAngle != finishAngle) {
                val step = when {
                    remainTime <= 0 -> {
                        if (
                            (currentAngle > finishAngle && 360 - currentAngle + finishAngle > 180) ||
                            finishAngle - currentAngle > 180
                        ) {
                            2
                        } else {
                            1
                        }
                    }
                    else -> (remainTime * 2 / 1000).coerceIn(2, 10)
                }
                val delay = if (remainTime <= 0) 25 else 15
                delay(delay.toLong())
                remainTime -= delay
                currentAngle = if (remainTime <= 0 && currentAngle < finishAngle) {
                    (currentAngle + step).coerceAtMost(finishAngle)
                } else {
                    (currentAngle + step) % 360
                }
            }
            delay(500)
            when (finishAngle) {
                in 0f..89f -> {
                    onReward(-abs(winPoint) + abs(losePoint))
                }
                in 90f..179f -> {
                    onReward(abs(winPoint) * 4)
                }
                in 180f..269f -> {
                    onReward(-abs(winPoint))
                }
                else -> {
                    onReward(abs(winPoint) + abs(losePoint))
                }
            }
        }
    }
    BackHandler {
        if (!isStarting) {
            onClose()
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

@Composable
private fun RewardPointDialog(
    rewardPoints: Long,
    onDismiss: () -> Unit
) {
    AlertDialog(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.secondary,
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(if (rewardPoints >= 0) R.string.bet_reward_win_title else R.string.bet_reward_lose_title),
                color = MaterialTheme.colors.primary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
        },
        text = {
            Text(
                text = stringResource(
                    if (rewardPoints >= 0) R.string.bet_reward_win_text else R.string.bet_reward_lose_text,
                    abs(rewardPoints)
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