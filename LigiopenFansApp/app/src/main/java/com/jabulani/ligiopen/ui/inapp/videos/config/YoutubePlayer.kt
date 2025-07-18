import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun YouTubePlayer(
    youtubeVideoId: String,
    lifecycleOwner: LifecycleOwner,
    shouldMute: Boolean = false,
    shouldAutoPlay: Boolean = true,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val playerView = remember {
        YouTubePlayerView(context).apply {
            lifecycleOwner.lifecycle.addObserver(this)
        }
    }

    DisposableEffect(playerView, youtubeVideoId) {
        val listener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                if (shouldMute) {
                    youTubePlayer.mute()
                } else {
                    youTubePlayer.unMute()
                }
                if (shouldAutoPlay) {
                    youTubePlayer.loadVideo(youtubeVideoId, 0f)
                } else {
                    youTubePlayer.cueVideo(youtubeVideoId, 0f)
                }
            }
        }

        playerView.addYouTubePlayerListener(listener)

        onDispose {
            playerView.removeYouTubePlayerListener(listener)
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { playerView }
    )
}