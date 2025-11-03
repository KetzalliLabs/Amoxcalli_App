# Amoxcalli — Tu casa de saber para LSM
# Alfredo
> Inclusión que se transforma con tecnología. Hecha en México por Ketzalli Labs. 

## ✨ Visión general

Amoxcalli es una app móvil para aprender Lengua de Señas Mexicana (LSM) mediante microlecciones con video, práctica interactiva y evaluación formativa continua. Incorpora repetición espaciada, recuperación activa y motivación gamificada (XP, rachas, medallas y cacao como moneda blanda). También funciona **offline**, ofrece accesibilidad integral y adapta el contenido según el desempeño. 

---

## 🎯 Objetivos del producto

- **Cerrar brechas de acceso** a materiales de calidad y rutas guiadas para aprender LSM.
- **Acompañar a docentes y familias** con progreso medible, detección de rezagos y recursos confiables.
- **Operar bien con conectividad limitada**, manteniendo la motivación con metas y retos breves. 

---

## 🧩 Funcionalidades clave

### Diccionario y contenido
- Diccionario por categorías con **videos de LSM** (controles de reproducción y velocidad).
- **Búsqueda** por texto; marcar favoritas o para repaso. 

### Práctica y evaluación
- **Quizzes** (opción múltiple, video, selección de seña correcta) con **retroalimentación inmediata**.
- Registro de desempeño por módulo; **repetición espaciada** de errores. 

### Gamificación y progreso
- **XP** por lecciones/prácticas; **cacao** como moneda blanda para pistas y ejercicios extra.
- **Medallas** (Jade, Obsidiana, Quetzal, etc.), rachas y niveles. 

### Personalización y accesibilidad
- **Modo offline** (descarga de unidades y sincronización posterior).
- **Rutas personalizadas** según desempeño y **accesibilidad visual** (WCAG 2.2 AA, soporte lector de pantalla, contraste alto, opción de desactivar animaciones). 

### Administración, analítica y reportes
- Gestión de señas, videos, categorías y bancos de preguntas.
- **Reportes** en PDF/Excel; métricas: accesos, videos más vistos, tasas de acierto, mapa de dominio, cohortes y alertas de rezago. 

---

## 👥 Casos de uso (resumen)

- **Completar lección con video LSM**: precarga ejercicios, respuesta con feedback instantáneo, avance y registro de progreso, asignación de XP/racha.
- **Registro e inicio de sesión**: email/federado, verificación, meta diaria y nivel inicial.
- **Descarga para uso offline y sincronización**: descarga de unidades, trabajo sin red y consolidación de progreso al reconectarse. 

---

## 📐 Requerimientos (extracto)

### Funcionales
- Diccionario por categorías con video; búsqueda y marcadores.
- Quizzes con feedback inmediato; registro de desempeño y repetición espaciada.
- Gamificación con XP, **cacao**, medallas y rachas; progreso por módulo/sesión.
- Modo **offline**; adaptación de contenido; accesibilidad.
- Panel de administración; analítica y reportes; autenticación segura y recuperación de cuenta. 

### No funcionales
- **Inicio < 3 s** (gama media) y **pantallas clave < 500 ms** post‑carga; latencia de acciones críticas < 300 ms en Wi‑Fi/5G.
- Disponibilidad backend **> 99.9%**; sesiones sin crash **> 99.5%**.
- **WCAG 2.2 AA**; videos 720p+ 24–60 fps; caché de próximos ejercicios.
- Seguridad y privacidad: cifrado E2E en tránsito/reposo, mínima recolección con consentimiento y panel de privacidad, gestión de secretos, rate-limits y protección anti‑abuso; controles parentales y cumplimiento para menores. 

---

## 🏗️ Arquitectura y diseño (alto nivel)

- **App móvil** con módulos de lecciones, diccionario, práctica/Estelas y perfil/progreso.
- **Sincronización** de progreso y descargas para **modo offline** (LWW + reintentos).
- **Panel de administración** y **servicios de analítica** para monitoreo de uso/aprendizaje. 

> *Estelas* certifican dominio por módulo con reactivos adaptativos e insights de errores frecuentes y rutas de repaso. 

---

## 🔐 Seguridad y cumplimiento

- Autenticación segura, recuperación de cuenta y protección de datos personales y de menores.
- Políticas de privacidad con consentimiento explícito, controles de notificaciones y **controles parentales**. 

---

## 🧪 Pruebas y despliegue

- Pruebas de rendimiento (render, latencia UI), estabilidad (tasa de crash), accesibilidad (WCAG), reproducción multimedia y **offline/replicación**.
- Observabilidad/analítica para métricas de adopción y aprendizaje; despliegue con CI/CD y monitoreo SLO/SLA. 

---

## 🛣️ Roadmap (Sprint 1 → siguientes)

- Consolidar MVP de lecciones + diccionario + quizzes con feedback.
- Implementar rachas/XP/medallas/cacao y primeras **Estelas**.
- Offline (descarga/sync), panel de administración y reportes base.
- Endurecer privacidad, controles parentales y anti‑abuso. 

---

## 🤝 Contribuir

1. Crear rama a partir de `main` y seguir la guía de estilo/linters.
2. Agregar pruebas y actualizar documentación relevante.
3. Abrir Pull Request con descripción y evidencia (capturas/metrics). 

---

## 🏷️ Créditos y equipo

Equipo #3 (Grupo 504):
- **Santiago Quintana Moreno** — Scrum Master; Senior Full‑Stack; QA/Calidad
- **Alfredo Luce Morales** — Product Owner; UX Research; Operación de Contenido LSM
- **Ernesto De Luna Quintero** — Backend; Seguridad/Privacidad; Cumplimiento de Datos
- **Israel Booz Rodríguez Zavala** — Mobile Frontend; Pipeline de Media/Cámara; Accesibilidad
- **Emilio Salas Porras** — DevOps & SRE; Analítica/Datos; Observabilidad y CI/CD 

---

## 📚 Fuentes base

- **EQ3_AmoxcalliApp_Sprint1** — Documento de requerimientos, casos de uso, NFRs, riesgos, diseño y roles. 
- **KetzalliLabs Proposal: Amoxcalli** — Propuesta de producto, principios pedagógicos, Estelas, gamificación y diferenciadores. 

