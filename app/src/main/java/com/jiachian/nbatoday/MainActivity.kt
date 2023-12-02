package com.jiachian.nbatoday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.jiachian.nbatoday.compose.screen.main.MainScreen
import com.jiachian.nbatoday.compose.theme.NBATodayTheme
import com.jiachian.nbatoday.utils.LocalActivity
import kotlinx.coroutines.launch
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
                NBATodayTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.primary
                    ) {
                        MainScreen(viewModel)
                    }
                }
            }
        }
        viewModel.loadData()

        lifecycleScope.launch {
            viewModel.eventFlow
                .flowWithLifecycle(lifecycle)
                .collect {
                    onEvent(it)
                }
        }
    }

    private fun onEvent(event: MainViewModel.Event?) {
        when (event) {
            MainViewModel.Event.Exit -> finish()
            else -> {}
        }
        viewModel.onEventConsumed(event)
    }
}
