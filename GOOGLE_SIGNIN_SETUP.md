# Configuración de Google Sign-In para Amoxcalli App

## ✅ Implementación Completada

Se ha implementado exitosamente la autenticación con Google Sign-In utilizando Firebase. Los archivos creados incluyen:

### Archivos Creados:
1. **AuthViewModel.kt** - ViewModel que maneja la autenticación con Firebase
2. **LoginScreen.kt** - Pantalla de inicio de sesión con botón de Google Sign-In
3. **HomeScreen.kt** - Pantalla principal después del login
4. **MainActivity.kt** - Actualizado para manejar la navegación entre pantallas

### Dependencias Agregadas:
- `com.google.android.gms:play-services-auth:21.2.0` - Google Play Services para autenticación

## 🔧 Pasos para Completar la Configuración

### 1. Obtener el Web Client ID de Firebase

1. Ve a la [Consola de Firebase](https://console.firebase.google.com/)
2. Selecciona tu proyecto "Amoxcalli"
3. Ve a **Authentication** > **Sign-in method**
4. Habilita **Google** como proveedor de autenticación
5. En la configuración del SDK web, encontrarás el **Web Client ID**
6. Copia el ID (tiene el formato: `XXXXXXXXXX-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX.apps.googleusercontent.com`)

### 2. Configurar el Web Client ID en la App

Abre el archivo `LoginScreen.kt` y reemplaza la línea:

```kotlin
val webClientId = "YOUR_WEB_CLIENT_ID.apps.googleusercontent.com"
```

Con tu Web Client ID real de Firebase.

### 3. Configurar SHA-1 en Firebase (Importante)

Para que Google Sign-In funcione en Android, necesitas agregar el SHA-1 de tu aplicación:

#### Obtener SHA-1 de Debug:
```bash
cd android
gradlew signingReport
```

O en Windows:
```cmd
gradlew.bat signingReport
```

#### Agregar SHA-1 a Firebase:
1. Copia el SHA-1 del reporte
2. Ve a Firebase Console > Configuración del proyecto
3. Selecciona tu app Android
4. Haz clic en "Agregar huella digital"
5. Pega el SHA-1 y guarda

### 4. Descargar y Actualizar google-services.json

1. En Firebase Console, ve a Configuración del proyecto
2. Descarga el archivo `google-services.json` actualizado
3. Reemplázalo en la carpeta `app/`

## 🎨 Características Implementadas

### LoginScreen
- ✅ Botón de "Iniciar sesión con Google"
- ✅ Indicador de carga durante la autenticación
- ✅ Manejo de errores
- ✅ UI moderna con Material Design 3

### AuthViewModel
- ✅ Gestión del estado de autenticación
- ✅ Integración con Firebase Auth
- ✅ Manejo de credenciales de Google
- ✅ StateFlow para observar cambios de usuario

### HomeScreen
- ✅ Pantalla de bienvenida con información del usuario
- ✅ Botón de cerrar sesión
- ✅ TopAppBar con navegación

## 🚀 Cómo Probar

1. Completa los pasos de configuración anteriores
2. Conecta un dispositivo Android o inicia un emulador
3. Ejecuta la aplicación
4. Haz clic en "Iniciar sesión con Google"
5. Selecciona una cuenta de Google
6. ¡Deberías ver la pantalla de inicio!

## 📱 Flujo de la Aplicación

```
MainActivity
    ↓
AmoxcalliApp (Composable)
    ↓
¿Usuario autenticado?
    ├─ NO → LoginScreen
    │        ↓
    │   Google Sign-In
    │        ↓
    │   Firebase Auth
    │        ↓
    └─ SÍ → HomeScreen
             ↓
        [Cerrar Sesión] → LoginScreen
```

## ⚠️ Notas Importantes

1. **Web Client ID**: Es esencial configurar el Web Client ID correcto de Firebase
2. **SHA-1**: Sin el SHA-1 correcto, la autenticación fallará silenciosamente
3. **google-services.json**: Asegúrate de tener la versión más reciente del archivo
4. **Internet**: Se requiere conexión a internet para la autenticación

## 🔐 Seguridad

- Las credenciales se manejan de forma segura mediante Firebase
- El token de ID se valida en el servidor de Firebase
- No se almacenan contraseñas localmente
- Se utiliza OAuth 2.0 para la autenticación

## 📚 Recursos Adicionales

- [Firebase Authentication Documentation](https://firebase.google.com/docs/auth)
- [Google Sign-In for Android](https://developers.google.com/identity/sign-in/android/start)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)

---

**¡Implementación completada exitosamente!** 🎉

Si encuentras algún problema, revisa que todos los pasos de configuración estén completos.

