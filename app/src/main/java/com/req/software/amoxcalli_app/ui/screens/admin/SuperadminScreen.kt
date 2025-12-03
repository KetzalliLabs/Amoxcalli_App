package com.req.software.amoxcalli_app.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.req.software.amoxcalli_app.data.dto.UserStatsResponse
import com.req.software.amoxcalli_app.domain.model.UserRole
import com.req.software.amoxcalli_app.ui.components.admin.*
import com.req.software.amoxcalli_app.ui.components.headers.StatsHeader
import com.req.software.amoxcalli_app.ui.components.medals.MedalCelebrationDialog
import com.req.software.amoxcalli_app.ui.components.notifications.StatsNotification
import com.req.software.amoxcalli_app.ui.theme.Special3Color
import com.req.software.amoxcalli_app.ui.theme.ThirdColor
import com.req.software.amoxcalli_app.viewmodel.MedalViewModel
import com.req.software.amoxcalli_app.viewmodel.UserStatsViewModel

/**
 * Comprehensive Superadmin Panel
 * Full access to all system features, statistics, and tools
 */
@Composable
fun SuperadminScreen(
    userStats: UserStatsResponse?,
    userRole: UserRole,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    medalViewModel: MedalViewModel = viewModel(),
    userStatsViewModel: UserStatsViewModel = viewModel()
) {
    // State
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("📊 Dashboard", "🧪 Testing", "👥 Users", "⚙️ System")

    // Observe states
    val newlyEarnedMedal by medalViewModel.newlyEarnedMedal.collectAsState()
    val notification by userStatsViewModel.notification.collectAsState()
    val localXP by userStatsViewModel.localXP.collectAsState()
    val currentStreak by medalViewModel.currentStreak.collectAsState()
    val bestStreak by medalViewModel.bestStreak.collectAsState()

    // Medal celebration dialog
    newlyEarnedMedal?.let { medal ->
        MedalCelebrationDialog(
            medal = medal,
            onDismiss = { medalViewModel.dismissMedalCelebration() }
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top header with stats
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ThirdColor)
                    .padding(top = 12.dp, bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Back button and title
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "🛡️ Superadmin Panel",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Control total del sistema",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                // Stats header
                val coins = userStats?.stats?.find { it.name == "coins" }?.currentValue ?: 0
                val energy = userStats?.stats?.find { it.name == "energy" }?.currentValue ?: 0
                val streak = userStats?.streak?.currentDays ?: 0
                val experience = userStats?.stats?.find { it.name == "experience_points" }?.currentValue ?: 0

                StatsHeader(
                    coins = coins,
                    energy = energy,
                    streak = streak,
                    experience = experience,
                    medalsCount = userStats?.medals?.size ?: 0,
                    useLocalXP = true,
                    localXP = localXP
                )
            }

            // Tab navigation
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = ThirdColor,
                edgePadding = 0.dp
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 14.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // Tab content
            when (selectedTab) {
                0 -> DashboardTab(userStats, localXP, currentStreak)
                1 -> TestingTab(
                    userStatsViewModel = userStatsViewModel,
                    medalViewModel = medalViewModel,
                    localXP = localXP,
                    currentStreak = currentStreak,
                    bestStreak = bestStreak
                )
                2 -> UsersTab()
                3 -> SystemTab()
            }
        }

        // Notification overlay
        StatsNotification(
            notificationState = notification,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

/**
 * Dashboard Tab - Statistics and overview
 */
@Composable
private fun DashboardTab(
    userStats: UserStatsResponse?,
    localXP: Int,
    currentStreak: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6EF))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // System Statistics
        SectionHeader(
            title = "Estadísticas del Sistema",
            icon = "📊",
            subtitle = "Vista general de la plataforma"
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                icon = Icons.Default.Person,
                iconTint = Color(0xFF2196F3),
                value = "156",
                label = "Usuarios Totales",
                subtitle = "+12 esta semana",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                icon = Icons.Default.School,
                iconTint = Color(0xFF4CAF50),
                value = "8",
                label = "Maestros",
                subtitle = "activos",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                icon = Icons.Default.EmojiEvents,
                iconTint = Color(0xFFFFC107),
                value = "423",
                label = "Medallas Otorgadas",
                subtitle = "en total",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                icon = Icons.Default.CheckCircle,
                iconTint = Color(0xFF9C27B0),
                value = "2,547",
                label = "Ejercicios Completados",
                subtitle = "este mes",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // User Statistics
        SectionHeader(
            title = "Tus Estadísticas",
            icon = "👤",
            subtitle = "Tu progreso personal"
        )

        InfoCard(
            title = "XP Local",
            content = "$localXP puntos de experiencia acumulados",
            icon = "⭐",
            gradientColors = listOf(Color(0xFFFFD700), Color(0xFFFFA500)),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        InfoCard(
            title = "Racha Actual",
            content = "$currentStreak días consecutivos de práctica",
            icon = "🔥",
            gradientColors = listOf(Color(0xFFFF6B6B), Color(0xFFFF8E53)),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Quick Stats
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Datos Detallados",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                DataListItem(
                    label = "Medallas Obtenidas",
                    value = "${userStats?.medals?.size ?: 0}",
                    icon = Icons.Default.EmojiEvents
                )
                Divider()
                DataListItem(
                    label = "Ejercicios Completados",
                    value = "${userStats?.exerciseHistory?.size ?: 0}",
                    icon = Icons.Default.CheckCircle
                )
                Divider()
                DataListItem(
                    label = "Señas Vistas",
                    value = "${userStats?.signViews?.size ?: 0}",
                    icon = Icons.Default.Visibility
                )
                Divider()
                DataListItem(
                    label = "Precisión",
                    value = "${((userStats?.attempts?.accuracyPercentage ?: 0.0) * 100).toInt()}%",
                    icon = Icons.Default.Star
                )
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

/**
 * Testing Tab - XP, Streaks, and Medals
 */
@Composable
private fun TestingTab(
    userStatsViewModel: UserStatsViewModel,
    medalViewModel: MedalViewModel,
    localXP: Int,
    currentStreak: Int,
    bestStreak: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6EF))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        WarningBanner(
            message = "Estas herramientas solo deben usarse para pruebas. Los cambios afectan datos locales.",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // XP Testing
        SectionHeader(
            title = "Pruebas de XP",
            icon = "⭐",
            subtitle = "XP Local: $localXP"
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ActionButton(
                text = "+10 XP",
                emoji = "⭐",
                onClick = { userStatsViewModel.awardCorrectAnswerXP() },
                backgroundColor = Color(0xFFFFD700),
                contentColor = Color.Black,
                modifier = Modifier.weight(1f)
            )
            ActionButton(
                text = "+50 XP",
                emoji = "🎯",
                onClick = { userStatsViewModel.awardExerciseCompletionXP() },
                backgroundColor = Color(0xFFFFA500),
                contentColor = Color.Black,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ActionButton(
                text = "+100 XP",
                emoji = "💎",
                onClick = { userStatsViewModel.addXP(100) },
                backgroundColor = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )
            ActionButton(
                text = "Reset XP",
                icon = Icons.Default.Delete,
                onClick = { userStatsViewModel.resetAllXP() },
                backgroundColor = Color(0xFFD32F2F),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Streak Testing
        SectionHeader(
            title = "Pruebas de Racha",
            icon = "🔥",
            subtitle = "Racha Actual: $currentStreak días (Mejor: $bestStreak)"
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ActionButton(
                text = "Set 3 días",
                emoji = "🔥",
                onClick = { medalViewModel.setStreakForTesting(3) },
                backgroundColor = Color(0xFF2196F3),
                modifier = Modifier.weight(1f)
            )
            ActionButton(
                text = "Set 7 días",
                emoji = "🔥",
                onClick = { medalViewModel.setStreakForTesting(7) },
                backgroundColor = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ActionButton(
                text = "Reset Racha",
                icon = Icons.Default.Delete,
                onClick = { medalViewModel.resetStreak() },
                backgroundColor = Color(0xFFD32F2F),
                modifier = Modifier.weight(1f)
            )
            ActionButton(
                text = "Test Notificación",
                icon = Icons.Default.Notifications,
                onClick = {
                    userStatsViewModel.showNotification(
                        com.req.software.amoxcalli_app.ui.components.notifications.StatsNotifications.streakIncreased(
                            currentStreak
                        )
                    )
                },
                backgroundColor = Color(0xFFFF6B6B),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Medal Testing
        SectionHeader(
            title = "Pruebas de Medallas",
            icon = "🏅",
            subtitle = "Animaciones y celebraciones"
        )

        ActionButton(
            text = "Test All Medal Animations",
            emoji = "🏅",
            onClick = { medalViewModel.testAllMedalAnimations() },
            backgroundColor = Special3Color,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(80.dp))
    }
}

/**
 * Users Tab - User management
 */
@Composable
private fun UsersTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6EF))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        SectionHeader(
            title = "Gestión de Usuarios",
            icon = "👥",
            subtitle = "Administrar usuarios del sistema"
        )

        InfoCard(
            title = "Próximamente",
            content = "Panel de gestión de usuarios: Ver, editar, promover roles y más.",
            icon = "🚧",
            gradientColors = listOf(Color(0xFF667EEA), Color(0xFF764BA2)),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Quick actions
        ActionButton(
            text = "Ver Todos los Usuarios",
            icon = Icons.Default.Person,
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )

        Spacer(modifier = Modifier.height(8.dp))

        ActionButton(
            text = "Gestionar Maestros",
            icon = Icons.Default.School,
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )

        Spacer(modifier = Modifier.height(8.dp))

        ActionButton(
            text = "Asignar Roles",
            icon = Icons.Default.AdminPanelSettings,
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )
    }
}

/**
 * System Tab - System configuration and info
 */
@Composable
private fun SystemTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6EF))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        SectionHeader(
            title = "Información del Sistema",
            icon = "⚙️",
            subtitle = "Configuración y estado"
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                DataListItem(
                    label = "Versión de la App",
                    value = "1.0.0",
                    icon = Icons.Default.Info
                )
                Divider()
                DataListItem(
                    label = "API Backend",
                    value = "Conectado",
                    icon = Icons.Default.CloudDone
                )
                Divider()
                DataListItem(
                    label = "Storage Local",
                    value = "SharedPreferences",
                    icon = Icons.Default.Storage
                )
                Divider()
                DataListItem(
                    label = "Modo",
                    value = "Producción",
                    icon = Icons.Default.Settings
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        WarningBanner(
            message = "Los cambios en esta sección pueden afectar el funcionamiento del sistema.",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(80.dp))
    }
}
