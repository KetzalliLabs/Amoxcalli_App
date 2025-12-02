package com.req.software.amoxcalli_app.ui.screens.exercises.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
//new imports
import android.net.Uri
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import coil.compose.AsyncImage
import com.req.software.amoxcalli_app.ui.theme.MainColor
/**
 * Tipos de media que puede mostrar un ejercicio
 */
enum class MediaType {
    VIDEO,
    IMAGE,
    NONE
}

/**
 * Componente que decide automáticamente si mostrar video o imagen
 * según el tipo de media del ejercicio
 */
@Composable
fun MediaDisplay(
    mediaType: MediaType,
    imageUrl: String? = null,
    videoUrl: String? = null,
    modifier: Modifier = Modifier
) {
    // El Card exterior asegura que el contenido (imagen o video) tenga el mismo borde y fondo.
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f) // Proporción panorámica para video e imagen
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black) // Fondo negro para el reproductor de video
            .border(
                width = 2.dp,
                color = Color(0xFFC9CCD1), // special2_color
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ){
        when (mediaType) {
            MediaType.VIDEO -> VideoPlayer(
                videoUrl = videoUrl,
                // El modifier fillMaxSize se aplica dentro para que el video llene el Card
                modifier = Modifier.fillMaxSize()
            )
            MediaType.IMAGE -> ImageDisplay(
                imageUrl = imageUrl,
                modifier = Modifier.fillMaxSize()
            )
            MediaType.NONE -> {
                // Placeholder si no hay media, para mantener el espacio
                Text(text = "🖼️", fontSize = 48.sp, color = Color.White)
            }
        }
    }
}

@Composable
private fun VideoPlayer(
    videoUrl: String?,
    modifier: Modifier = Modifier
) {
    if (videoUrl.isNullOrEmpty()) {
        // Fallback si no hay URL de video
        Box(
            modifier = modifier.background(Color(0xFFE0E0E0)),
            contentAlignment = Alignment.Center
        ) {
            Text("🎥 Video no disponible", color = Color.Black)
        }
        return
    }

    val context = LocalContext.current

    // --- Video Player ---
    val exoPlayer = remember(videoUrl) {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true // Autoplay
            repeatMode = Player.REPEAT_MODE_ONE // Loop por defecto
        }
    }

    // --- Control States ---
    var slowMode by remember { mutableStateOf(false) }
    var loopEnabled by remember { mutableStateOf(true) } // Loop activado por defecto

    // Gestiona el ciclo de vida del player
    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Contenedor del reproductor de video
        Box(modifier = Modifier.weight(1f)) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        // Desactivamos el controlador por defecto para usar los nuestros
                        useController = false
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }


        // --- Fila de Controles Personalizados ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.6f)) // Fondo semitransparente para los controles
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- BOTÓN CÁMARA LENTA ---
            IconButton(
                onClick = {
                    slowMode = !slowMode
                    exoPlayer.setPlaybackSpeed(if (slowMode) 0.5f else 1.0f)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.SlowMotionVideo,
                    contentDescription = "Cámara Lenta",
                    tint = if (slowMode) MainColor else Color.White
                )
            }

            // --- BOTÓN LOOP ---
            IconButton(
                onClick = {
                    loopEnabled = !loopEnabled
                    exoPlayer.repeatMode =
                        if (loopEnabled) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh, // Usamos Refresh como ícono para loop
                    contentDescription = "Repetir",
                    tint = if (loopEnabled) MainColor else Color.White
                )
            }
        }
    }
}

/**
 * Muestra una imagen desde URL
 * Usa Coil para cargar la imagen
 */
@Composable
fun ImageDisplay(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    if (!imageUrl.isNullOrEmpty()) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Imagen del ejercicio",
            modifier = modifier,
            contentScale = ContentScale.Fit
        )
    } else {
        // Placeholder si la URL de la imagen es nula pero el tipo es IMAGE
        Text(text = "🖼️", fontSize = 48.sp)
    }
}

/**
 * Botón de opción con IMAGEN
 * Para ejercicios donde las opciones son imágenes de señas
 */
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
                color = if (selected) Color(0xFF004225) else Color(0xFFC9CCD1), // special3_color : special2_color
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                if (selected) Color(0xFFE8F5E9) else Color.White
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (!imageUrl.isNullOrEmpty()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Opción de seña",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentScale = ContentScale.Fit
            )
        } else {
            Text(text = "✋", fontSize = 32.sp)
        }
    }
}


/**
 * Botón de opción con VIDEO
 * Para ejercicios donde las opciones son videos de señas
 */

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
                color = if (selected) Color(0xFF004225) else Color(0xFFC9CCD1), // special3_color : special2_color
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                if (selected) Color(0xFFE8F5E9) else Color(0xFFF8F6EF) // main_color
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // Se mantiene el placeholder simple para los botones de opción.
        // Implementar un mini-reproductor aquí sería computacionalmente costoso.
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
            Text(
                text = "Video",
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
    }
}
/**
 * Placeholder temporal para videos
 */

/*                  OLD VIDEO PLACEHOLDER
@Composable
private fun VideoPlaceholder(
    videoUrl: String? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE0E0E0)),
        contentAlignment = Alignment.Center
    ) {
        // Controles de video simulados
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🎥")
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Reproducir",
                tint = Color.Black
            )
            Text("🔊")
        }
    }


}                    */

@Composable
private fun VideoPlaceholder(
    videoUrl: String? = null,
    modifier: Modifier = Modifier
) {
    if (videoUrl.isNullOrEmpty()) {
        // fallback when no video URL
        Box(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE0E0E0)),
            contentAlignment = Alignment.Center
        ) {
            // Controles de video simulados
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🎥")
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Reproducir",
                    tint = Color.Black
                )
                Text("🔊")
            }
        }
    } else {
        val context = LocalContext.current
        val exoPlayer = remember(videoUrl) {
            ExoPlayer.Builder(context).build().apply {
                val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = true
            }
        }

        DisposableEffect(exoPlayer) {
            onDispose {
                exoPlayer.release()
            }
        }

        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = true
                    // optional: adjust controller show timeout etc.
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black)
        )
    }
}