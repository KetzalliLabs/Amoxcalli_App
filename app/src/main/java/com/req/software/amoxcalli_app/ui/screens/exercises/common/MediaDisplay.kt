package com.req.software.amoxcalli_app.ui.screens.exercises.common

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SlowMotionVideo
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.req.software.amoxcalli_app.ui.theme.MainColor


enum class MediaType {
    VIDEO,
    IMAGE,
    NONE
}

@OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun MediaDisplay(
    mediaType: MediaType,
    imageUrl: String? = null,
    videoUrl: String? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    // Estados de los controles
    var slowMode by remember { mutableStateOf(false) }
    var loopEnabled by remember { mutableStateOf(true) }
    var isPlaying by remember { mutableStateOf(false) }
    var hasError by remember { mutableStateOf(false) }

    // Optimized Video Player with proper configuration
    val exoPlayer = remember(videoUrl) {
        if (mediaType == MediaType.VIDEO && videoUrl != null) {
            try {
                ExoPlayer.Builder(context)
                    .setLoadControl(
                        androidx.media3.exoplayer.DefaultLoadControl.Builder()
                            .setBufferDurationsMs(
                                500,  // Min buffer (500ms)
                                2000, // Max buffer (2s)
                                500,  // Buffer for playback
                                500   // Buffer after rebuffer
                            )
                            .build()
                    )
                    .build()
                    .apply {
                        val mediaItem = MediaItem.Builder()
                            .setUri(Uri.parse(videoUrl))
                            .build()
                        setMediaItem(mediaItem)
                        prepare()
                        playWhenReady = false // Don't autoplay to reduce load
                        repeatMode = Player.REPEAT_MODE_ONE

                        // Error listener
                        addListener(object : Player.Listener {
                            override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                                hasError = true
                                android.util.Log.e("MediaDisplay", "Video error: ${error.message}")
                            }

                            override fun onIsPlayingChanged(playing: Boolean) {
                                isPlaying = playing
                            }
                        })
                    }
            } catch (e: Exception) {
                hasError = true
                android.util.Log.e("MediaDisplay", "Failed to create player", e)
                null
            }
        } else {
            null
        }
    }

    // Lifecycle awareness - pause when app goes to background
    DisposableEffect(lifecycleOwner, exoPlayer) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            when (event) {
                androidx.lifecycle.Lifecycle.Event.ON_PAUSE -> {
                    exoPlayer?.pause()
                }
                androidx.lifecycle.Lifecycle.Event.ON_STOP -> {
                    exoPlayer?.pause()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Release player when leaving screen
    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer?.stop()
            exoPlayer?.release()
        }
    }

    // ✅ PASO 2: CONTENEDOR DE TAMAÑO MÁS GRANDE
    Box(
        modifier = modifier
            .fillMaxWidth()
            // Le damos el 40% de la altura de la pantalla, mucho más grande
            .fillMaxHeight(0.4f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black)
            .border(
                width = 2.dp,
                color = Color(0xFFC9CCD1),
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        when (mediaType) {
            MediaType.VIDEO -> {
                if (hasError || exoPlayer == null) {
                    VideoErrorPlaceholder()
                } else {
                    VideoPlayer(
                        exoPlayer = exoPlayer,
                        slowMode = slowMode,
                        loopEnabled = loopEnabled,
                        hasError = hasError,
                        onSlowModeToggle = {
                            slowMode = !slowMode
                            exoPlayer.setPlaybackSpeed(if (slowMode) 0.5f else 1.0f)
                        },
                        onLoopToggle = {
                            loopEnabled = !loopEnabled
                            exoPlayer.repeatMode = if (loopEnabled) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
                        },
                        onPlayPauseToggle = {
                            if (exoPlayer.isPlaying) {
                                exoPlayer.pause()
                            } else {
                                exoPlayer.play()
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            MediaType.IMAGE -> ImageDisplay(imageUrl = imageUrl, modifier = Modifier.fillMaxSize())
            MediaType.NONE -> Text(text = "🖼️", fontSize = 48.sp, color = Color.White)
        }
    }
}

/**
 * Optimized Video Player component with proper error handling and performance
 */
@Composable
private fun VideoPlayer(
    exoPlayer: ExoPlayer,
    slowMode: Boolean,
    loopEnabled: Boolean,
    hasError: Boolean,
    onSlowModeToggle: () -> Unit,
    onLoopToggle: () -> Unit,
    onPlayPauseToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.weight(1f)) {
            if (hasError) {
                // Show error state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "⚠️", fontSize = 48.sp)
                        Text(
                            text = "Error al cargar video",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                // Video Player View - Optimized
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            player = exoPlayer
                            useController = true
                            // Show buffering indicator
                            setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                        }
                    },
                    update = { playerView ->
                        playerView.player = exoPlayer
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onPlayPauseToggle
                        )
                )
            }
        }

        // Controls - only show if no error
        if (!hasError) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Slow motion button
                IconButton(onClick = onSlowModeToggle) {
                    Icon(
                        imageVector = Icons.Default.SlowMotionVideo,
                        contentDescription = "Cámara Lenta",
                        tint = if (slowMode) MainColor else Color.Gray
                    )
                }

                // Loop button
                IconButton(onClick = onLoopToggle) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Repetir",
                        tint = if (loopEnabled) MainColor else Color.Gray
                    )
                }
            }
        }
    }
}

// ... (El resto del archivo: VideoErrorPlaceholder, ImageDisplay, etc., no cambian) ...

@Composable
private fun VideoErrorPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF424242)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "🎥",
                fontSize = 48.sp
            )
            Text(
                text = "Video no disponible",
                color = Color.White,
                fontSize = 16.sp
            )
            Text(
                text = "Intenta de nuevo más tarde",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun ImageDisplay(imageUrl: String?, modifier: Modifier = Modifier) {
    if (!imageUrl.isNullOrEmpty()) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Imagen del ejercicio",
            modifier = modifier,
            contentScale = ContentScale.Fit
        )
    } else {
        Text(text = "🖼️", fontSize = 48.sp)
    }
}

@Composable
fun GameImageButton(
    imageUrl: String?,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 3.dp,
                color = if (selected) Color(0xFF004225) else Color(0xFFC9CCD1),
                shape = RoundedCornerShape(16.dp)
            )
            .background(if (selected) Color(0xFFE8F5E9) else Color.White)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (!imageUrl.isNullOrEmpty()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Opción de seña",
                modifier = Modifier.fillMaxSize().padding(8.dp),
                contentScale = ContentScale.Fit
            )
        } else {
            Text(text = "✋", fontSize = 32.sp)
        }
    }
}


@Composable
fun GameVideoButton(
    videoUrl: String?,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 3.dp,
                color = if (selected) Color(0xFF004225) else Color(0xFFC9CCD1),
                shape = RoundedCornerShape(16.dp)
            )
            .background(if (selected) Color(0xFFE8F5E9) else Color(0xFFF8F6EF))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Reproducir video",
                modifier = Modifier.size(32.dp),
                tint = Color.Black
            )
            Text(text = "Video", fontSize = 10.sp, color = Color.Gray)
        }
    }
}
