package com.req.software.amoxcalli_app.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.req.software.amoxcalli_app.ui.theme.ThirdColor

data class EditProfileUiState(
    val coins: String = "301",
    val username: String = "TheNetoLaNet4",
    val firstName: String = "Ernesto",
    val lastName: String = "De Luna Quintero",
    val email: String = "ernesto.deluna@tec.mx",
    val birthDate: String = "09/12/2003",
    val country: String = "México"
)

@Composable
fun EditProfileScreen(
    uiState: EditProfileUiState = EditProfileUiState(),
    onBack: () -> Unit = {},
    onBellClick: () -> Unit = {},
    onSendEmailClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ThirdColor)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {

        // ---------- BARRA SUPERIOR ----------
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
            }

            Text(
                text = "Perfil",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )

            CoinsPill(uiState.coins)

            Spacer(Modifier.width(8.dp))

            // Placeholder insignia hexagonal
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF8B6A2B))
            )

            Spacer(Modifier.width(8.dp))

            IconButton(onClick = onBellClick) {
                Icon(
                    Icons.Default.NotificationsNone,
                    contentDescription = "Notificaciones",
                    tint = Color.White
                )
            }
        }

        Spacer(Modifier.height(26.dp))

        // ---------- AVATAR ----------
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Foto de perfil",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }

            // Botón editar foto (overlay)
            Box(
                modifier = Modifier
                    .offset(x = 34.dp, y = 34.dp)
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Editar foto",
                    tint = ThirdColor,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(Modifier.height(24.dp))
        Divider(color = Color.White.copy(alpha = 0.5f))

        Spacer(Modifier.height(10.dp))

        // ---------- CAMPOS ----------
        ProfileLineField(
            label = "Nombre de usuario",
            value = uiState.username
        )

        ProfileLineField(
            label = "Nombre",
            value = uiState.firstName
        )

        ProfileLineField(
            label = "Apellido",
            value = uiState.lastName
        )

        EmailField(
            label = "Correo",
            value = uiState.email,
            onSendClick = onSendEmailClick
        )

        DateField(
            label = "Fecha de nacimiento",
            value = uiState.birthDate
        )

        CountryField(
            label = "País",
            value = uiState.country
        )

        Spacer(Modifier.height(24.dp))
    }
}

// ---------- COMPONENTES ----------
@Composable
private fun ProfileLineField(label: String, value: String) {
    Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.85f),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = value,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(10.dp))
        Divider(color = Color.White.copy(alpha = 0.5f))
    }
}

@Composable
private fun EmailField(label: String, value: String, onSendClick: () -> Unit) {
    Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.85f),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = onSendClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp)
            ) {
                Text("Enviar", color = ThirdColor, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(10.dp))
        Divider(color = Color.White.copy(alpha = 0.5f))
    }
}

@Composable
private fun DateField(label: String, value: String) {
    Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.85f),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Seleccionar fecha",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.height(10.dp))
        Divider(color = Color.White.copy(alpha = 0.5f))
    }
}

@Composable
private fun CountryField(label: String, value: String) {
    Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.85f),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Elegir país",
                tint = Color(0xFFB388FF),
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(Modifier.height(10.dp))
        Divider(color = Color.White.copy(alpha = 0.5f))
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
        Text(value, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFC94A))
                .border(1.dp, Color(0xFFE0A800), CircleShape)
        )
    }
}
