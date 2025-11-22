# 📱 Amoxcalli App - Estructura del Proyecto

## 🎯 Propósito
Aplicación Android para aprender Lenguaje de Señas Mexicanas (LSM) usando Jetpack Compose.

## 📁 Estructura de Carpetas

```
app/src/main/java/com/req/software/amoxcalli_app/
│
├── 🎨 ui/                          # Capa de presentación (UI)
│   ├── components/                 # Componentes reutilizables
│   │   ├── buttons/               # Botones personalizados
│   │   │   └── PrimaryButton.kt   # Botón principal verde
│   │   ├── cards/                 # Cards reutilizables
│   │   │   └── TopicCard.kt       # Card para temas con progreso
│   │   └── headers/               # Headers y barras superiores
│   │       └── StatsHeader.kt     # Header de estadísticas (monedas, energía, etc)
│   │
│   ├── screens/                   # Pantallas completas de la app
│   │   ├── home/                  # Pantalla principal
│   │   │   └── HomeScreen.kt      # IMPLEMENTADA
│   │   ├── topics/                # Pantalla de temas
│   │   ├── quiz/                  # Pantalla de quiz
│   │   ├── practice/              # Pantalla de práctica
│   │   └── profile/               # Pantalla de perfil
│   │
│   ├── navigation/                # Sistema de navegación
│   │   └── NavGraph.kt            # Rutas y navegación
│   │
│   └── theme/                     # Temas y estilos
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
│
├── 🏗️ domain/                     # Lógica de negocio (independiente de Android)
│   ├── model/                     # Modelos de dominio
│   │   └── Topic.kt               # Modelo de temas y estadísticas
│   ├── usecase/                   # Casos de uso (TODO)
│   └── repository/                # Interfaces de repositorio (TODO)
│
├── 💾 data/                        # Capa de datos
│   ├── repository/                # Implementaciones de repositorios
│   ├── dto/                       # Data Transfer Objects
│   │   └── ItemRequests.kt
│   └── local/                     # Base de datos local (TODO)
│
├── 🔌 service/                     # Servicios externos
│   ├── FirebaseItemService.kt
│   └── ItemService.kt
│
└── 🎮 viewmodel/                   # ViewModels (MVVM)
    ├── HomeViewModel.kt           # ✅ IMPLEMENTADO
    └── ItemsViewModel.kt
```

## 🎨 Componentes Creados

### ✅ Componentes Reutilizables

1. **StatsHeader** (`ui/components/headers/`)
   - Muestra estadísticas del usuario (🪙 monedas, ⚡ energía, 🔥 racha, XP)
   - Reutilizable en cualquier pantalla
   - Diseño responsive con capsulas redondeadas

2. **TopicCard** (`ui/components/cards/`)
   - Card para mostrar temas con progreso
   - Versión simple y versión con barra de progreso
   - Colores personalizables
   - Click handling incluido

3. **PrimaryButton** & **SecondaryButton** (`ui/components/buttons/`)
   - Botones estilizados consistentes
   - Colores y texto personalizables
   - Diseño redondeado moderno

### ✅ Pantallas

1. **HomeScreen** (`ui/screens/home/`)
   - Pantalla principal implementada
   - Muestra estadísticas del usuario
   - Lista de temas recientes
   - Lista de temas recomendados
   - Botones para quiz diario y práctica

### ✅ ViewModels

1. **HomeViewModel** (`viewmodel/`)
   - Maneja el estado de la pantalla Home
   - Flujos reactivos con StateFlow
   - Preparado para integración con Firebase

## 🚀 Próximos Pasos

### Para el Equipo

#### 👤 Persona 1: Navegación y Bottom Bar
- [ ] Implementar Navigation Compose
- [ ] Crear Bottom Navigation Bar con iconos (Home, Temas, Buscar, Perfil)
- [ ] Conectar todas las rutas

#### 👤 Persona 2: Pantalla de Temas
- [ ] Crear `TopicsScreen.kt`
- [ ] Implementar `TopicsViewModel.kt`
- [ ] Listar todos los temas disponibles con filtros

#### 👤 Persona 3: Pantalla de Quiz
- [ ] Crear `QuizScreen.kt`
- [ ] Implementar `QuizViewModel.kt`
- [ ] Diseñar componentes de pregunta/respuesta

#### 👤 Persona 4: Pantalla de Práctica
- [ ] Crear `PracticeScreen.kt`
- [ ] Implementar `PracticeViewModel.kt`
- [ ] Integrar reproducción de videos/imágenes de señas

#### 👤 Persona 5: Perfil e Integración Firebase
- [ ] Crear `ProfileScreen.kt`
- [ ] Implementar autenticación completa
- [ ] Conectar ViewModels con Firestore
- [ ] Implementar repositorios

## 🎯 Convenciones de Código

### Nomenclatura
- **Composables**: PascalCase (ej: `HomeScreen`, `TopicCard`)
- **ViewModels**: PascalCase + "ViewModel" (ej: `HomeViewModel`)
- **Variables**: camelCase (ej: `userStats`, `recentTopics`)
- **Constantes**: UPPER_SNAKE_CASE (ej: `MAX_TOPICS`)

### Organización de Archivos
- Un composable principal por archivo
- Composables privados en el mismo archivo
- Helpers y funciones auxiliares al final

### Comentarios
```kotlin
/**
 * Descripción del componente/función
 * @param parametro Descripción del parámetro
 */
```

## 🔧 Dependencias Importantes

```kotlin
// Jetpack Compose
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")

// ViewModel
implementation("androidx.lifecycle:lifecycle-viewmodel-compose")

// Firebase
implementation("com.google.firebase:firebase-firestore-ktx")
implementation("com.google.firebase:firebase-auth-ktx")

// Navigation (TODO: Agregar)
implementation("androidx.navigation:navigation-compose:2.7.5")

// Coil para imágenes (TODO: Agregar)
implementation("io.coil-kt:coil-compose:2.5.0")
```

## 📝 Buenas Prácticas

1. **Separación de Responsabilidades**: UI, Domain, Data
2. **Estado Unidireccional**: ViewModel → Screen
3. **Composables Stateless**: Reciben datos y callbacks
4. **Preview para cada componente**: Facilita desarrollo
5. **Reutilización**: Extraer componentes comunes
6. **Naming consistente**: Facilita búsqueda de archivos

## 🎨 Paleta de Colores

```kotlin
Abecedario: Color(0xFF5C6BC0)  // Azul
Animales:   Color(0xFF4CAF50)  // Verde
Vehículos:  Color(0xFFFDD835)  // Amarillo
Verbos:     Color(0xFFEF5350)  // Rojo
Preguntas:  Color(0xFF9C27B0)  // Morado
```

## 🤝 Workflow de Git (Recomendado)

1. Crear rama por feature: `git checkout -b feature/quiz-screen`
2. Hacer commits descriptivos: `git commit -m "Add quiz question component"`
3. Push y crear PR: `git push origin feature/quiz-screen`
4. Code review antes de merge
5. Mergear a `main` o `develop`

## 📚 Recursos Útiles

- [Jetpack Compose Guidelines](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)
- [Firebase Android](https://firebase.google.com/docs/android/setup)
