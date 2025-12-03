package com.req.software.amoxcalli_app.ui.screens.teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.req.software.amoxcalli_app.data.dto.UserStatsResponse
import com.req.software.amoxcalli_app.domain.model.UserRole
import com.req.software.amoxcalli_app.ui.components.admin.*
import com.req.software.amoxcalli_app.ui.components.headers.StatsHeader
import com.req.software.amoxcalli_app.ui.theme.Special3Color
import com.req.software.amoxcalli_app.ui.theme.ThirdColor
import com.req.software.amoxcalli_app.viewmodel.UserStatsViewModel

/**
 * Teacher Dashboard Screen
 * Focus on student performance, class management, and educational tools
 */
@Composable
fun TeacherDashboardScreen(
    userStats: UserStatsResponse?,
    userRole: UserRole,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    userStatsViewModel: UserStatsViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("📚 Mis Clases", "👥 Estudiantes", "📊 Progreso", "📝 Contenido")

    val localXP by userStatsViewModel.localXP.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        // Top header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Special3Color)
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
                        text = "🎓 Panel de Maestro",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Gestión de estudiantes y contenido",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.9f)
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
            contentColor = Special3Color,
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

        // Content
        when (selectedTab) {
            0 -> MyClassesTab()
            1 -> StudentsTab()
            2 -> ProgressTab()
            3 -> ContentTab()
        }
    }
}

/**
 * My Classes Tab - Overview of classes
 */
@Composable
private fun MyClassesTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6EF))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        SectionHeader(
            title = "Resumen de Clases",
            icon = "📚",
            subtitle = "Vista general de tus grupos"
        )

        // Class stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                icon = Icons.Default.Group,
                iconTint = Color(0xFF2196F3),
                value = "3",
                label = "Grupos Activos",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                icon = Icons.Default.Person,
                iconTint = Color(0xFF4CAF50),
                value = "67",
                label = "Estudiantes",
                subtitle = "total",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                icon = Icons.Default.TrendingUp,
                iconTint = Color(0xFF9C27B0),
                value = "78%",
                label = "Progreso Promedio",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                icon = Icons.Default.EmojiEvents,
                iconTint = Color(0xFFFFC107),
                value = "156",
                label = "Medallas",
                subtitle = "otorgadas",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionHeader(
            title = "Tus Grupos",
            subtitle = "Clases activas"
        )

        // Sample classes
        ClassCard(
            className = "LSM Básico - Grupo A",
            studentCount = 25,
            progress = 65,
            nextLesson = "Saludos y Despedidas"
        )

        Spacer(modifier = Modifier.height(12.dp))

        ClassCard(
            className = "LSM Intermedio - Grupo B",
            studentCount = 22,
            progress = 45,
            nextLesson = "Números y Tiempo"
        )

        Spacer(modifier = Modifier.height(12.dp))

        ClassCard(
            className = "LSM Avanzado - Grupo C",
            studentCount = 20,
            progress = 82,
            nextLesson = "Conversaciones Complejas"
        )

        Spacer(modifier = Modifier.height(80.dp))
    }
}

/**
 * Students Tab - Student list and management
 */
@Composable
private fun StudentsTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6EF))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        SectionHeader(
            title = "Mis Estudiantes",
            icon = "👥",
            subtitle = "67 estudiantes activos"
        )

        // Search and filter
        OutlinedTextField(
            value = "",
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar estudiante...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Top performers
        InfoCard(
            title = "Destacados de la Semana",
            content = "María García - 500 XP • Juan Pérez - 450 XP • Ana López - 420 XP",
            icon = "⭐",
            gradientColors = listOf(Color(0xFFFFD700), Color(0xFFFFA500)),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        SectionHeader(
            title = "Lista de Estudiantes",
            subtitle = "Ordenados por progreso"
        )

        // Sample students
        StudentCard(
            name = "María García",
            group = "Grupo A",
            progress = 85,
            xp = 1250,
            streak = 12,
            status = "Excelente"
        )

        Spacer(modifier = Modifier.height(8.dp))

        StudentCard(
            name = "Juan Pérez",
            group = "Grupo B",
            progress = 72,
            xp = 980,
            streak = 7,
            status = "Buen progreso"
        )

        Spacer(modifier = Modifier.height(8.dp))

        StudentCard(
            name = "Ana López",
            group = "Grupo A",
            progress = 68,
            xp = 875,
            streak = 5,
            status = "En progreso"
        )

        Spacer(modifier = Modifier.height(80.dp))
    }
}

/**
 * Progress Tab - Detailed progress tracking
 */
@Composable
private fun ProgressTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6EF))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        SectionHeader(
            title = "Análisis de Progreso",
            icon = "📊",
            subtitle = "Métricas detalladas de aprendizaje"
        )

        // Progress metrics
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Rendimiento General",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                DataListItem(
                    label = "Tasa de Finalización",
                    value = "78%",
                    icon = Icons.Default.CheckCircle
                )
                Divider()
                DataListItem(
                    label = "Precisión Promedio",
                    value = "82%",
                    icon = Icons.Default.Star
                )
                Divider()
                DataListItem(
                    label = "Tiempo Promedio/Ejercicio",
                    value = "3.5 min",
                    icon = Icons.Default.Timer
                )
                Divider()
                DataListItem(
                    label = "Ejercicios Completados",
                    value = "2,547",
                    icon = Icons.Default.Assignment
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        InfoCard(
            title = "Próximamente: Gráficas",
            content = "Visualiza el progreso de tus estudiantes con gráficas interactivas y reportes detallados.",
            icon = "📈",
            gradientColors = listOf(Color(0xFF667EEA), Color(0xFF764BA2)),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(80.dp))
    }
}

/**
 * Content Tab - Content management
 */
@Composable
private fun ContentTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6EF))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        SectionHeader(
            title = "Gestión de Contenido",
            icon = "📝",
            subtitle = "Crea y organiza material educativo"
        )

        InfoCard(
            title = "Herramientas de Contenido",
            content = "Próximamente podrás crear lecciones personalizadas, ejercicios y evaluaciones.",
            icon = "🚧",
            gradientColors = listOf(Color(0xFF4CAF50), Color(0xFF388E3C)),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        ActionButton(
            text = "Crear Nueva Lección",
            icon = Icons.Default.Add,
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Special3Color,
            enabled = false
        )

        Spacer(modifier = Modifier.height(8.dp))

        ActionButton(
            text = "Gestionar Ejercicios",
            icon = Icons.Default.Assignment,
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )

        Spacer(modifier = Modifier.height(8.dp))

        ActionButton(
            text = "Crear Evaluación",
            icon = Icons.Default.Assessment,
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )

        Spacer(modifier = Modifier.height(80.dp))
    }
}

/**
 * Class Card Component
 */
@Composable
private fun ClassCard(
    className: String,
    studentCount: Int,
    progress: Int,
    nextLesson: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = className,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Special3Color,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$studentCount estudiantes",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$progress% progreso",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Próxima lección: $nextLesson",
                fontSize = 11.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Light
            )
        }
    }
}

/**
 * Student Card Component
 */
@Composable
private fun StudentCard(
    name: String,
    group: String,
    progress: Int,
    xp: Int,
    streak: Int,
    status: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar placeholder
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Special3Color),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.first().toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = group,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "⭐ $xp XP",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "🔥 $streak días",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$progress%",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        progress >= 80 -> Color(0xFF4CAF50)
                        progress >= 60 -> Color(0xFFFFC107)
                        else -> Color(0xFFFF9800)
                    }
                )
                Text(
                    text = status,
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
