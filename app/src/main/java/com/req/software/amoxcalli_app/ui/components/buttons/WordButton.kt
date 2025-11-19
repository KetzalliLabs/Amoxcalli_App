package com.req.software.amoxcalli_app.ui.components.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WordButton(
    text: String,
    isFavorite: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp,
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)   // cuadrado
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // Favorito?
                Text(
                    text = if (isFavorite) "❤️" else "",
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.End)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = text,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
