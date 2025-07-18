package com.jabulani.ligiopen.ui.utils.video

import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.jabulani.ligiopen.R

@OptIn(UnstableApi::class)
@Composable
fun VideoWithControls(
    videoUrl: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as? AppCompatActivity
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
            playWhenReady = false
        }
    }
    DisposableEffect(exoPlayer) {
        onDispose { exoPlayer.release() }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                useController = true
                controllerShowTimeoutMs = 3000
                // Show only rewind/fast-forward, hide next/previous
                setShowRewindButton(true)
                setShowFastForwardButton(true)
                setShowPreviousButton(false)
                setShowNextButton(false)
                // Optional: adjust skip increments
//                setRewindIncrementMs(5000)
//                setFastForwardIncrementMs(15000)
            }
        },
        update = { it.player = exoPlayer },
        modifier = modifier
    )
}

@Composable
fun FullscreenVideoScreen(videoUrl: String) {
    val context = LocalContext.current
    val activity = context as? AppCompatActivity
    var isFullscreen by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        VideoWithControls(
            videoUrl = videoUrl,
            modifier = if (isFullscreen) Modifier.fillMaxSize()
            else Modifier
                .fillMaxWidth()
                .height(240.dp)
                .align(Alignment.TopCenter)
        )

        IconButton(
            onClick = {
                isFullscreen = !isFullscreen
                activity?.window?.let { window ->
                    val decorView = window.decorView
                    if (isFullscreen) {
                        // Enter fullscreen: hide system bars and action bar
                        WindowCompat.setDecorFitsSystemWindows(window, false)
                        WindowInsetsControllerCompat(window, decorView)
                            .hide(WindowInsetsCompat.Type.systemBars())
                        activity.supportActionBar?.hide()
                    } else {
                        // Exit fullscreen: show system bars and action bar
                        WindowInsetsControllerCompat(window, decorView)
                            .show(WindowInsetsCompat.Type.systemBars())
                        WindowCompat.setDecorFitsSystemWindows(window, true)
                        activity.supportActionBar?.show()
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
        ) {
            Icon(
                painter = painterResource(id = if (isFullscreen) R.drawable.minimize else R.drawable.expand),
                contentDescription = if (isFullscreen) "Exit Fullscreen" else "Fullscreen"
            )
        }
    }
}
