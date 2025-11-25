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

    private val _authToken = MutableStateFlow<String?>(null)
    val authToken: StateFlow<String?> = _authToken

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
                _authToken.value = null

                // Cerrar sesión de Google Sign-In para forzar selección de cuenta en próximo login
                googleSignInClient?.signOut()

                // Cerrar sesión de Firebase
                auth.signOut()
            } catch (_: Exception) {
                // Aún así mantener la sesión cerrada
                _currentUser.value = null
                _authState.value = AuthState.Idle
                _authToken.value = null
                auth.signOut()
            }
        }
    }

    /**
     * Sign in with Google
     * 1. Authenticate with Firebase using Google credentials
     * 2. Send Google ID token to backend API (handles both new and existing users)
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

                // Step 2: Send Firebase UID to backend API for login/registration
                val loginRequest = UserRegistrationRequest(
                    firebaseUid = firebaseUser.uid,
                    email = account.email,
                    displayName = account.displayName,
                    avatarUrl = account.photoUrl?.toString()
                )

                // Call backend API to login (handles both new and existing users)
                val response = authService.login(loginRequest)

                if (response.success && response.data != null) {
                    _currentUser.value = response.data

                    // Get Firebase ID token for subsequent API calls
                    try {
                        val token = firebaseUser.getIdToken(false).await()
                        _authToken.value = "Bearer ${token.token}"
                    } catch (e: Exception) {
                        // Continue even if token fetch fails
                        _authToken.value = null
                    }

                    _authState.value = AuthState.Success(response.data)
                } else {
                    _authState.value = AuthState.Error(response.message ?: "Error al sincronizar con el servidor")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Error de conexión con el servidor")
            }
        }
    }

    /**
     * Load user stats using UserStatsViewModel
     * Should be called after successful login
     */
    fun loadUserStats(userStatsViewModel: UserStatsViewModel) {
        _authToken.value?.let { token ->
            userStatsViewModel.loadUserStats(token)
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: UserResponse?) : AuthState()
    data class Error(val message: String) : AuthState()
}

