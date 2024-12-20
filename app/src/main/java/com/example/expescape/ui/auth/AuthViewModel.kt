package com.example.expescape.ui.auth


import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.expescape.data.repository.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import com.example.expescape.util.Result
import com.example.expescape.util.BaseViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    private val googleSignInClient: GoogleSignInClient
) : BaseViewModel() {
    private val _authState = MutableLiveData<Result<Unit>>()
    val authState: LiveData<Result<Unit>> = _authState

    private val _navigationEvent = MutableLiveData<NavigationEvent>()
    val navigationEvent: LiveData<NavigationEvent> = _navigationEvent

    private val _googleSignInIntent = MutableLiveData<Intent>()
    val googleSignInIntent: LiveData<Intent> = _googleSignInIntent

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        if (authRepository.isUserSignedIn()) {
            _navigationEvent.value = NavigationEvent.NavigateToDashboard
        }
    }

    // Navigation click handlers
    fun onGetStartedClick() {
        _navigationEvent.value = NavigationEvent.NavigateToLoginSignup
    }

    fun onLoginClick() {
        _navigationEvent.value = NavigationEvent.NavigateToLogin
    }

    fun onSignupClick() {
        _navigationEvent.value = NavigationEvent.NavigateToSignup
    }

    // Authentication handlers
    fun performLogin(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = Result.Loading
            val result = authRepository.signIn(email, password)
            _authState.value = result
            if (result is Result.Success) {
                _navigationEvent.value = NavigationEvent.NavigateToDashboard
            }
        }
    }

    fun performSignup(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = Result.Loading
            val result = authRepository.signUp(email, password)
            _authState.value = result
            if (result is Result.Success) {
                _navigationEvent.value = NavigationEvent.NavigateToDashboard
            }
        }
    }


    fun initiateGoogleSignIn() {
        _googleSignInIntent.value = googleSignInClient.signInIntent
    }

    fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        viewModelScope.launch {
            try {
                val account = task.getResult(ApiException::class.java)
                account.idToken?.let { token ->
                    _authState.value = Result.Loading
                    val result = authRepository.signInWithGoogle(token)
                    _authState.value = result
                    if (result is Result.Success) {
                        _navigationEvent.value = NavigationEvent.NavigateToDashboard
                    }
                }
            } catch (e: ApiException) {
                _authState.value = Result.Error(e.message ?: "Google sign in failed")
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
        _navigationEvent.value = NavigationEvent.NavigateToLogin
    }

    sealed class NavigationEvent {
        object NavigateToLoginSignup : NavigationEvent()
        object NavigateToLogin : NavigationEvent()
        object NavigateToSignup : NavigationEvent()
        object NavigateToDashboard : NavigationEvent()
    }
}