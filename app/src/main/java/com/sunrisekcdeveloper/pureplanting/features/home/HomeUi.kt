package com.sunrisekcdeveloper.pureplanting.features.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.pureplanting.core.design.theme.ppColors
import com.sunrisekcdeveloper.pureplanting.core.design.theme.ppTypography
import com.sunrisekcdeveloper.pureplanting.core.design.ui.PlantBox
import com.sunrisekcdeveloper.pureplanting.core.design.ui.SnackbarEmitter
import com.sunrisekcdeveloper.pureplanting.core.design.ui.SnackbarEmitterType
import com.sunrisekcdeveloper.pureplanting.core.design.ui.ThemeSurfaceWrapper
import com.sunrisekcdeveloper.pureplanting.features.home.ui.NotificationIconUi
import com.sunrisekcdeveloper.pureplanting.features.home.ui.PlantListUi
import com.zhuinden.liveevent.observe
import kotlinx.coroutines.launch

@Composable
fun HomeUi(
    viewModel: HomeViewModel,
    snackbarEmitter: SnackbarEmitter,
) {

    val isNotificationBadgeVisible by viewModel.isNotificationBadgeVisible.collectAsState()

    SnackbarWrapper(snackbarEmitter) {
        PlantBox {
            Column {
                Row(
                    Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 35.dp)
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Let's Care\nMy Plants!",
                        style = MaterialTheme.ppTypography.title,
                        color = MaterialTheme.ppColors.onSurfacePrimary,
                        modifier = Modifier.weight(1f)
                    )
                    NotificationIconUi(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(42.dp), // todo update notification icon to scale predictably (try to set size smaller)
                        isBadgeVisible = isNotificationBadgeVisible,
                        onClick = { viewModel.onNotificationIconClick() }
                    )
                }
                PlantListUi(
                    viewModel = viewModel,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun SnackbarWrapper(
    emitter: SnackbarEmitter,
    content: @Composable () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color.Transparent
    ) { _ ->

        val lifecycleOwner = LocalLifecycleOwner.current
        LaunchedEffect(lifecycleOwner.lifecycle) {
           emitter.snackbarEvents.observe(lifecycleOwner) { eventType ->
                scope.launch {
                    when (eventType) {
                        is SnackbarEmitterType.Text -> snackbarHostState.showSnackbar(eventType.text)
                        is SnackbarEmitterType.TextRes -> snackbarHostState.showSnackbar(context.getString(eventType.resId))
                        is SnackbarEmitterType.Undo -> {
                            val result = snackbarHostState.showSnackbar(
                                message = eventType.text,
                                actionLabel = "Undo",
                                duration = SnackbarDuration.Long,
                            )

                            when (result) {
                                SnackbarResult.ActionPerformed -> eventType.undoAction()
                                SnackbarResult.Dismissed -> {}
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun HomeUi_Preview() {
    ThemeSurfaceWrapper {
        HomeUi(HomeViewModel.Fake(), SnackbarEmitter())
    }
}