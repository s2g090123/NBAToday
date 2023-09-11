package com.jiachian.nbatoday.compose.widget

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.jiachian.nbatoday.utils.color
import com.jiachian.nbatoday.utils.noRippleClickable

@Composable
fun RefreshingScreen(
    modifier: Modifier = Modifier,
    color: Color
) {
    Box(
        modifier = modifier.then(
            Modifier.noRippleClickable { }
        )
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = color
        )
    }
    BackHandler {
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
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.then(
            Modifier
                .focusRequester(focusRequester)
                .onFocusChanged { isFocus = it.isFocused }
                .border(
                    borderWidth,
                    if (isError) errorColor else if (isFocus) textColor else borderColor,
                    shape
                )
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
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (hint, startIcon, endIcon, textField) = createRefs()
            Box(
                modifier = Modifier
                    .constrainAs(startIcon) {
                        linkTo(top = parent.top, bottom = parent.bottom)
                        start.linkTo(parent.start)
                    }
            ) {
                if (leadingIcon != null) {
                    leadingIcon()
                }
            }
            Box(
                modifier = Modifier
                    .constrainAs(endIcon) {
                        linkTo(top = parent.top, bottom = parent.bottom)
                        end.linkTo(parent.end)
                    }
            ) {
                if (trailingIcon != null) {
                    trailingIcon()
                }
            }
            Box(
                modifier = Modifier
                    .constrainAs(hint) {
                        val startMargin = if (leadingIcon != null) 0.dp else 16.dp
                        val endMargin = if (trailingIcon != null) 0.dp else 16.dp
                        linkTo(start = startIcon.end, end = endIcon.start, startMargin, endMargin)
                        linkTo(top = parent.top, bottom = parent.bottom)
                        width = Dimension.fillToConstraints
                    }
            ) {
                if (placeHolder != null && value.isEmpty()) {
                    placeHolder()
                }
            }
            Box(
                modifier = Modifier
                    .constrainAs(textField) {
                        val startMargin = if (leadingIcon != null) 0.dp else 16.dp
                        val endMargin = if (trailingIcon != null) 0.dp else 16.dp
                        linkTo(start = startIcon.end, end = endIcon.start, startMargin, endMargin)
                        linkTo(top = parent.top, bottom = parent.bottom)
                        width = Dimension.fillToConstraints
                    }
            ) {
                it()
            }
        }
    }
}
