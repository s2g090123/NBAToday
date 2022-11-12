package com.jiachian.nbatoday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.compose.theme.NBATodayTheme
import com.jiachian.nbatoday.utils.LocalActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Ltr,
                LocalTextStyle provides LocalTextStyle.current.copy(textDirection = TextDirection.Ltr),
                LocalActivity provides this
            ) {

            }
            NBATodayTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NbaScreen(viewModel)
                }
            }
        }
    }
}

@Composable
private fun NbaScreen(viewModel: MainViewModel) {
    val isLoadingApp by viewModel.isLoadingApp.collectAsState()

    if (isLoadingApp) {
        SplashScreen()
    } else {
        MainScreen(viewModel)
    }
}

@Composable
private fun SplashScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = MaterialTheme.colors.secondary)
        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = stringResource(R.string.is_loading_app),
            color = MaterialTheme.colors.secondary
        )
    }
}

@Composable
private fun MainScreen(
    viewModel: MainViewModel
) {
    Scaffold() {

    }
}