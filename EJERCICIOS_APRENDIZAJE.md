# 📚 Ejercicios de Aprendizaje - Amoxcalli App

## 🎯 Objetivo
Aprender Jetpack Compose MODIFICANDO el código existente paso a paso.

---

## 📅 SEMANA 1: Entender lo que ya tienes

### Día 1-2: Componentes Básicos (2 horas)

#### ✏️ Ejercicio 1: Modifica `TopicCard.kt`
**Objetivo**: Entender cómo funciona un Composable

**Tareas:**
1. Abre `TopicCard.kt`
2. Cambia el tamaño del texto de 18.sp a 20.sp
3. Cambia el RoundedCornerShape de 16.dp a 24.dp (más redondeado)
4. Cambia el padding vertical de 16.dp a 20.dp
5. Ve los cambios en el Preview

**Pregúntate:**
- ¿Qué hace `@Composable`?
- ¿Qué es `Modifier`?
- ¿Cómo se combinan los modificadores (.fillMaxWidth(), .padding(), etc)?

#### ✏️ Ejercicio 2: Personaliza Colores
**Archivo**: `TopicCard.kt`

**Tareas:**
1. En el Preview, cambia el color azul `Color(0xFF5C6BC0)` a tu color favorito
2. Experimenta con diferentes valores hexadecimales
3. Agrega un nuevo color para un tema nuevo (ej: "Números")

**Aprende:**
- Formato de colores en Android (0xAARRGGBB)
- Cómo usar colores con transparencia (alpha)

#### ✏️ Ejercicio 3: Crea tu Propio Botón
**Archivo**: Crea `app/src/main/java/com/req/software/amoxcalli_app/ui/components/buttons/CustomButton.kt`

```kotlin
package com.req.software.amoxcalli_app.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
 * TU EJERCICIO: Crea un botón circular
 * Sigue el patrón de PrimaryButton.kt pero hazlo circular
 */
@Composable
fun CircularButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFFF5722) // Naranja
) {
    // TODO: Implementa aquí usando Box, CircleShape, y Text
    // Pista: usa CircleShape en lugar de RoundedCornerShape
}

@Preview(showBackground = true)
@Composable
fun CircularButtonPreview() {
    CircularButton(
        text = "GO",
        onClick = {}
    )
}
```

**Reto**: Hazlo funcionar por tu cuenta. Si te atoras, compara con `PrimaryButton.kt`

---

### Día 3-4: Estados y Interactividad (3 horas)

#### ✏️ Ejercicio 4: Agrega un Contador
**Archivo**: Crea `app/src/main/java/com/req/software/amoxcalli_app/ui/components/common/Counter.kt`

```kotlin
package com.req.software.amoxcalli_app.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * EJERCICIO: Aprende sobre Estado en Compose
 */
@Composable
fun Counter() {
    // TODO: 
    // 1. Crea una variable de estado usando: var count by remember { mutableStateOf(0) }
    // 2. Crea un Column con:
    //    - Text que muestre el count
    //    - Row con dos botones: "-" y "+"
    //    - Botón "-" decrementa count
    //    - Botón "+" incrementa count
    // 3. IMPORTANTE: cuando cambias count, la UI se actualiza automáticamente
    
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Tu código aquí
    }
}

@Preview(showBackground = true)
@Composable
fun CounterPreview() {
    Counter()
}
```

**Aprende:**
- `remember` y `mutableStateOf` - El corazón de Compose
- Cómo funciona la recomposición
- Estado local vs estado compartido

#### ✏️ Ejercicio 5: Botón con Estado Visual
**Tarea**: Modifica `TopicCard.kt` para que cambie de color cuando lo presionas

```kotlin
// Agrega esto dentro de TopicCard
var isPressed by remember { mutableStateOf(false) }

val cardColor = if (isPressed) color.copy(alpha = 0.7f) else color

Box(
    modifier = modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(cardColor) // Usa el color dinámico
        .clickable { 
            isPressed = !isPressed
            onClick() 
        }
        // ... resto del código
```

---

### Día 5-7: Listas y Datos (4 horas)

#### ✏️ Ejercicio 6: Entiende LazyColumn
**Archivo**: Crea `app/src/main/java/com/req/software/amoxcalli_app/ui/screens/practice/PracticeListScreen.kt`

