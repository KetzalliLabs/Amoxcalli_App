package com.req.software.amoxcalli_app.ui.components.cards

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Card reutilizable con efecto 3D y animaciones
 * 
 * CARACTERÍSTICAS:
 * - Efecto 3D con sombra
 * - Animación al presionar (escala)
 * - Degradado de color
 * - Borde brillante opcional
 * - Elevación animada
 * 
 * @param title Nombre del tema
 * @param progress Progreso en porcentaje (0-100)
 * @param color Color de fondo del card
 * @param onClick Acción al hacer click
 * @param enable3D Activar efecto 3D (por defecto true)
 */
@Composable
fun TopicCard(
    title: String,
    progress: Int,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enable3D: Boolean = false  // Nuevo parámetro
) {
    // ===== ANIMACIONES =====
    
    // 1. Detectar cuando se presiona
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // 2. Animación de escala al presionar
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,  // Se encoge al presionar
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "card_scale"
    )
    
    // 3. Animación de elevación (sombra)
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 4.dp else 8.dp,  // Sombra menor al presionar
        animationSpec = tween(
            durationMillis = 150,
            easing = FastOutSlowInEasing
        ),
        label = "card_elevation"
    )
    
    // 4. Animación de brillo (solo si progreso > 0)
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer_transition")
    
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = if (progress >= 100) 0.3f else 0f,
        targetValue = if (progress >= 100) 0.7f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_alpha"
    )
    
    // ===== COMPONENTE VISUAL =====
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (enable3D) elevation else 0.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false,
                ambientColor = if (enable3D) color.copy(alpha = 0.3f) else Color.Transparent,
                spotColor = if (enable3D) color.copy(alpha = 0.5f) else Color.Transparent
            )
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = if (enable3D) {
                    Brush.verticalGradient(
                        colors = listOf(
                            color.copy(alpha = 1f),     // Color original arriba
                            color.copy(alpha = 0.85f)   // Más oscuro abajo (efecto 3D)
                        )
                    )
                } else {
                    Brush.verticalGradient(listOf(color, color))  // Sin degradado
                }
            )
            // Borde brillante si el progreso es 100%
            .then(
                if (progress >= 100) {
                    Modifier.border(
                        width = (1 + shimmerAlpha * 2).dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = shimmerAlpha),
                                Color(0xFFFFD700).copy(alpha = shimmerAlpha * 0.7f),  // Dorado
                                Color.White.copy(alpha = shimmerAlpha)
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                } else {
                    Modifier
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,  // Sin ripple, usamos nuestras animaciones
                onClick = onClick
            )
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {

        
        // Contenido del card
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Título con sombra de texto si está completo
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = if (progress >= 100) FontWeight.Bold else FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            
            // Porcentaje con fondo si es 100%
            if (progress >= 100) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "✓ $progress%",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)  // Dorado
                    )
                }
            } else {
                Text(
                    text = "$progress%",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

/**
 * Card de tema con barra de progreso visual y efectos 3D
 */
@Composable
fun TopicCardWithProgressBar(
    title: String,
    progress: Int,
    color: Color,
    progressColor: Color = Color.White,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enable3D: Boolean = true
) {
    // ===== ANIMACIONES =====
    
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "card_scale"
    )
    
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 4.dp else 8.dp,
        animationSpec = tween(150),
        label = "card_elevation"
    )
    
    // Animación de la barra de progreso
    val animatedProgress by animateFloatAsState(
        targetValue = progress / 100f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "progress_animation"
    )
    
    // ===== COMPONENTE VISUAL =====
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (enable3D) elevation else 0.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false,
                ambientColor = if (enable3D) color.copy(alpha = 0.3f) else Color.Transparent,
                spotColor = if (enable3D) color.copy(alpha = 0.5f) else Color.Transparent
            )
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = if (enable3D) {
                    Brush.verticalGradient(
                        colors = listOf(
                            color.copy(alpha = 1f),
                            color.copy(alpha = 0.85f)
                        )
                    )
                } else {
                    Brush.verticalGradient(listOf(color, color))
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "$progress%",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Barra de progreso con animación
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = progressColor,
            trackColor = Color.White.copy(alpha = 0.3f)
        )
    }
}

// ============================================
// PREVIEWS
// ============================================

@Preview(name = "Topic Card 3D - Completo", showBackground = true)
@Composable
fun TopicCard3DPreview() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        TopicCard(
            title = "Abecedario",
            progress = 100,
            color = Color(0xFF5C6BC0),
            onClick = {},
            enable3D = true
        )
    }
}

@Preview(name = "Topic Card - Comparación 2D vs 3D", showBackground = true)
@Composable
fun TopicCardComparisonPreview() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Sin Efecto 3D:", fontWeight = FontWeight.Bold)
        TopicCard(
            title = "Animales",
            progress = 45,
            color = Color(0xFF4CAF50),
            onClick = {},
            enable3D = false
        )
        
        Text("Con Efecto 3D:", fontWeight = FontWeight.Bold)
        TopicCard(
            title = "Animales",
            progress = 45,
            color = Color(0xFF4CAF50),
            onClick = {},
            enable3D = true
        )
    }
}

@Preview(name = "Topic Cards - Diferentes Estados", showBackground = true)
@Composable
fun TopicCardStatesPreview() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TopicCard(
            title = "Completado ✓",
            progress = 100,
            color = Color(0xFF5C6BC0),
            onClick = {}
        )
        
        TopicCard(
            title = "En Progreso",
            progress = 65,
            color = Color(0xFF4CAF50),
            onClick = {}
        )
        
        TopicCard(
            title = "Poco Avance",
            progress = 15,
            color = Color(0xFFFDD835),
            onClick = {}
        )
        
        TopicCard(
            title = "Sin Iniciar",
            progress = 0,
            color = Color(0xFFEF5350),
            onClick = {}
        )
    }
}

@Preview(name = "Topic Card - Con Barra de Progreso 3D", showBackground = true)
@Composable
fun TopicCardWithProgressBar3DPreview() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TopicCardWithProgressBar(
            title = "Abecedario",
            progress = 100,
            color = Color(0xFF5C6BC0),
            onClick = {}
        )
        
        TopicCardWithProgressBar(
            title = "Animales",
            progress = 60,
            color = Color(0xFF4CAF50),
            onClick = {}
        )
        
        TopicCardWithProgressBar(
            title = "Preguntas básicas",
            progress = 25,
            color = Color(0xFF9C27B0),
            onClick = {}
        )
    }
}