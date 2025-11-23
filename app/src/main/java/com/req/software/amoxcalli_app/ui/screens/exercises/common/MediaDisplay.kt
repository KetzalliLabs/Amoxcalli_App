package com.req.software.amoxcalli_app.ui.screens.exercises.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
    when (mediaType) {
        MediaType.VIDEO -> VideoPlaceholder(
            videoUrl = videoUrl,
            modifier = modifier
        )
        MediaType.IMAGE -> ImageDisplay(
            imageUrl = imageUrl,
            modifier = modifier
        )
        MediaType.NONE -> { /* No mostrar nada */ }
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
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(4f / 3f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(
                width = 2.dp,
                color = Color(0xFFC9CCD1), // special2_color
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (!imageUrl.isNullOrEmpty()) {
            // TODO: Agregar dependencia de Coil en build.gradle.kts
            // implementation("io.coil-kt:coil-compose:2.5.0")
            AsyncImage(
                model = imageUrl,
                contentDescription = "Imagen del ejercicio",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        } else {
            Text(text = "🖼️", fontSize = 48.sp)
        }
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
        // TODO: Integrar ExoPlayer para reproducir videos
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
}