```kotlin
package com.req.software.amoxcalli_app.ui.screens.practice

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.req.software.amoxcalli_app.ui.components.cards.TopicCard
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding

/**
 * EJERCICIO: Aprende a mostrar listas eficientes
 */
@Composable
fun PracticeListScreen() {
    // Crea una lista de palabras para practicar
    val palabras = listOf(
        "Hola", "Adiós", "Gracias", "Por favor", 
        "Familia", "Amigo", "Casa", "Escuela"
    )
    
    LazyColumn {
        items(palabras) { palabra ->
            TopicCard(
                title = palabra,
                progress = 0,
                color = Color(0xFF4CAF50),
                onClick = { /* TODO: Navegar a práctica de esta palabra */ },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PracticeListScreenPreview() {
    PracticeListScreen()
}
```

**Aprende:**
- `LazyColumn` vs `Column` (¿Cuándo usar cada uno?)
- `items()` para iterar listas
- Performance en listas grandes

---

## 📅 SEMANA 2: Crea tus Propias Pantallas

### Proyecto: Pantalla de Perfil

#### ✏️ Ejercicio 7: Diseña ProfileScreen
**Archivo**: `app/src/main/java/com/req/software/amoxcalli_app/ui/screens/profile/ProfileScreen.kt`

**Requisitos:**
1. Header con foto de perfil (por ahora usa un emoji 👤)
2. Nombre del usuario
3. Estadísticas detalladas:
   - Total de palabras aprendidas
   - Días consecutivos de práctica
   - Temas completados
4. Botón de "Cerrar sesión"

**Pistas:**
- Reutiliza `StatsHeader` o crea uno nuevo
- Usa `Column` para organizar verticalmente
- Usa `Card` de Material3 para las secciones

---

## 📅 SEMANA 3: Navegación y ViewModels

#### ✏️ Ejercicio 8: Agrega Navigation Compose

1. **Agrega la dependencia** en `app/build.gradle.kts`:
```kotlin
implementation("androidx.navigation:navigation-compose:2.7.5")
```

2. **Crea el NavHost** (te daré estructura, tú la completas)

#### ✏️ Ejercicio 9: Conecta Firebase

1. Lee los datos de Firebase en `HomeViewModel`
2. Aprende sobre `StateFlow` y `collectAsState`

---

## 📅 SEMANA 4: Pulir y Completar

- Animaciones simples
- Manejo de errores
- Testing básico
- Preparar presentación

---

## 🎓 Recursos de Aprendizaje

### Videos Recomendados (Cortos y Efectivos):
1. **Philipp Lackner** - [Jetpack Compose Playlist](https://www.youtube.com/playlist?list=PLQkwcJG4YTCSpJ2NLhDTHhi6XBNfk9WiC)
2. **Stevdza-San** - Tutoriales en español si los prefieres

### Documentación:
- [Compose Basics](https://developer.android.com/jetpack/compose/tutorial)
- [State in Compose](https://developer.android.com/jetpack/compose/state)

### Práctica Diaria (30 min):
- Lee un archivo de código existente
- Modifica algo pequeño
- Ve el resultado en el Preview
- Pregúntate: "¿Por qué funciona así?"

---

## 🤔 Metodología de Aprendizaje

### Cuando veas código que no entiendes:

1. **Identifica** qué hace cada línea
2. **Comenta** el código explicándolo con tus palabras
3. **Modifica** un valor y ve qué pasa
4. **Pregunta** (a mí, a ChatGPT, a la documentación)
5. **Documenta** lo que aprendiste

### Ejemplo:
```kotlin
// Antes (no entiendes):
.fillMaxWidth()

// Después (entendiste):
.fillMaxWidth() // Hace que el componente ocupe todo el ancho disponible
                // Es como "width: 100%" en CSS
```

---

## ✅ Checklist de Progreso

### Semana 1:
- [ ] Entiendo qué es un @Composable
- [ ] Puedo modificar colores y tamaños
- [ ] Sé usar Modifier
- [ ] Entiendo remember y mutableStateOf
- [ ] Puedo crear un componente simple desde cero

### Semana 2:
- [ ] Creé mi primera pantalla completa
- [ ] Entiendo Column, Row, Box
- [ ] Puedo usar LazyColumn
- [ ] Sé reutilizar componentes

### Semana 3:
- [ ] Implementé navegación básica
- [ ] Entiendo ViewModel
- [ ] Conecto con Firebase
- [ ] Manejo estados de carga/error

### Semana 4:
- [ ] App funcional completa
- [ ] Proyecto listo para entregar
- [ ] Puedo explicar cómo funciona
- [ ] Estoy orgulloso del resultado

---

## 💡 Consejo Final

**NO COPIES Y PEGUES.** 

Escribe cada línea manualmente, aunque sea código que te doy. Escribir te obliga a leer y entender.

**¡Mucho éxito! 🚀**
