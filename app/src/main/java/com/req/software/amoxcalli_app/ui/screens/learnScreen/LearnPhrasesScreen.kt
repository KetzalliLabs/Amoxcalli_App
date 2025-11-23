package com.req.software.amoxcalli_app.ui.screens.learnScreen

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.req.software.amoxcalli_app.ui.components.headers.StatsHeader
import com.req.software.amoxcalli_app.ui.theme.Special3Color

/**
 * Pantalla para aprender nuevas frases en Lengua de Señas Mexicana
 * Muestra frases comunes con videos demostrativos
 */
@Composable
fun LearnPhrasesScreen(
    onNavigateToExercises: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Datos de ejemplo de frases
    val phrases = remember {
        listOf(
            PhraseItem(
                id = "1",
                spanish = "Hola, ¿cómo estás?",
                category = "Saludos",
                difficulty = "Básico",
                videoUrl = "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Hola_Web.m4v",
                isCompleted = true,
                stars = 3
            ),
            PhraseItem(
                id = "2",
                spanish = "Buenos días",
                category = "Saludos",
                difficulty = "Básico",
                videoUrl = "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/BuenosDias_Web.m4v",
                isCompleted = true,
                stars = 2
            ),
            PhraseItem(
                id = "3",
                spanish = "Gracias",
                category = "Cortesía",
                difficulty = "Básico",
                videoUrl = "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Gracias_Web.m4v",
                isCompleted = false,
                stars = 0
            ),
            PhraseItem(
                id = "4",
                spanish = "¿Cómo te llamas?",
                category = "Preguntas",
                difficulty = "Intermedio",
                videoUrl = "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/ComeTeLlamas_Web.m4v",
                isCompleted = false,
                stars = 0
            ),
            PhraseItem(
                id = "5",
                spanish = "Me llamo...",
                category = "Presentación",
                difficulty = "Intermedio",
                videoUrl = "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/MeLlamo_Web.m4v",
                isCompleted = false,
                stars = 0
            ),
            PhraseItem(
                id = "6",
                spanish = "¿Dónde está...?",
                category = "Preguntas",
                difficulty = "Intermedio",
                videoUrl = "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/DondeEsta_Web.m4v",
                isCompleted = false,
                stars = 0
            ),
            PhraseItem(
                id = "7",
                spanish = "Por favor",
                category = "Cortesía",
                difficulty = "Básico",
                videoUrl = "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/PorFavor_Web.m4v",
                isCompleted = false,
                stars = 0
            ),
            PhraseItem(
                id = "8",
                spanish = "Mucho gusto",
                category = "Presentación",
                difficulty = "Básico",
                videoUrl = "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/MuchoGusto_Web.m4v",
                isCompleted = false,
                stars = 0
            )
        )
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(Color(0xFF2196F3))
                    .padding(top = 15.dp, bottom = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StatsHeader(
                    coins = 300,
                    energy = 20,
                    streak = 9,
                    experience = 2500
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2196F3),
                            Color(0xFFBBDEFB).copy(alpha = 0.7f),
                            Color.White
                        )
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                // Título de la sección
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.9f)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Aprende Frases Nuevas",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0D1A3A),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Practica estas frases comunes en Lengua de Señas Mexicana",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Botón para practicar con minijuegos
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF004225)
                    ),
                    onClick = onNavigateToExercises
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Practicar",
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Practica con Minijuegos",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Ejercicios interactivos de señas",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Lista de frases
            items(phrases) { phrase ->
                PhraseCard(
                    phrase = phrase,
                    onClick = { /* TODO: Navegar a práctica de frase */ }
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun PhraseCard(
    phrase: PhraseItem,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .scale(scale)
            .clickable {
                isPressed = true
                onClick()
            }
            .shadow(6.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (phrase.isCompleted) Color(0xFFE8F5E9) else Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Círculo de dificultad
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(
                        when (phrase.difficulty) {
                            "Básico" -> Color(0xFF4CAF50)
                            "Intermedio" -> Color(0xFFFFA726)
                            else -> Color(0xFFEF5350)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = phrase.difficulty.first().toString(),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Información de la frase
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = phrase.spanish,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = phrase.category,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Estrellas de progreso
                if (phrase.isCompleted) {
                    Row {
                        repeat(3) { index ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Estrella",
                                tint = if (index < phrase.stars) Color(0xFFFFC107) else Color.Gray.copy(alpha = 0.3f),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Botón de reproducción
            Card(
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2196F3)
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Reproducir",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        // Indicador de completado
        if (phrase.isCompleted) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF4CAF50),
                                Color(0xFF8BC34A)
                            )
                        )
                    )
            )
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}

/**
 * Modelo de datos para una frase
 */
data class PhraseItem(
    val id: String,
    val spanish: String,
    val category: String,
    val difficulty: String, // "Básico", "Intermedio", "Avanzado"
    val videoUrl: String,
    val isCompleted: Boolean = false,
    val stars: Int = 0 // 0-3 estrellas
)

@Preview(showBackground = true)
@Composable
fun LearnPhrasesScreenPreview() {
    MaterialTheme {
        LearnPhrasesScreen()
    }
}
