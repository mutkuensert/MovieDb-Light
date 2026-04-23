package core.ui.component

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

/**
 * Add `android:configChanges="orientation|screenSize"` for owner activity in the manifest file.
 *
 * For example if you want the player to be rotatable when it's fullscreen and also your app
 * has to be in portrait mode when it's not:
 * ```
 * YoutubeVideoPlayer(
 *     videoId = "youtube_video_id",
 *     fullscreenConfig = FullscreenConfig(
 *         orientationOnEnterFullscreen = ActivityInfo.SCREEN_ORIENTATION_SENSOR,
 *         orientationOnExitFullscreen = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
 *     )
 * )
 * ```
 * @param previewOverlay a composable that will be shown on top of the original player
 * and the player will start playing video when this composable is clicked.
 * The overlay will be constrained to a 16:9 aspect ratio.
 */
@Composable
fun YoutubeVideoPlayer(
    videoId: String,
    modifier: Modifier = Modifier,
    fullscreenConfig: FullscreenConfig = FullscreenConfig.default(),
    previewOverlay: @Composable (() -> Unit)? = null,
) {
    val activity = LocalActivity.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var showsPreviewOverlay by rememberSaveable { mutableStateOf(true) }
    var playerView: YouTubePlayerView? by remember { mutableStateOf(null) }
    var fullscreenView: View? by remember { mutableStateOf(null) }
    var player: YouTubePlayer? by remember { mutableStateOf(null) }
    val tracker = remember { YouTubePlayerTracker() }
    val playerListener = remember {
        object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.addListener(tracker)
                player = youTubePlayer
            }
        }
    }
    val exitFullscreen = {
        activity?.showSystemBars()
        fullscreenConfig.orientationOnExitFullscreen?.let {
            activity?.requestedOrientation = it
        }
        fullscreenView?.removeFromParent()
        fullscreenView = null
    }

    if (fullscreenView != null) {
        BackHandler { exitFullscreen() }
    }

    Box(
        modifier,
        contentAlignment = Alignment.Center,
    ) {
        AndroidView(
            factory = { context ->
                val view = createPlayerView(
                    context,
                    lifecycleOwner,
                    onEnterFullscreen = { view ->
                        fullscreenConfig.orientationOnEnterFullscreen?.let {
                            activity?.requestedOrientation = it
                        }
                        fullscreenView = view
                        activity?.enableTransientSystemBars()
                        activity?.addViewMatchingScreen(view)

                    },
                    exitFullscreen,
                    playerListener
                )
                playerView = view
                view
            }
        )

        if (previewOverlay != null && showsPreviewOverlay) {
            Box(
                Modifier
                    .aspectRatio(16f / 9f)
                    .clickable { showsPreviewOverlay = false },
            ) {
                previewOverlay()
            }
        }
    }

    LaunchedEffect(showsPreviewOverlay, player, videoId) {
        if (!showsPreviewOverlay && previewOverlay != null) {
            player?.loadOrCueVideo(lifecycleOwner.lifecycle, videoId, 0f)
        } else {
            player?.cueVideo(videoId, 0f)
        }
    }

    DisposableEffect(lifecycleOwner, activity) {
        onDispose {
            fullscreenView?.removeFromParent()
            fullscreenView = null
            playerView?.let { lifecycleOwner.lifecycle.removeObserver(it) }
            playerView?.release()
            playerView = null
            showsPreviewOverlay = true
        }
    }
}

private fun Activity.enableTransientSystemBars() {
    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    windowInsetsController.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
}

private fun Activity.showSystemBars() {
    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
    windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
}

private fun createPlayerView(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    onEnterFullscreen: (View) -> Unit,
    onExitFullscreen: () -> Unit,
    listener: AbstractYouTubePlayerListener,
): YouTubePlayerView {
    val youTubePlayerView = YouTubePlayerView(context)
    youTubePlayerView.enableAutomaticInitialization = false
    lifecycleOwner.lifecycle.addObserver(youTubePlayerView)
    youTubePlayerView.addFullscreenListener(object : FullscreenListener {
        override fun onEnterFullscreen(
            fullscreenView: View,
            exitFullscreen: () -> Unit
        ) {
            onEnterFullscreen(fullscreenView)
        }

        override fun onExitFullscreen() {
            onExitFullscreen()
        }
    })

    val options = IFramePlayerOptions.Builder(context)
        .controls(1)
        .rel(0)
        .fullscreen(1)
        .ivLoadPolicy(3)
        .build()

    youTubePlayerView.initialize(listener, options)
    return youTubePlayerView
}

private fun Activity.addViewMatchingScreen(view: View) {
    val decor = window.decorView as ViewGroup
    decor.addView(
        view,
        ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    )
}

private fun View.removeFromParent() {
    (this.parent as? ViewGroup)?.removeView(this)
}

data class FullscreenConfig(
    val orientationOnEnterFullscreen: Int?,
    val orientationOnExitFullscreen: Int?
) {

    companion object {
        fun default(): FullscreenConfig {
            return FullscreenConfig(
                orientationOnEnterFullscreen = null,
                orientationOnExitFullscreen = null
            )
        }
    }
}