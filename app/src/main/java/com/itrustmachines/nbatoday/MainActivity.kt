package com.itrustmachines.nbatoday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.itrustmachines.nbatoday.compose.theme.NBATodayTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NBATodayTheme {
                // A surface container using the 'background' color from the theme
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

}