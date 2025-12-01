package com.req.software.amoxcalli_app.ui.screens.profile

import android.app.Activity
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.req.software.amoxcalli_app.viewmodel.AuthViewModel
import com.req.software.amoxcalli_app.viewmodel.UserStatsViewModel
import com.req.software.amoxcalli_app.ui.theme.MainColor
import com.req.software.amoxcalli_app.ui.theme.Special3Color
import com.req.software.amoxcalli_app.ui.theme.ThirdColor
import kotlin.math.roundToInt

/**
 * Pantalla de perfil del usuario
 * Muestra información del usuario y permite cerrar sesión
 */
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel = viewModel(),
    userStatsViewModel: UserStatsViewModel = viewModel(),
    onLogoutSuccess: () -> Unit = {},
    onNavigateToAdmin: () -> Unit = {}
) {
    val context = LocalContext.current
    val currentUser by authViewModel.currentUser.collectAsState()
    val userStats by userStatsViewModel.userStats.collectAsState()
    val isLoading by userStatsViewModel.isLoading.collectAsState()
    val isAdmin by authViewModel.isAdmin.collectAsState()
    val userRole by authViewModel.userRole.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Load stats when screen appears
    LaunchedEffect(Unit) {
        authViewModel.loadUserStats(userStatsViewModel)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Top header (same style as other screens)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(ThirdColor)
                .padding(top = 12.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Extract values from UserStatsResponse
            val coins = userStats?.stats?.find { it.name == "coins" }?.currentValue ?: 0
            val energy = userStats?.stats?.find { it.name == "energy" }?.currentValue ?: 0
            val streak = userStats?.streak?.currentDays ?: 0
            val experience = userStats?.stats?.find { it.name == "experience_points" }?.currentValue ?: 0

            com.req.software.amoxcalli_app.ui.components.headers.StatsHeader(
                coins = coins,
                energy = energy,
                streak = streak,
                experience = experience,
                medalsCount = userStats?.medals?.size ?: 0
            )
        }

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MainColor)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Avatar del usuario
            Card(
                modifier = Modifier
                    .size(140.dp)
                    .shadow(8.dp, CircleShape),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (currentUser?.avatarUrl != null) {
                        AsyncImage(
                            model = currentUser?.avatarUrl,
                            contentDescription = "Avatar del usuario",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = currentUser?.displayName?.firstOrNull()?.toString()?.uppercase() ?: "U",
                            fontSize = 56.sp,
                            fontWeight = FontWeight.Bold,
                            color = Special3Color
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Nombre del usuario
            Text(
                text = currentUser?.displayName ?: "Usuario",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Email del usuario
            Text(
                text = currentUser?.email ?: "",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Stats Overview Cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Streak Card
                StatsCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Favorite,
                    iconTint = Color(0xFFFF6B6B),
                    label = "Racha Actual",
                    value = userStats?.streak?.currentDays?.toString() ?: "0",
                    subtitle = "días"
                )

                // Exercises Completed
                StatsCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.CheckCircle,
                    iconTint = Color(0xFF4CAF50),
                    label = "Ejercicios",
                    value = userStatsViewModel.getExercisesCompleted().toString(),
                    subtitle = "completados"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Accuracy
                StatsCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Star,
                    iconTint = Color(0xFFFFC107),
                    label = "Precisión",
                    value = "${userStatsViewModel.getAccuracyPercentage().roundToInt()}%",
                    subtitle = "${userStatsViewModel.getCorrectAttempts()}/${userStatsViewModel.getTotalAttempts()}"
                )

                // Medals
                StatsCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.EmojiEvents,
                    iconTint = Color(0xFFFF9800),
                    label = "Medallas",
                    value = userStatsViewModel.getMedalsCount().toString(),
                    subtitle = "logradas"
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Progress Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(3.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Progreso por Categorías",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ProgressItem(
                            label = "En Progreso",
                            value = userStatsViewModel.getCategoriesInProgress().toString(),
                            color = Special3Color
                        )
                        ProgressItem(
                            label = "Completadas",
                            value = userStatsViewModel.getCategoriesCompleted().toString(),
                            color = Color(0xFF4CAF50)
                        )
                        ProgressItem(
                            label = "Mejor Racha",
                            value = userStats?.streak?.bestDays?.toString() ?: "0",
                            color = Color(0xFFFF9800)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Medals Section
            if (!userStats?.medals.isNullOrEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .shadow(3.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Medallas Recientes",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(userStats?.medals?.take(5) ?: emptyList()) { medal ->
                                MedalItem(
                                    name = medal.name,
                                    iconUrl = medal.iconUrl
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Achievements Summary
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(3.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Resumen de Actividad",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ActivityRow(
                        icon = Icons.Default.Assignment,
                        label = "Intentos Totales",
                        value = userStats?.attempts?.total?.toString() ?: "0"
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ActivityRow(
                        icon = Icons.Default.CheckCircle,
                        label = "Respuestas Correctas",
                        value = userStats?.attempts?.correct?.toString() ?: "0"
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ActivityRow(
                        icon = Icons.Default.Visibility,
                        label = "Señas Vistas",
                        value = userStats?.signViews?.size?.toString() ?: "0"
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ActivityRow(
                        icon = Icons.Default.LocalLibrary,
                        label = "Ejercicios Históricos",
                        value = userStats?.exerciseHistory?.size?.toString() ?: "0"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de Panel de Administración (solo para admin/superadmin)
            if (isAdmin) {
                Button(
                    onClick = onNavigateToAdmin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6B5B95),
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Text(
                        text = "🛡️",
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Panel de Administración",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = userRole.displayName,
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Botón de Cerrar Sesión
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEF5350),
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Cerrar sesión",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Cerrar Sesión",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Información adicional
            Text(
                text = "Miembro desde: ${currentUser?.joinDate ?: "---"}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Dialog de confirmación de logout
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    text = "Cerrar Sesión",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("¿Estás seguro de que quieres cerrar sesión?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        val webClientId = "973466374407-rvc7bk7ifbg2im0256b9307lojsubd7d.apps.googleusercontent.com"
                        val gso = com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder(
                            com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN
                        )
                            .requestIdToken(webClientId)
                            .requestEmail()
                            .build()
                        val googleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(
                            context as Activity,
                            gso
                        )
                        authViewModel.signOut(googleSignInClient)
                        onLogoutSuccess()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEF5350)
                    )
                ) {
                    Text("Cerrar Sesión")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun StatsCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconTint: Color,
    label: String,
    value: String,
    subtitle: String
) {
    Card(
        modifier = modifier
            .shadow(3.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ProgressItem(
    label: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun MedalItem(
    name: String,
    iconUrl: String?
) {
    Card(
        modifier = Modifier
            .size(80.dp)
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF8E1)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (iconUrl != null) {
                AsyncImage(
                    model = iconUrl,
                    contentDescription = name,
                    modifier = Modifier.size(40.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = name,
                    tint = Color(0xFFFFB300),
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = name,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ActivityRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Special3Color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun AnimatedProfileHeader(
    displayName: String,
    experiencePoints: Int,
    coins: Int,
    streak: Int,
    medalsCount: Int
) {
    // Gradient colors for header
    val gradientColors = listOf(
        Color(0xFF6A1B9A),
        Color(0xFF8E24AA),
        Color(0xFFAB47BC),
        Color(0xFFCE93D8)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = gradientColors
                    )
                )
                .padding(top = 20.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Title with animation
            Text(
                text = "Mi Perfil",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Stats Row with animations
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // XP Stat
                AnimatedProfileStat(
                    emoji = "⭐",
                    value = experiencePoints.toString(),
                    label = "XP",
                    color = Color(0xFFFFF8E1)
                )

                // Coins Stat
                AnimatedProfileStat(
                    emoji = "🪙",
                    value = coins.toString(),
                    label = "Cacao",
                    color = Color(0xFFFFF3E0)
                )

                // Streak Stat
                AnimatedProfileStat(
                    emoji = "🔥",
                    value = streak.toString(),
                    label = "Racha",
                    color = Color(0xFFFFE5D9)
                )

                // Medals Stat
                AnimatedProfileStat(
                    emoji = "🏆",
                    value = medalsCount.toString(),
                    label = "Medallas",
                    color = Color(0xFFFFECB3)
                )
            }
        }
    }
}

@Composable
private fun AnimatedProfileStat(
    emoji: String,
    value: String,
    label: String,
    color: Color
) {
    // Bounce animation
    val infiniteTransition = rememberInfiniteTransition(label = "stat_$label")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    // Rotation for emoji
    val rotation by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = color,
        modifier = Modifier
            .width(80.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animated emoji
            Text(
                text = emoji,
                fontSize = 24.sp,
                modifier = Modifier
                    .scale(scale)
                    .rotate(rotation)
            )
            Spacer(modifier = Modifier.height(6.dp))
            // Value
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            // Label
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF666666)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen()
    }
}
