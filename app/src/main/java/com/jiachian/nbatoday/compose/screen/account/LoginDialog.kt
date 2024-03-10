package com.jiachian.nbatoday.compose.screen.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.testing.testtag.UserTestTag
import com.jiachian.nbatoday.utils.color

@Composable
fun LoginDialog(
    onLogin: (account: String, password: String) -> Unit,
    onRegister: (account: String, password: String) -> Unit,
    onDismiss: () -> Unit
) {
    var account by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val valid by remember {
        derivedStateOf { account.isNotBlank() && password.isNotBlank() }
    }
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .testTag(UserTestTag.LoginDialog)
                .width(IntrinsicSize.Min)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginTextFiled(
                modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp),
                password = false,
                value = account,
                onValueChanged = { account = it }
            )
            LoginTextFiled(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                password = true,
                value = password,
                onValueChanged = { password = it }
            )
            BottomButtons(
                enabled = valid,
                onRegister = { onRegister(account, password) },
                onLogin = { onLogin(account, password) }
            )
        }
    }
}

@Composable
private fun LoginTextFiled(
    modifier: Modifier = Modifier,
    password: Boolean,
    value: String,
    onValueChanged: (String) -> Unit
) {
    val hintVisible by remember(value) { derivedStateOf { value.isEmpty() } }
    Column(modifier = modifier) {
        BasicTextField(
            modifier = Modifier.testTag(if (password) UserTestTag.PasswordTextField_TextField else UserTestTag.AccountTextField_TextField),
            value = value,
            onValueChange = onValueChanged,
            singleLine = true,
            maxLines = 1,
            visualTransformation = if (password) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(imeAction = if (password) ImeAction.Done else ImeAction.Next),
            textStyle = TextStyle(
                color = "#de000000".color,
                fontSize = 18.sp,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Start
            ),
            decorationBox = { innerTextField ->
                if (hintVisible) {
                    Text(
                        text = stringResource(if (password) R.string.user_login_password_hint else R.string.user_login_account_hint),
                        color = "#40000000".color,
                        fontSize = 18.sp
                    )
                }
                innerTextField()
            }
        )
        Divider(
            modifier = Modifier.padding(top = 1.dp),
            color = "#de000000".color
        )
    }
}

@Composable
private fun BottomButtons(
    enabled: Boolean,
    onRegister: () -> Unit,
    onLogin: () -> Unit
) {
    Row {
        Button(
            modifier = Modifier
                .testTag(UserTestTag.BottomButtons_Button_Register)
                .padding(top = 16.dp, bottom = 16.dp, start = 16.dp)
                .width(120.dp),
            onClick = onRegister,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary
            )
        ) {
            Text(
                text = stringResource(R.string.user_register),
                color = MaterialTheme.colors.primaryVariant
            )
        }
        Button(
            modifier = Modifier
                .testTag(UserTestTag.BottomButtons_Button_Login)
                .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                .width(120.dp),
            enabled = enabled,
            onClick = onLogin,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.secondary
            )
        ) {
            Text(
                text = stringResource(R.string.user_login),
                color = MaterialTheme.colors.secondaryVariant
            )
        }
    }
}
