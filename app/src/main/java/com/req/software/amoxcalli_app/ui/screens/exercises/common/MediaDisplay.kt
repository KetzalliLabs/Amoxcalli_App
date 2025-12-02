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

@Composable
fun MediaDisplay(
    mediaType: MediaType,
    imageUrl: String? = null,
    videoUrl: String? = null,
    modifier: Modifier = Modifier
) {
    // ✅ PASO 1: ESTADO ELEVADO Y COMPLETO
    // Se declaran aquí todas las variables de estado para que persistan.
    val context = LocalContext.current

    val exoPlayer = remember(videoUrl) {
        if (mediaType == MediaType.VIDEO && videoUrl != null) {
            ExoPlayer.Builder(context).build().apply {
                setMediaItem(MediaItem.fromUri(Uri.parse(videoUrl)))
                prepare()
                playWhenReady = true // Autoplay
                repeatMode = Player.REPEAT_MODE_ONE
            }
        } else {
            null
        }
    }

    // Estados de los controles
    var slowMode by remember { mutableStateOf(false) }
    var loopEnabled by remember { mutableStateOf(true) }
    var isPlaying by remember(exoPlayer) { mutableStateOf(exoPlayer?.isPlaying ?: false) }

    // Listener para actualizar isPlaying
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }
        }
        exoPlayer?.addListener(listener)

        onDispose {
            exoPlayer?.removeListener(listener)
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
                if (exoPlayer != null) {
                    VideoPlayer(
                        exoPlayer = exoPlayer,
                        slowMode = slowMode,
                        loopEnabled = loopEnabled,
                        onSlowModeToggle = {
                            slowMode = !slowMode
                            exoPlayer.setPlaybackSpeed(if (slowMode) 0.5f else 1.0f)
                        },
                        onLoopToggle = {
                            loopEnabled = !loopEnabled
                            exoPlayer.repeatMode = if (loopEnabled) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
                        },
                        // ✅ PASO 3: PASAR EVENTO DE PAUSA/PLAY
                        onPlayPauseToggle = {
                            if (exoPlayer.isPlaying) {
                                exoPlayer.pause()
                            } else {
                                exoPlayer.play()
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    VideoErrorPlaceholder()
                }
            }
            MediaType.IMAGE -> ImageDisplay(imageUrl = imageUrl, modifier = Modifier.fillMaxSize())
            MediaType.NONE -> Text(text = "🖼️", fontSize = 48.sp, color = Color.White)
        }
    }
}

/**
 * Componente de Video "tonto" que ahora sí replica la interacción de WordDetailScreen.
 */
@Composable
private fun VideoPlayer(
    exoPlayer: ExoPlayer,
    slowMode: Boolean,
    loopEnabled: Boolean,
    onSlowModeToggle: () -> Unit,
    onLoopToggle: () -> Unit,
    onPlayPauseToggle: () -> Unit, // Recibe el nuevo evento
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.weight(1f)) {
            // Video Player View
            AndroidView(
                // ✅ PASO 4: HACER EL VIDEO CLICKEABLE PARA PAUSA/PLAY
                factory = {
                    PlayerView(it).apply {
                        player = exoPlayer
                        // AHORA SÍ HABILITAMOS EL CONTROLADOR NATIVO
                        useController = true
                        // Opcional: Ocultar el controlador por defecto y manejar todo manualmente
                        // useController = false
                        // setControllerAutoShow(false)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    // Si queremos un control manual de pausa/play al tocar
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null, // Sin efecto ripple
                        onClick = onPlayPauseToggle
                    )
            )
        }

        // Fila de controles personalizados
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // BOTÓN CÁMARA LENTA
            IconButton(onClick = onSlowModeToggle) {
                Icon(
                    imageVector = Icons.Default.SlowMotionVideo,
                    contentDescription = "Cámara Lenta",
                    // ✅ CORRECCIÓN FINAL: Usar Color.Gray para el estado inactivo
                    tint = if (slowMode) MainColor else Color.Gray
                )
            }

            // BOTÓN LOOP
            IconButton(onClick = onLoopToggle) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Repetir",
                    // ✅ CORRECCIÓN FINAL: Usar Color.Gray para el estado inactivo
                    tint = if (loopEnabled) MainColor else Color.Gray
                )
            }
        }
    }
}

// ... (El resto del archivo: VideoErrorPlaceholder, ImageDisplay, etc., no cambian) ...

@Composable
private fun VideoErrorPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize().background(Color(0xFFE0E0E0)),
        contentAlignment = Alignment.Center
    ) {
        Text("🎥 Video no disponible", color = Color.Black)
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
