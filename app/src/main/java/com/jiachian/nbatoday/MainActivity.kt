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
import com.jiachian.nbatoday.event.ToastEvent
import com.jiachian.nbatoday.event.toastEventManager
import com.jiachian.nbatoday.utils.LocalActivity
import com.jiachian.nbatoday.utils.showErrorToast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
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
                        MainScreen()
                    }
                }
            }
        }
        toastEventManager.eventFlow.collectWithLifecycle(this::onToastEvent)
    }

    /**
     * Handles toast events received from the toast event manager.
     */
    private fun onToastEvent(event: ToastEvent) {
        when (event) {
            ToastEvent.OnError -> showErrorToast()
        }
    }

    private fun <T> Flow<T>.collectWithLifecycle(collector: FlowCollector<T>) {
        lifecycleScope.launch {
            flowWithLifecycle(lifecycle).collect(collector)
        }
    }
}
