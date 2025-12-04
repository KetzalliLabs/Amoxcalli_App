# Amoxcalli 📱

**App Android para aprender Lengua de Señas Mexicana (LSM)** mediante microlecciones interactivas, gamificación y soporte offline. Desarrollada por Ketzalli Labs.

## ¿Qué es Amoxcalli?

*Amoxcalli* (del náhuatl: "casa de libros" o "casa de saber") es una plataforma educativa móvil enfocada en **democratizar el acceso al aprendizaje de la Lengua de Señas Mexicana**. En México, más de 2.3 millones de personas tienen discapacidad auditiva, pero los recursos de calidad para aprender LSM son escasos, fragmentados y mayormente presenciales.

**Nuestro objetivo:** Ofrecer una experiencia de aprendizaje estructurada, basada en evidencia pedagógica (repetición espaciada, retroalimentación inmediata) y accesible desde cualquier dispositivo Android, incluso sin conexión a internet.

**Desarrollada por Ketzalli Labs** — Equipo #3 del Grupo 504 — como proyecto académico en la materia de Seguridad en Desarrollo de Software. Combina principios de Clean Architecture, seguridad by design y enfoque en accesibilidad universal (WCAG 2.2 AA).

---

## Stack Técnico

**Frontend**
- **Kotlin** + **Jetpack Compose** (Material 3)
- Arquitectura **MVVM** con Clean Architecture
- **Retrofit** + OkHttp para networking
- **Room** para persistencia offline
- **Coil** para carga de imágenes/videos
- **Navigation Compose** para routing

**Backend & Auth**
- REST API desplegada en **Railway**
- **Firebase Auth** + Google Sign-In
- Videos en **Cloudflare R2 CDN**

**Package:** `com.req.software.amoxcalli_app`

---

## Características Principales

### Aprendizaje Interactivo
- Diccionario LSM con **videos HD** (720p+) categorizados y búsqueda
- Ejercicios adaptativos: Video→Texto, Imagen→Texto, Palabra→Seña
- Sistema de **repetición espaciada** para reforzar errores
- Retroalimentación inmediata con explicaciones

### Gamificación 🎮
- **XP** y niveles de progreso
- **Cacao** (moneda blanda) para pistas y contenido extra
- **Medallas** (Jade, Obsidiana, Quetzal) y rachas diarias
- **Estelas**: certificaciones de dominio por módulo

### Modo Offline
- Descarga de unidades completas
- Sincronización automática con estrategia **LWW** (Last-Write-Wins)
- Caché inteligente de próximos ejercicios

### Accesibilidad
- Cumplimiento **WCAG 2.2 AA**
- Soporte para lectores de pantalla
- Alto contraste y control de animaciones

---

## Arquitectura

```
app/src/main/java/com/req/software/amoxcalli_app/
├── ui/                    # Composables (screens, components, theme)
├── viewmodel/             # State management (StateFlow)
├── domain/                # Models & use cases
├── data/                  # Repositories & DTOs
├── service/               # Retrofit interfaces
├── network/               # HTTP clients
└── config/                # API & Firebase config
```

**Flujo de datos:**
```
UI → ViewModel → Repository → Service → Backend API
      ↓ StateFlow
    UI actualiza
```

**Navegación:**
- Autenticación (Login) → App principal (Bottom Nav)
- 5 secciones: Home, Learn, Library, Quiz, Profile

---

## Comandos de Desarrollo

### Build
```bash
./gradlew clean build           # Compilación completa
./gradlew assembleDebug         # APK debug
./gradlew installDebug          # Instalar en dispositivo
```

### Testing
```bash
./gradlew test                  # Unit tests
./gradlew connectedAndroidTest  # Instrumented tests
./gradlew lintDebug             # Análisis estático
```

### Debug
```bash
adb logcat | grep -i "amoxcalli"  # Filtrar logs
adb shell pm clear com.req.software.amoxcalli_app  # Limpiar datos
```

---

## Configuración

### API Backend
Actualizar `config/ApiConfig.kt`:
```kotlin
const val BASE_URL = "https://ketzallidbapi-production.up.railway.app/api/"
```

Para desarrollo local:
- **Emulador:** `http://10.0.2.2:PORT/api/`
- **Dispositivo físico:** `http://<TU_IP_LOCAL>:PORT/api/`

### Firebase
1. Descargar `google-services.json` desde Firebase Console
2. Colocar en `app/google-services.json`
3. Actualizar `WEB_CLIENT_ID` en `config/FirebaseConfig.kt`

---

## Requerimientos de Performance

- **Tiempo de inicio:** < 3s (dispositivos gama media)
- **Carga de pantallas:** < 500ms post-carga inicial
- **Latencia de acciones:** < 300ms en Wi-Fi/5G
- **Disponibilidad backend:** > 99.9%
- **Sesiones sin crash:** > 99.5%

---

## Seguridad 🔐

- Cifrado **E2E** en tránsito (HTTPS) y reposo
- Autenticación mediante **Firebase ID tokens** (Bearer)
- Rate limiting y protección anti-abuso en backend
- Gestión segura de secretos (no incluidos en control de versiones)
- Cumplimiento de privacidad para menores

---

## Equipo — Ketzalli Labs

**Equipo #3 (Grupo 504)**

| Rol | Responsable |
|-----|------------|
| Scrum Master & QA | Santiago Quintana Moreno |
| Product Owner & UX | Alfredo Luce Morales |
| Backend & Seguridad | Ernesto De Luna Quintero |
| Mobile & Accesibilidad | Israel Booz Rodríguez Zavala |
| DevOps & Analítica | Emilio Salas Porras |

---

## Contribuir

1. Crear rama desde `Development`
2. Seguir convenciones de Kotlin y Compose
3. Incluir tests y actualizar documentación
4. PR con descripción detallada y evidencia


---

**Licencia:** Proyecto académico — Ketzalli Labs © 2024
