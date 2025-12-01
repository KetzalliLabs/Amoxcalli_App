package com.req.software.amoxcalli_app.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.req.software.amoxcalli_app.data.dto.UserStatsResponse
import com.req.software.amoxcalli_app.domain.model.UserRole
import com.req.software.amoxcalli_app.ui.components.headers.StatsHeader
import com.req.software.amoxcalli_app.ui.theme.ThirdColor

/**
 * Temporary Admin Screen
 * Only accessible to users with admin or superadmin roles
 */
@Composable
fun AdminScreen(
    userStats: UserStatsResponse?,
    userRole: UserRole,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Top header with stats
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(ThirdColor)
                .padding(top = 12.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Back button
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
                Text(
                    text = "Panel de Administración",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Stats header
            val coins = userStats?.stats?.find { it.name == "coins" }?.currentValue ?: 0
            val energy = userStats?.stats?.find { it.name == "energy" }?.currentValue ?: 0
            val streak = userStats?.streak?.currentDays ?: 0
            val experience = userStats?.stats?.find { it.name == "experience_points" }?.currentValue ?: 0

            StatsHeader(
                coins = coins,
                energy = energy,
                streak = streak,
                experience = experience,
                medalsCount = userStats?.medals?.size ?: 0
            )
        }

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F6EF))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Admin badge icon
            Text(
                text = "🛡️",
                fontSize = 72.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Admin view temp text (as requested)
            Text(
                text = "admin view temp",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = ThirdColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Role information
            Text(
                text = "Rol: ${userRole.displayName}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Role ID: ${userRole.id}",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Temporary message
            Text(
                text = "Esta es una vista temporal de administrador.",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Text(
                text = "Aquí se implementarán las funcionalidades administrativas.",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}
