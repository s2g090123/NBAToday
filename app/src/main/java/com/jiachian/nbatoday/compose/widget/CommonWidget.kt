package com.jiachian.nbatoday.compose.widget

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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

/**
 * Displays a loading screen with a circular progress indicator.
 *
 * @param modifier The modifier for the loading screen.
 * @param color The color of the circular progress indicator.
 * @param interceptBack Whether to intercept the back button press.
 */
@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    color: Color,
    interceptBack: Boolean = false,
) {
    FocusableBox(modifier = modifier) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = color
        )
    }
    BackHandler(interceptBack) {}
}

/**
 * Custom implementation of an outlined text field with additional customization options.
 */
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
    error: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(autoCorrect = false),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    shape: Shape = RoundedCornerShape(4.dp),
    borderWidth: Dp = 2.dp
) {
    val focusManager = LocalFocusManager.current
    var focus by remember { mutableStateOf(false) }
    val currentColor = if (error) errorColor else if (focus) textColor else borderColor
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.then(
            Modifier
                .focusRequester(focusRequester)
                .onFocusChanged { focus = it.isFocused }
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
        cursorBrush = SolidColor(if (error) errorColor else textColor)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            leadingIcon?.let {
                Box(modifier = Modifier.align(Alignment.CenterVertically)) {
                    it()
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                placeHolder
                    ?.takeIf { value.isEmpty() }
                    ?.let {
                        it()
                    }
                it()
            }
            trailingIcon?.let {
                Box(modifier = Modifier.align(Alignment.CenterVertically)) {
                    it()
                }
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Composable
inline fun FocusableBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.then(Modifier.noRippleClickable { })) {
        content()
    }
}

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    @DrawableRes drawableRes: Int,
    padding: Dp = 12.dp,
    enabled: Boolean = true,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    onClick: () -> Unit
) {
    androidx.compose.material.IconButton(
        modifier = modifier.then(
            Modifier
                .size(48.dp)
                .padding(padding)
        ),
        enabled = enabled,
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

/**
 * Displays the team logo image using an AsyncImage.
 *
 * @param modifier The modifier for the team logo image.
 * @param team The NBA team for which to display the logo.
 */
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

/**
 * Displays the player image using an AsyncImage.
 *
 * @param modifier The modifier for the player image.
 * @param playerId The ID of the player for which to display the image.
 */
@Composable
fun PlayerImage(
    modifier: Modifier = Modifier,
    playerId: Int?
) {
    AsyncImage(
        modifier = modifier,
        model = SvgRequest.Builder(LocalContext.current)
            .data(NBAUtils.getPlayerImageUrlById(playerId ?: 0))
            .build(),
        error = painterResource(R.drawable.ic_black_person),
        placeholder = painterResource(R.drawable.ic_black_person),
        contentDescription = null
    )
}

/**
 * Conditional rendering based on nullability of data.
 *
 * @param data The data to be checked for null.
 * @param ifNull Content to be displayed if data is null.
 * @param ifNotNull Content to be displayed if data is not null.
 */
@Composable
fun <T> NullCheckScreen(
    data: T?,
    ifNull: (@Composable () -> Unit)?,
    ifNotNull: (@Composable (T) -> Unit)?
) {
    data?.let {
        ifNotNull?.invoke(it)
    } ?: ifNull?.invoke()
}

/**
 * Animate the expansion of content.
 *
 * @param modifier The modifier for the AnimatedExpand.
 * @param visible Whether the content is currently visible.
 * @param content The content to be animated.
 */
@Composable
fun AnimatedExpand(
    modifier: Modifier = Modifier,
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
