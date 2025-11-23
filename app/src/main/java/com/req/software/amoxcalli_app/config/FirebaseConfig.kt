package com.req.software.amoxcalli_app.config

/**
 * Configuración de Firebase para Amoxcalli App
 *
 * INSTRUCCIONES:
 * 1. Ve a Firebase Console: https://console.firebase.google.com/
 * 2. Selecciona tu proyecto "Amoxcalli"
 * 3. Ve a Authentication > Sign-in method
 * 4. Habilita Google como proveedor
 * 5. Copia el Web Client ID de la configuración del SDK web
 * 6. Reemplaza el valor de WEB_CLIENT_ID abajo
 */
object FirebaseConfig {
    /**
     * Web Client ID de Firebase
     * Formato: XXXXXXXXXX-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX.apps.googleusercontent.com
     *
     * TODO: Reemplazar con tu Web Client ID real de Firebase Console
     */
    const val WEB_CLIENT_ID = "YOUR_WEB_CLIENT_ID.apps.googleusercontent.com"
}

