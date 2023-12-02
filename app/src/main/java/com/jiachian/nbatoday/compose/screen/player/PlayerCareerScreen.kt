package com.jiachian.nbatoday.compose.screen.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.player.info.PlayerCareerInfo
import com.jiachian.nbatoday.compose.screen.player.stats.PlayerCareerStats
import com.jiachian.nbatoday.compose.widget.FocusableColumn
import com.jiachian.nbatoday.compose.widget.IconButton
import com.jiachian.nbatoday.compose.widget.LoadingScreen

@Composable
fun PlayerCareerScreen(
    viewModel: PlayerViewModel,
    onBack: () -> Unit,
) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val notFoundVisible by viewModel.notFoundVisible.collectAsState()
    FocusableColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary),
    ) {
        IconButton(
            modifier = Modifier
                .testTag("PlayerCareerScreen_Btn_Back")
                .padding(top = 8.dp, start = 8.dp),
            drawableRes = R.drawable.ic_black_back,
            tint = MaterialTheme.colors.secondary,
            onClick = onBack
        )
        when {
            isRefreshing -> {
                LoadingScreen(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.secondary,
                    interceptBack = false
                )
            }
            notFoundVisible -> {
                PlayerCareerNotFound(modifier = Modifier.fillMaxSize())
            }
            else -> {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    PlayerCareerInfo(viewModel = viewModel)
                    PlayerCareerStats(
                        modifier = Modifier.fillMaxWidth(),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerCareerNotFound(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(R.string.player_career_not_found),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
    }
}
