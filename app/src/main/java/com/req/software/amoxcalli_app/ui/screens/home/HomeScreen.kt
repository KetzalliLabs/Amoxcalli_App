package com.req.software.amoxcalli_app.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.req.software.amoxcalli_app.domain.model.Topic
import com.req.software.amoxcalli_app.domain.model.UserStats
import com.req.software.amoxcalli_app.ui.components.buttons.PrimaryButton
import com.req.software.amoxcalli_app.ui.components.cards.TopicCard
import com.req.software.amoxcalli_app.ui.components.headers.StatsHeader

/**
 * Pantalla principal (Home) de la aplicación
 * Muestra las estadísticas del usuario, temas recientes y recomendados
 */
@Composable
fun HomeScreen(
    userStats: UserStats,
    recentTopics: List<Topic>,
    recommendedTopics: List<Topic>,
    onTopicClick: (Topic) -> Unit,
    onQuizClick: () -> Unit,
    onPracticeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            Column (
                modifier = Modifier
                    .background(
                        Color(0xFF2196F3)
//                        brush = Brush.horizontalGradient(
//                            colors = listOf(
//                                Color(0xFF2196F3),  // Azul arriba
//                                Color(0xFFBBDEFB),  // Azul claro
//                            )
//                        )
                    ) // Este es el fondo azul que aparece en el topbar
                    .padding(top= 15.dp, bottom = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {

                StatsHeader(
                    coins = userStats.coins,
                    energy = userStats.energy,
                    streak = userStats.streak,
                    experience = userStats.experience
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
//                    Color.White
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2196F3),  // Azul arriba
                            Color(0xFFBBDEFB),  // Azul claro
                        )
                   )
                )
                .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {

            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Sección de Temas Recientes
            SectionTitle(text = "Temas Recientes:")
            
            Spacer(modifier = Modifier.height(12.dp))
            
            recentTopics.forEach { topic ->
                TopicCard(
                    title = topic.name,
                    progress = topic.progress,
                    color = getTopicColor(topic.id),
                    onClick = { onTopicClick(topic) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Sección de Temas Recomendados
            SectionTitle(text = "Temas Recomendados:")
            
            Spacer(modifier = Modifier.height(12.dp))
            
            recommendedTopics.forEach { topic ->
                TopicCard(
                    title = topic.name,
                    progress = topic.progress,
                    color = getTopicColor(topic.id),
                    onClick = { onTopicClick(topic) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Botones de acción
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "¡Quiz diario!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                PrimaryButton(
                    text = "Comenzar",
                    onClick = onQuizClick,
                    backgroundColor = Color(0xFF4CAF50),
                    enablePulse = true
                )

                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Reforzar palabras",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                PrimaryButton(
                    text = "Comenzar",
                    onClick = onPracticeClick,
                    backgroundColor = Color(0xFF4CAF50),
                    enablePulse = true
                )
            }
            
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

/**
 * Función helper para asignar colores a los temas
 */
private fun getTopicColor(topicId: String): Color {
    return when (topicId) {
        "abecedario" -> Color(0xFF5C6BC0) // Azul
        "animales" -> Color(0xFF4CAF50)   // Verde
        "vehiculos" -> Color(0xFFFDD835)  // Amarillo
        "verbos" -> Color(0xFFEF5350)     // Rojo
        "preguntas" -> Color(0xFF9C27B0)  // Morado
        else -> Color(0xFF757575)         // Gris por defecto
    }
}

// ============================================
// PREVIEW - Para ver la pantalla sin emulador
// ============================================

/**
 * Preview de la pantalla Home con datos de ejemplo
 * Usa esto para ver cambios en tiempo real en Android Studio
 */
@Preview(
    name = "Home Screen - Light",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun HomeScreenPreview() {
    // Datos de ejemplo
    val sampleUserStats = UserStats(
        coins = 300,
        energy = 20,
        streak = 9,
        experience = 2500
    )
    
    val sampleRecentTopics = listOf(
        Topic(id = "abecedario", name = "Abecedario", progress = 100, isRecent = true),
        Topic(id = "animales", name = "Animales", progress = 20, isRecent = true),
        Topic(id = "vehiculos", name = "Vehículos", progress = 5, isRecent = true)
    )
    
    val sampleRecommendedTopics = listOf(
        Topic(id = "verbos", name = "Verbos simples", progress = 0),
        Topic(id = "preguntas", name = "Preguntas básicas", progress = 0)
    )
    
    HomeScreen(
        userStats = sampleUserStats,
        recentTopics = sampleRecentTopics,
        recommendedTopics = sampleRecommendedTopics,
        onTopicClick = {},
        onQuizClick = {},
        onPracticeClick = {}
    )
}

/**
 * Preview con diferentes estados de progreso
 */
@Preview(
    name = "Home Screen - Progreso Variado",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun HomeScreenProgressVariedPreview() {
    val sampleUserStats = UserStats(
        coins = 150,
        energy = 15,
        streak = 3,
        experience = 850
    )
    
    val sampleRecentTopics = listOf(
        Topic(id = "abecedario", name = "Abecedario", progress = 50, isRecent = true),
        Topic(id = "animales", name = "Animales", progress = 75, isRecent = true)
    )
    
    val sampleRecommendedTopics = listOf(
        Topic(id = "verbos", name = "Verbos simples", progress = 10),
        Topic(id = "preguntas", name = "Preguntas básicas", progress = 5),
        Topic(id = "vehiculos", name = "Vehículos", progress = 0)
    )
    
    HomeScreen(
        userStats = sampleUserStats,
        recentTopics = sampleRecentTopics,
        recommendedTopics = sampleRecommendedTopics,
        onTopicClick = {},
        onQuizClick = {},
        onPracticeClick = {}
    )
}

/**
 * Preview para usuario nuevo (sin progreso)
 */
@Preview(
    name = "Home Screen - Usuario Nuevo",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun HomeScreenNewUserPreview() {
    val sampleUserStats = UserStats(
        coins = 0,
        energy = 20,
        streak = 0,
        experience = 0
    )
    
    val sampleRecentTopics = emptyList<Topic>()
    
    val sampleRecommendedTopics = listOf(
        Topic(id = "abecedario", name = "Abecedario", progress = 0),
        Topic(id = "animales", name = "Animales", progress = 0),
        Topic(id = "verbos", name = "Verbos simples", progress = 0),
        Topic(id = "preguntas", name = "Preguntas básicas", progress = 0)
    )
    
    HomeScreen(
        userStats = sampleUserStats,
        recentTopics = sampleRecentTopics,
        recommendedTopics = sampleRecommendedTopics,
        onTopicClick = {},
        onQuizClick = {},
        onPracticeClick = {}
    )
}
