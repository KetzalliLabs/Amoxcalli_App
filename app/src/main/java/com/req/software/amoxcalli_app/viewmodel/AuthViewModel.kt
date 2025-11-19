package com.req.software.amoxcalli_app.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.req.software.amoxcalli_app.data.dto.UserRegistrationRequest
import com.req.software.amoxcalli_app.data.dto.UserResponse
import com.req.software.amoxcalli_app.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val authService = RetrofitClient.authService

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _currentUser = MutableStateFlow<UserResponse?>(null)
    val currentUser: StateFlow<UserResponse?> = _currentUser

    init {
        // No cargar automáticamente usuario para forzar login manual cada vez
        // Si deseas permitir sesiones persistentes, implementa SharedPreferences o DataStore
    }

    fun getGoogleSignInClient(activity: Activity, webClientId: String): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(activity, gso)
    }

    fun signOut(googleSignInClient: GoogleSignInClient?) {
        viewModelScope.launch {
            try {
                // Actualizar el estado para evitar recargas
                _currentUser.value = null
                _authState.value = AuthState.Idle

                // Cerrar sesión de Google Sign-In para forzar selección de cuenta en próximo login
                googleSignInClient?.signOut()

                // Cerrar sesión de Firebase
                auth.signOut()
            } catch (_: Exception) {
                // Aún así mantener la sesión cerrada
                _currentUser.value = null
                _authState.value = AuthState.Idle
                auth.signOut()
            }
        }
    }

    /**
     * Sign in with Google
     * 1. Authenticate with Firebase to get firebase_uid
     * 2. Send firebase_uid to backend API to sync user data
     */
    fun signInWithGoogle(account: GoogleSignInAccount) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading

                // Get ID token from Google Sign-In
                val idToken = account.idToken
                if (idToken == null) {
                    _authState.value = AuthState.Error("No se pudo obtener el token de Google")
                    return@launch
                }

                // Step 1: Authenticate with Firebase to get firebase_uid
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val firebaseResult = auth.signInWithCredential(credential).await()
                val firebaseUser = firebaseResult.user

                if (firebaseUser == null) {
                    _authState.value = AuthState.Error("Error al autenticar con Firebase")
                    return@launch
                }

                // Step 2: Send firebase_uid to backend API
                val registrationRequest = UserRegistrationRequest(
                    firebaseUid = firebaseUser.uid,
                    email = account.email,
                    displayName = account.displayName,
                    avatarUrl = account.photoUrl?.toString()
                )

                // Call backend API to register/sync user
                val response = authService.register(registrationRequest)

                if (response.success && response.data != null) {
                    _currentUser.value = response.data
                    _authState.value = AuthState.Success(response.data)
                } else {
                    _authState.value = AuthState.Error(response.message ?: "Error al sincronizar con el servidor")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Error de conexión con el servidor")
            }
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: UserResponse?) : AuthState()
    data class Error(val message: String) : AuthState()
}

