@file:androidx.media3.common.util.UnstableApi

package com.req.software.amoxcalli_app.ui.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.req.software.amoxcalli_app.data.dto.UserStatsResponse
import com.req.software.amoxcalli_app.ui.theme.ThirdColor
import com.req.software.amoxcalli_app.ui.theme.MainColor
import kotlinx.coroutines.launch
import com.req.software.amoxcalli_app.ui.theme.Special3Color
import com.req.software.amoxcalli_app.viewmodel.LibraryViewModel
import com.req.software.amoxcalli_app.viewmodel.CategoryViewModel
import com.req.software.amoxcalli_app.viewmodel.UserStatsViewModel
// new video imports
import android.net.Uri
import androidx.compose.material.icons.filled.SlowMotionVideo
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordDetailScreen(
    wordId: String,
    onClose: () -> Unit,
    libraryViewModel: LibraryViewModel = viewModel(),
    userStats: UserStatsResponse? = null,
    authToken: String? = null,
    categoryViewModel: CategoryViewModel = viewModel(),
    userStatsViewModel: UserStatsViewModel = viewModel(),
    viewedSignsViewModel: com.req.software.amoxcalli_app.viewmodel.ViewedSignsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val signs by libraryViewModel.signs.collectAsState()
    val word = signs.find { it.id == wordId }

    // Get viewed signs from local state
    val viewedSignIds by viewedSignsViewModel.viewedSignIds.collectAsState()

    // Check if this sign has been viewed - use local state
    val isViewed = viewedSignIds.contains(wordId)


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = word?.name ?: "Cargando...",
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            color = MainColor
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    // 🔹 Invisible icon to balance the layout (keeps title perfectly centered)
                    IconButton(onClick = {}, enabled = false) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.Transparent
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ThirdColor
                )
            )
        }
    ) { paddingValues ->
        if (word == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = ThirdColor)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF8F6EF))
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título principal
                /*
                Text(
                    text = word.name,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = ThirdColor,
                    textAlign = TextAlign.Center
                )*/

                // Card para imagen o video
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 250.dp, max = 400.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            word.imageUrl != null -> {
                                // Mostrar imagen
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(word.imageUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = word.name,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            word.videoUrl != null -> {
                                val context = LocalContext.current
                                val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

                                // --- Control States ---
                                var slowMode by remember { mutableStateOf(false) }
                                var isPlaying by remember { mutableStateOf(false) }
                                var loopEnabled by remember { mutableStateOf(true) }
                                var hasError by remember { mutableStateOf(false) }

                                // --- Optimized Video Player ---
                                val exoPlayer = remember(word.videoUrl) {
                                    ExoPlayer.Builder(context)
                                        .setLoadControl(
                                            androidx.media3.exoplayer.DefaultLoadControl.Builder()
                                                .setBufferDurationsMs(
                                                    500,  // Min buffer before playback starts (500ms)
                                                    2000, // Max buffer (2s) - reduced for shorter videos
                                                    500,  // Buffer for playback (500ms)
                                                    500   // Buffer for playback after rebuffer (500ms)
                                                )
                                                .build()
                                        )
                                        .build()
                                        .apply {
                                            try {
                                                val mediaItem = MediaItem.Builder()
                                                    .setUri(Uri.parse(word.videoUrl))
                                                    .build()

                                                setMediaItem(mediaItem)
                                                prepare()
                                                playWhenReady = false // Don't autoplay
                                                repeatMode = if (loopEnabled) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF

                                                // Add listener for errors
                                                addListener(object : Player.Listener {
                                                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                                                        hasError = true
                                                        android.util.Log.e("WordDetailScreen", "Video error: ${error.message}")
                                                    }
                                                })
                                            } catch (e: Exception) {
                                                hasError = true
                                                android.util.Log.e("WordDetailScreen", "Failed to load video", e)
                                            }
                                        }
                                }

                                // Lifecycle awareness - pause when app goes to background
                                DisposableEffect(lifecycleOwner) {
                                    val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
                                        when (event) {
                                            androidx.lifecycle.Lifecycle.Event.ON_PAUSE -> {
                                                exoPlayer.pause()
                                            }
                                            androidx.lifecycle.Lifecycle.Event.ON_STOP -> {
                                                exoPlayer.pause()
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
                                        exoPlayer.stop()
                                        exoPlayer.release()
                                    }
                                }

                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    // Error message if video failed to load
                                    if (hasError) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(280.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Text(
                                                    text = "⚠️",
                                                    fontSize = 48.sp
                                                )
                                                Text(
                                                    text = "Error al cargar el video",
                                                    color = Color.Red,
                                                    fontSize = 14.sp
                                                )
                                            }
                                        }
                                    } else {
                                        // Video Player View
                                        AndroidView(
                                            factory = { ctx ->
                                                PlayerView(ctx).apply {
                                                    player = exoPlayer
                                                    useController = true
                                                    // Optimize video surface for better performance
                                                    setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                                                }
                                            },
                                            update = { playerView ->
                                                playerView.player = exoPlayer
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .aspectRatio(16f / 9f) // Responsive height based on width
                                        )
                                    }

                                    if (!hasError) {
                                        Spacer(modifier = Modifier.height(2.dp))

                                        // --- Control Row ---
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceEvenly,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            // SLOW DOWN BUTTON --------------------------------------
                                            IconButton(
                                                onClick = {
                                                    slowMode = !slowMode
                                                    exoPlayer.setPlaybackSpeed(if (slowMode) 0.5f else 1.0f)
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.SlowMotionVideo,
                                                    contentDescription = "Slow Motion",
                                                    tint = if (slowMode) MainColor else Color.Gray
                                                )
                                            }

                                            // LOOP ---------------------------------------------------
                                            IconButton(
                                                onClick = {
                                                    loopEnabled = !loopEnabled
                                                    exoPlayer.repeatMode =
                                                        if (loopEnabled) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Refresh,
                                                    contentDescription = "Loop",
                                                    tint = if (loopEnabled) MainColor else Color.Gray
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            else -> {
                                // Placeholder cuando no hay video ni imagen
                                Text(
                                    text = word.name,
                                    fontSize = 48.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                // Descripción si está disponible
                word.description?.let { description ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Descripción",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = ThirdColor
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = description,
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                // Información adicional
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {/*
                        Text(
                            text = "Información",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = ThirdColor
                        )*/

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Tipo de contenido:",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = when {
                                    word.videoUrl != null -> "Video"
                                    word.imageUrl != null -> "Imagen"
                                    else -> "Texto"
                                },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ThirdColor
                            )
                        }

                    }
                }
                Button(
                    onClick = {
                        // Mark as viewed locally
                        if (!isViewed) {
                            viewedSignsViewModel.markSignAsViewed(wordId)
                        }
                        // Close the screen immediately
                        onClose()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isViewed) Color(0xFF4CAF50) else Special3Color,
                        contentColor = MainColor
                    )

                ) {
                    Text(
                        text = if (isViewed) "Ya visto ✓" else "Listo",
                        fontSize = 27.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

