package com.req.software.amoxcalli_app.ui.screens.learnScreen

// ===== COMPOSE FOUNDATION - Layouts y Efectos Visuales =====
import androidx.compose.foundation.background // Modificador para aplicar colores o gradientes de fondo a componentes
import androidx.compose.foundation.layout.* // Todos los componentes de diseño: Column, Row, Box, Spacer, padding, fillMaxSize, etc.
import androidx.compose.foundation.lazy.LazyColumn

// ===== MATERIAL3 - Componentes de Material Design =====
import androidx.compose.material3.Scaffold // Estructura base de pantalla con soporte para TopBar, BottomBar, FAB y contenido

// ===== COMPOSE RUNTIME - Ciclo de Vida y Recomposición =====
import androidx.compose.runtime.Composable // Anotación que marca funciones como componentes de interfaz reutilizables
import androidx.compose.runtime.collectAsState

// ===== COMPOSE UI - Modificadores y Alineación =====
import androidx.compose.ui.Alignment // Opciones de alineación para elementos dentro de Box, Column y Row (Center, Top, Bottom, etc.)
import androidx.compose.ui.Modifier // Clase base para modificar apariencia y comportamiento de componentes (tamaño, padding, color, etc.)

// ===== COMPOSE UI GRAPHICS - Colores y Gradientes =====
import androidx.compose.ui.graphics.Brush // Crea gradientes lineales, radiales o con patrones personalizados para fondos
import androidx.compose.ui.graphics.Color // Define colores personalizados usando valores RGB, ARGB o hexadecimales

// ===== COMPOSE UI TEXT - Estilos de Texto =====

// ===== COMPOSE UI TOOLING - Herramientas de Desarrollo =====

// ===== COMPOSE UI UNITS - Unidades de Medida =====
import androidx.compose.ui.unit.dp // Density-independent pixels: unidad que se ajusta según la densidad de la pantalla
import androidx.lifecycle.viewmodel.compose.viewModel

// ===== DOMAIN MODELS - Modelos de Datos de la Aplicación =====

// ===== UI COMPONENTS - Componentes Personalizados Reutilizables =====
import com.req.software.amoxcalli_app.ui.components.cards.TopicCard // Card que muestra información de un tema con imagen, título y progreso
import com.req.software.amoxcalli_app.ui.components.headers.StatsHeader // Header que muestra las estadísticas del usuario en formato visual
import com.req.software.amoxcalli_app.viewmodel.LearnViewModel

import androidx.compose.foundation.lazy.items // ✅ AGREGAR ESTE IMPORT
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview // ✅ Agregar este import

/**
 * Pantalla Aprender
 * Se muestran los diferentes temas que se pueden aprender por categorias
 */
@Preview
@Composable
fun LearnScreen(
    viewModel: LearnViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            Column (
                modifier = Modifier
                    .background(Color(0xFF2196F3))
                    .padding(top= 15.dp, bottom = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StatsHeader(
                    coins = uiState.userStats.coins,
                    energy = uiState.userStats.energy,
                    streak = uiState.userStats.streak,
                    experience = uiState.userStats.experience
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
                            Color(0xFFBBDEFB),
                        )
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            // ✅ Lista dinámica desde ViewModel
            items(uiState.topics) { topic ->
                TopicCard(
                    title = topic.name,
                    progress = topic.progress,
                    color = getTopicColor(topic.id),
                    onClick = { viewModel.onTopicClick(topic) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
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



