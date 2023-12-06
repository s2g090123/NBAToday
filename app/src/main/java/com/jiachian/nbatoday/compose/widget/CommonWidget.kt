package com.jiachian.nbatoday.compose.widget

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.annotation.ExcludeFromJacocoGeneratedReport
import com.jiachian.nbatoday.compose.coil.SvgRequest
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.utils.NBAUtils
import com.jiachian.nbatoday.utils.color
import com.jiachian.nbatoday.utils.noRippleClickable

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    color: Color,
    interceptBack: Boolean = true,
    backButton: @Composable BoxScope.() -> Unit = {},
) {
    FocusableBox(modifier = modifier) {
        Box(
            modifier = Modifier.align(Alignment.TopStart),
            content = backButton
        )
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = color
        )
    }
    if (interceptBack) {
        BackHandler {
        }
    }
}

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester = remember { FocusRequester() },
    textColor: Color = MaterialTheme.colors.primary,
    errorColor: Color = "#dc2c00".color,
    borderColor: Color = MaterialTheme.colors.primary,
    enabled: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    placeHolder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(autoCorrect = false),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    shape: Shape = RoundedCornerShape(4.dp),
    borderWidth: Dp = 2.dp
) {
    val focusManager = LocalFocusManager.current
    var isFocus by remember { mutableStateOf(false) }
    val currentColor = if (isError) errorColor else if (isFocus) textColor else borderColor
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.then(
            Modifier
                .focusRequester(focusRequester)
                .onFocusChanged { isFocus = it.isFocused }
                .border(borderWidth, currentColor, shape)
        ),
        enabled = enabled,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardActions.onDone?.invoke(this)
                focusManager.clearFocus()
            },
            onGo = keyboardActions.onGo,
            onNext = keyboardActions.onNext,
            onPrevious = keyboardActions.onPrevious,
            onSearch = keyboardActions.onSearch,
            onSend = keyboardActions.onSearch
        ),
        singleLine = singleLine,
        maxLines = maxLines,
        cursorBrush = SolidColor(if (isError) errorColor else textColor)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            if (leadingIcon != null) {
                Box(modifier = Modifier.align(Alignment.CenterVertically)) {
                    leadingIcon()
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (placeHolder != null && value.isEmpty()) {
                    placeHolder()
                }
                it()
            }
            if (trailingIcon != null) {
                Box(modifier = Modifier.align(Alignment.CenterVertically)) {
                    trailingIcon()
                }
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Composable
inline fun FocusableColumn(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier.noRippleClickable { }) {
        content()
    }
}

@ExcludeFromJacocoGeneratedReport
@Composable
inline fun FocusableBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.noRippleClickable { }) {
        content()
    }
}

@Composable
fun BackHandle(
    enabled: Boolean = true,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    content()
    BackHandler(enabled) {
        onBack()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DisableOverscroll(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        content()
    }
}

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    @DrawableRes drawableRes: Int,
    padding: Dp = 12.dp,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    onClick: () -> Unit
) {
    androidx.compose.material.IconButton(
        modifier = modifier
            .size(48.dp)
            .padding(padding),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(drawableRes),
            contentDescription = null,
            tint = tint
        )
    }
}

@Composable
fun TeamLogoImage(
    modifier: Modifier = Modifier,
    team: NBATeam
) {
    AsyncImage(
        modifier = modifier,
        model = SvgRequest.Builder(LocalContext.current)
            .data(NBAUtils.getTeamLogoUrlById(team.teamId))
            .build(),
        error = painterResource(team.logoRes),
        placeholder = painterResource(team.logoRes),
        contentDescription = null
    )
}

@Composable
fun PlayerImage(
    modifier: Modifier = Modifier,
    playerId: Int?
) {
    val imageUrl by remember(playerId) {
        val url = playerId?.let { NBAUtils.getPlayerImageUrlById(it) }
        mutableStateOf(url)
    }
    AsyncImage(
        modifier = modifier,
        model = SvgRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .build(),
        error = painterResource(R.drawable.ic_black_person),
        placeholder = painterResource(R.drawable.ic_black_person),
        contentDescription = null
    )
}

@Composable
fun <T> NullCheckScreen(
    data: T?,
    ifNull: @Composable () -> Unit,
    ifNotNull: @Composable (T) -> Unit
) {
    if (data == null) {
        ifNull()
    } else {
        ifNotNull(data)
    }
}

@Composable
fun AnimatedExpand(
    modifier: Modifier,
    visible: Boolean,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = expandIn(),
        exit = shrinkOut()
    ) {
        content()
    }
}
