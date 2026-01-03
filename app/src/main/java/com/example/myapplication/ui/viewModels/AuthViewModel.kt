package com.example.myapplication.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.User
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.AuthResult
import com.example.myapplication.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val user: User) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val authState: StateFlow<AuthUiState> = _authState.asStateFlow()

    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            authRepository.getAuthStateFlow().collect { firebaseUser ->
                _isUserLoggedIn.value = firebaseUser != null
            }
        }
    }

    fun signUp(email: String, password: String, displayName: String) {
        if (email.isBlank() || password.isBlank() || displayName.isBlank()) {
            _authState.value = AuthUiState.Error("All fields are required")
            return
        }

        if (password.length < 6) {
            _authState.value = AuthUiState.Error("Password must be at least 6 characters")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthUiState.Loading

            when (val result = authRepository.signUp(email, password, displayName)) {
                is AuthResult.Success -> {
                    // Save user to Firestore
                    userRepository.saveUser(result.user)
                    _authState.value = AuthUiState.Success(result.user)
                }
                is AuthResult.Error -> {
                    _authState.value = AuthUiState.Error(result.message)
                }
            }
        }
    }

    fun signIn(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthUiState.Error("Email and password are required")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthUiState.Loading

            when (val result = authRepository.signIn(email, password)) {
                is AuthResult.Success -> {
                    _authState.value = AuthUiState.Success(result.user)
                }
                is AuthResult.Error -> {
                    _authState.value = AuthUiState.Error(result.message)
                }
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
        _authState.value = AuthUiState.Idle
    }

    fun resetAuthState() {
        _authState.value = AuthUiState.Idle
    }

    fun getCurrentUserId(): String? {
        return authRepository.getCurrentUserId()
    }

    fun isUserLoggedIn(): Boolean {
        return authRepository.isUserLoggedIn()
    }
}
