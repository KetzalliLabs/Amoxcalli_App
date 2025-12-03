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
import com.req.software.amoxcalli_app.ui.theme.ThirdColor
import com.req.software.amoxcalli_app.viewmodel.UserStatsViewModel

/**
 * Admin Panel Screen (Limited Features)
 * Access to statistics and basic management (no system modifications)
 */
@Composable
fun AdminPanelScreen(
    userStats: UserStatsResponse?,
    userRole: UserRole,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    userStatsViewModel: UserStatsViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("📊 Dashboard", "👥 Users", "📈 Reports")

    val localXP by userStatsViewModel.localXP.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        // Top header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(ThirdColor)
                .padding(top = 12.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                        text = "🛡️ Panel de Administrador",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Gestión y estadísticas",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

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

        // Tabs
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
                    text = { Text(title, fontSize = 14.sp) }
                )
            }
        }

        // Content
        when (selectedTab) {
            0 -> AdminDashboardTab(userStats, localXP)
            1 -> AdminUsersTab()
            2 -> AdminReportsTab()
        }
    }
}

@Composable
private fun AdminDashboardTab(userStats: UserStatsResponse?, localXP: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6EF))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        SectionHeader(
            title = "Vista General",
            icon = "📊",
            subtitle = "Estadísticas de la plataforma"
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                icon = Icons.Default.Person,
                iconTint = Color(0xFF2196F3),
                value = "156",
                label = "Usuarios Activos",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                icon = Icons.Default.TrendingUp,
                iconTint = Color(0xFF4CAF50),
                value = "+18%",
                label = "Crecimiento",
                subtitle = "este mes",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                icon = Icons.Default.CheckCircle,
                iconTint = Color(0xFF9C27B0),
                value = "2,547",
                label = "Ejercicios",
                subtitle = "completados",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                icon = Icons.Default.Star,
                iconTint = Color(0xFFFFC107),
                value = "85%",
                label = "Precisión",
                subtitle = "promedio",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        InfoCard(
            title = "Tus Estadísticas Personales",
            content = "$localXP XP • ${userStats?.medals?.size ?: 0} medallas • ${userStats?.streak?.currentDays ?: 0} días de racha",
            icon = "👤",
            gradientColors = listOf(Color(0xFF667EEA), Color(0xFF764BA2)),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
private fun AdminUsersTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6EF))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        SectionHeader(
            title = "Gestión de Usuarios",
            icon = "👥"
        )

        InfoCard(
            title = "Próximamente",
            content = "Visualización de usuarios y estadísticas básicas.",
            icon = "🚧",
            gradientColors = listOf(Color(0xFF2196F3), Color(0xFF1976D2)),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        ActionButton(
            text = "Ver Lista de Usuarios",
            icon = Icons.Default.List,
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )
    }
}

@Composable
private fun AdminReportsTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6EF))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        SectionHeader(
            title = "Reportes",
            icon = "📈",
            subtitle = "Análisis y estadísticas"
        )

        InfoCard(
            title = "Reportes Disponibles",
            content = "Genera reportes de actividad, progreso y rendimiento de usuarios.",
            icon = "📊",
            gradientColors = listOf(Color(0xFF4CAF50), Color(0xFF388E3C)),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        ActionButton(
            text = "Reporte de Actividad",
            icon = Icons.Default.Assessment,
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )

        Spacer(modifier = Modifier.height(8.dp))

        ActionButton(
            text = "Reporte de Progreso",
            icon = Icons.Default.TrendingUp,
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )
    }
}
