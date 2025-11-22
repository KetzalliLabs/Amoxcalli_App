package com.req.software.amoxcalli_app.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 🎯 EJERCICIO 1: Tu primer componente interactivo
 * 
 * TAREAS:
 * 1. Descomenta el código
 * 2. Completa los TODOs
 * 3. Ve el resultado en el Preview
 * 4. Experimenta cambiando valores
 * 
 * TIEMPO: 30 minutos
 */
@Composable
fun MiPrimerContador() {
    // TODO 1: Crea una variable de estado llamada 'contador'
    // Pista: var contador by remember { mutableStateOf(0) }
    var contador by remember { mutableStateOf(0) }
    
    // TODO 2: Crea otra variable de estado para el color de fondo
    // Cambia el color cada vez que el contador sea múltiplo de 5
    val colorFondo = if (contador % 5 == 0) Color(0xFF4CAF50) else Color(0xFF2196F3)
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(colorFondo)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Título
        Text(
            text = "Mi Contador de Aprendizaje 🎓",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        
        // Mostrar el contador
        Text(
            text = contador.toString(),
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        
        // Mensaje dinámico
        Text(
            text = when {
                contador == 0 -> "¡Comienza a contar!"
                contador < 10 -> "¡Vas bien!"
                contador < 20 -> "¡Excelente progreso!"
                else -> "¡Eres un campeón! 🏆"
            },
            fontSize = 16.sp,
            color = Color.White
        )
        
        // Botones
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // TODO 3: Haz que este botón RESTE 1 al contador
            Button(onClick = { contador-- }) {
                Text("- Menos", fontSize = 16.sp)
            }
            
            // TODO 4: Haz que este botón reinicie el contador a 0
            Button(onClick = { contador = 0 }) {
                Text("🔄 Reset", fontSize = 16.sp)
            }
            
            // TODO 5: Haz que este botón SUME 1 al contador
            Button(onClick = { contador++ }) {
                Text("+ Más", fontSize = 16.sp)
            }
        }
        
        // RETO EXTRA: Agrega un botón que sume 5 de golpe
        Button(onClick = { contador += 5 }) {
            Text("⚡ +5", fontSize = 16.sp)
        }
    }
}

/**
 * 🎨 Preview para ver tu trabajo
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MiPrimerContadorPreview() {
    MiPrimerContador()
}

/**
 * 📝 PREGUNTAS PARA REFLEXIONAR (Responde después de hacer el ejercicio):
 * 
 * 1. ¿Qué hace la función remember { mutableStateOf(0) }?
 *    R: Crea una variable que Compose "recuerda" entre recomposiciones
 *       Cuando cambia, la UI se actualiza automáticamente
 * 
 * 2. ¿Por qué cambia la UI cuando modificas 'contador'?
 *    R: Porque es un State. Compose observa los cambios y redibuja.
 * 
 * 3. ¿Qué pasa si quitas 'remember'?
 *    R: El contador se resetearía cada vez (¡pruébalo!)
 * 
 * 4. ¿Cómo funciona el cambio de color?
 *    R: Se calcula en cada recomposición basado en el valor del contador
 * 
 * 5. ¿Qué es Modifier?
 *    R: Una forma de modificar la apariencia y comportamiento de composables
 *       (tamaño, padding, colores, etc)
 */
