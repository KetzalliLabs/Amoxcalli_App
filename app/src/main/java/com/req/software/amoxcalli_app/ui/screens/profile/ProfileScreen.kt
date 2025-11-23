package com.req.software.amoxcalli_app.ui.screens.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.max

data class ProfileUiState(
    val name: String = "Ernesto De Luna Quintero",
    val username: String = "@TheNetoLaNet4",
    val quizzes: String = "45",
    val plays: String = "5.6M",
    val rank: String = "16.8M",
    val coins: String = "301",
    val weeklyPointsTotal: Int = 875,
    val weeklyPoints: List<Int> = listOf(980, 420, 360, 640, 700, 880, 830),
    val medals: List<MedalUi> = listOf(
        MedalUi("tlalli", "Tlalli", "3", color = Color(0xFFFFF3C4)),
        MedalUi("obsidiana", "Obsidiana", "1", color = Color(0xFFE8E6FF)),
        MedalUi("racha", "Racha más larga", "32", isFire = true, color = Color(0xFFFFEFEF)),
        MedalUi("jade", "Jade", "2", color = Color(0xFFE7FFF1))
    )
)

data class MedalUi(
    val id: String,
    val title: String,
    val value: String,
    val isFire: Boolean = false,
    val color: Color
)

@Composable
fun ProfileScreen(
    uiState: ProfileUiState = ProfileUiState(),
    onBack: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onBellClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val bgBlue = Color(0xFF1F56E5)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgBlue)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {

        TopBar(
            coins = uiState.coins,
            onBack = onBack,
            onBellClick = onBellClick
        )

        Spacer(Modifier.height(8.dp))

        ProfileHeader(
            name = uiState.name,
            username = uiState.username,
            onEditProfile = onEditProfile
        )

        Spacer(Modifier.height(8.dp))

        StatsRow(
            quizzes = uiState.quizzes,
            plays = uiState.plays,
            rank = uiState.rank
        )

        Spacer(Modifier.height(14.dp))

        SectionTitle("Mis estadísticas")

        WeeklyStatisticsCard(
            total = uiState.weeklyPointsTotal,
            points = uiState.weeklyPoints
        )

        Spacer(Modifier.height(16.dp))

        SectionTitle("Medallas y logros:")

        MedalsGrid(uiState.medals)

        Spacer(Modifier.height(10.dp))

        GoldenCodexCard(
            value = "1",
            title = "CÓDICE DE ORO"
        )

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun TopBar(
    coins: String,
    onBack: () -> Unit,
    onBellClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, null, tint = Color.White)
        }

        Text(
            "Perfil",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )

        CoinsPill(coins)

        Spacer(Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF8B6A2B))
        )

        Spacer(Modifier.width(8.dp))

        IconButton(onClick = onBellClick) {
            Icon(Icons.Default.NotificationsNone, null, tint = Color.White)
        }
    }
}

@Composable
private fun CoinsPill(value: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Color.White)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(value, fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFC94A))
        )
    }
}

@Composable
private fun ProfileHeader(
    name: String,
    username: String,
    onEditProfile: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, null, tint = Color.White)
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(name, color = Color.White, fontWeight = FontWeight.Bold)
            Text(username, color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.bodySmall)
        }

        Button(
            onClick = onEditProfile,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(999.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Icon(Icons.Default.Edit, null, tint = Color(0xFF1F56E5), modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Editar perfil", color = Color(0xFF1F56E5))
        }
    }

    Divider(
        color = Color.White.copy(alpha = 0.35f),
        modifier = Modifier.padding(top = 10.dp)
    )
}

@Composable
private fun StatsRow(quizzes: String, plays: String, rank: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatItem(quizzes, "Cuestionarios", Modifier.weight(1f))
        VerticalDivider()
        StatItem(plays, "Reproducciones", Modifier.weight(1f))
        VerticalDivider()
        StatItem(rank, "Rango", Modifier.weight(1f))
    }

    Divider(color = Color.White.copy(alpha = 0.35f))
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(value, color = Color.White, fontWeight = FontWeight.Bold)
        Text(label, color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(34.dp)
            .background(Color.White.copy(alpha = 0.35f))
    )
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text,
        color = Color.White,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(vertical = 6.dp)
    )
}

@Composable
private fun WeeklyStatisticsCard(total: Int, points: List<Int>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Tus puntos esta semana",
                    color = Color(0xFF1F56E5),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text("$total pts", color = Color(0xFF1F56E5), fontWeight = FontWeight.Bold)
            }

            Divider(
                color = Color(0xFF1F56E5).copy(alpha = 0.25f),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            WeeklyAreaChart(
                points = points,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Lun","Mar","Mié","Jue","Vie","Sáb","Dom").forEach {
                    Text(it, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
private fun WeeklyAreaChart(points: List<Int>, modifier: Modifier = Modifier) {
    val maxVal = max(points.maxOrNull() ?: 1, 1)

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stepX = w / (points.size - 1).coerceAtLeast(1)

        fun yFor(v: Int): Float {
            val ratio = v.toFloat() / maxVal.toFloat()
            return h * (1f - ratio)
        }

        val linePath = Path()
        points.forEachIndexed { i, v ->
            val x = i * stepX
            val y = yFor(v)
            if (i == 0) linePath.moveTo(x, y) else linePath.lineTo(x, y)
        }

        val fillPath = Path().apply {
            addPath(linePath)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }

        drawPath(fillPath, color = Color(0xFF2F6BFF))
        drawPath(linePath, color = Color(0xFF2F6BFF), style = Stroke(width = 6f, cap = StrokeCap.Round))
    }
}

@Composable
private fun MedalsGrid(items: List<MedalUi>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 260.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        userScrollEnabled = false
    ) {
        items(items, key = { it.id }) { medal -> MedalCard(medal) }
    }
}

@Composable
private fun MedalCard(medal: MedalUi) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(86.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (medal.isFire) {
                Icon(
                    Icons.Default.Whatshot,
                    null,
                    tint = Color(0xFFFF6A00),
                    modifier = Modifier.size(34.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(medal.color)
                        .border(1.dp, Color.LightGray, CircleShape)
                )
            }

            Spacer(Modifier.width(10.dp))

            Column(Modifier.weight(1f)) {
                Text(medal.value, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                Text(medal.title, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun GoldenCodexCard(value: String, title: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .border(2.dp, Color(0xFFD4AF37), RoundedCornerShape(12.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .border(2.dp, Color(0xFFD4AF37), RoundedCornerShape(6.dp))
            )

            Spacer(Modifier.width(10.dp))

            Text(
                value,
                color = Color(0xFFD4AF37),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.width(10.dp))

            Text(
                title,
                color = Color(0xFFD4AF37),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}
