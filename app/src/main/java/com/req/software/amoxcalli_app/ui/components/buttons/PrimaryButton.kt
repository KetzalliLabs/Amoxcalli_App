package com.req.software.amoxcalli_app.ui.components.buttons

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp

/**
 * Botón principal con efecto 3D y animación de pulso
 * 
 * CARACTERÍSTICAS:
 * - Efecto de pulso (crece y se encoge)
 * - Borde brillante que parpadea
 * - Sombra de color animada
 * - Degradado de fondo
 * - Animación al presionarpri
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF4CAF50), // Verde por defecto
    textColor: Color = Color.White,
    enablePulse: Boolean = false // ← Activar efecto de pulso
) {
    // ===== ANIMACIONES =====
    
    // 1. Detectar cuando el botón está siendo presionado
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // 2. Animación de PULSO - El botón crece y se encoge
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_transition")
    
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = if (enablePulse) 1f else 1f,
        targetValue = if (enablePulse) 1.08f else 1f,  // Crece 8%
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,  // 1 segundo para crecer
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse  // Regresa al tamaño normal
        ),
        label = "pulse_scale"
    )
    
    // 3. Animación de BORDE BRILLANTE - Brilla en el borde
    val borderGlow by infiniteTransition.animateFloat(
        initialValue = if (enablePulse) 0.3f else 0f,
        targetValue = if (enablePulse) 1f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 800,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "border_glow"
    )

    // 4. Animación de SOMBRA DE COLOR - Sombra que cambia de intensidad
    val shadowElevation by infiniteTransition.animateValue(
        initialValue = if (enablePulse) 8.dp else 12.dp,
        targetValue = if (enablePulse) 20.dp else 12.dp,
        typeConverter = Dp.VectorConverter,  // ← Convierte valores Dp
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shadow_elevation"
    )
    
    // 5. Animación al PRESIONAR - Efecto de click (CORREGIDO)
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,  // Se encoge al presionar
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "press_scale"
    )
    
    // 6. Brillo interior que respira
    val innerGlow by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "inner_glow"
    )
    
    // ===== COMPONENTE VISUAL =====
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .graphicsLayer {
                // Combinar ambas escalas: pulso + presionar
                val finalScale = pulseScale * pressScale
                scaleX = finalScale
                scaleY = finalScale
            }
            .shadow(
                elevation = shadowElevation,
                shape = RoundedCornerShape(16.dp),
                clip = false,
                ambientColor = if (enablePulse) backgroundColor else Color.Transparent,
                spotColor = if (enablePulse) backgroundColor else Color.Transparent
            )
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        backgroundColor.copy(alpha = 1f),
                        backgroundColor.copy(alpha = 0.85f),
                    )
                )
            )
            // Borde brillante animado
            .then(
                if (enablePulse) {
                    Modifier.border(
                        width = (2 + borderGlow * 2).dp,  // Borde de 2-4dp
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = borderGlow),
                                backgroundColor.copy(alpha = borderGlow * 0.5f),
                                Color.White.copy(alpha = borderGlow)
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
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // Capa de brillo interior
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = innerGlow * 0.4f),
                            Color.Transparent
                        )
                    )
                )
        )
        
        // Texto del botón
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

/**
 * Botón secundario (morado) con mismo efecto de pulso
 */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enablePulse: Boolean = false
) {
    PrimaryButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        backgroundColor = Color(0xFF9C27B0),  // Morado
        enablePulse = enablePulse
    )
}

// ============================================
// PREVIEWS
// ============================================

@Preview(name = "Botón Normal vs Pulso", showBackground = true)
@Composable
fun PulseComparisonPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Botón Normal:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        PrimaryButton(
            text = "Sin Efecto",
            onClick = {},
            backgroundColor = Color(0xFF4CAF50),
            enablePulse = false
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text("Botón con Pulso:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        PrimaryButton(
            text = "¡Con Pulso y Brillo!",
            onClick = {},
            backgroundColor = Color(0xFFFF5722),  // Naranja
            enablePulse = true  // ← ACTIVADO
        )
    }
}

@Preview(name = "Botones con Pulso - Diferentes Colores", showBackground = true)
@Composable
fun PulseButtonsPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            "Botones con Efecto de Pulso:",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        
        PrimaryButton(
            text = "¡Quiz Diario Disponible!",
            onClick = {},
            backgroundColor = Color(0xFFFF5722),  // Naranja
            enablePulse = true
        )
        
        PrimaryButton(
            text = "¡Nueva Lección!",
            onClick = {},
            backgroundColor = Color(0xFF2196F3),  // Azul
            enablePulse = true
        )
        
        PrimaryButton(
            text = "¡Racha en Peligro!",
            onClick = {},
            backgroundColor = Color(0xFFF44336),  // Rojo
            enablePulse = true
        )
        
        SecondaryButton(
            text = "¡Oferta Especial!",
            onClick = {},
            enablePulse = true
        )
        
        Text(
            "Botones sin Pulso:",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 16.dp)
        )
        
        PrimaryButton(
            text = "Comenzar",
            onClick = {},
            backgroundColor = Color(0xFF4CAF50)
        )
        
        SecondaryButton(
            text = "Cancelar",
            onClick = {}
        )
    }
}

@Preview(name = "Demostración Interactiva", showBackground = true)
@Composable
fun InteractiveDemoPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "🎯 Presiona los botones para ver el efecto",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        
        PrimaryButton(
            text = "Botón Normal",
            onClick = {},
            backgroundColor = Color(0xFF607D8B)  // Gris azulado
        )
        
        PrimaryButton(
            text = "🔥 ¡BOTÓN CON PULSO! 🔥",
            onClick = {},
            backgroundColor = Color(0xFFFF6F00),  // Naranja oscuro
            enablePulse = true
        )
    }
}