package com.req.software.amoxcalli_app.ui.components.headers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Header que muestra las estadísticas del usuario (monedas, energía, rachas, XP)
 * Componente reutilizable para mostrar stats en diferentes pantallas
 */
@Composable
fun StatsHeader(
    coins: Int,
    energy: Int,
    streak: Int,
    experience: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatItem(
            icon = "🪙",
            value = coins.toString(),
            backgroundColor = Color(0xFFF5F5DC)
        )
        StatItem(
            icon = "⚡",
            value = energy.toString(),
            backgroundColor = Color(0xFFFFF176)
        )
        StatItem(
            icon = "🔥",
            value = streak.toString(),
            backgroundColor = Color(0xFFFFCDD2)
        )
        StatItem(
            text = "XP: $experience",
            backgroundColor = Color(0xFFFFE082)
        )
    }
}

/**
 * Componente individual para cada estadística
 */
@Composable
private fun StatItem(
    icon: String? = null,
    text: String? = null,
    value: String? = null,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (icon != null) {
            Text(
                text = icon,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        
        if (text != null) {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        } else if (value != null) {
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

// ============================================
// PREVIEWS
// ============================================

@Preview(name = "Stats Header - Valores Altos", showBackground = true)
@Composable
fun StatsHeaderPreview() {
    StatsHeader(
        coins = 300,
        energy = 20,
        streak = 9,
        experience = 2500
    )
}

@Preview(name = "Stats Header - Valores Bajos", showBackground = true)
@Composable
fun StatsHeaderLowValuesPreview() {
    StatsHeader(
        coins = 50,
        energy = 5,
        streak = 1,
        experience = 150
    )
}

@Preview(name = "Stats Header - Usuario Nuevo", showBackground = true)
@Composable
fun StatsHeaderNewUserPreview() {
    StatsHeader(
        coins = 0,
        energy = 20,
        streak = 0,
        experience = 0
    )
}

@Preview(name = "Stats Header - Valores Máximos", showBackground = true)
@Composable
fun StatsHeaderMaxValuesPreview() {
    Column(modifier = Modifier.background(Color.White)) {
        StatsHeader(
            coins = 9999,
            energy = 99,
            streak = 365,
            experience = 99999
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Vista con valores extremos",
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}
