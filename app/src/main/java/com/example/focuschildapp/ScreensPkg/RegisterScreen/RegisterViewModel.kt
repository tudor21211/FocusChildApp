package com.example.focuschildapp.ScreensPkg.RegisterScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.focuschildapp.Firebase.domain.AuthRepository
import com.example.focuschildapp.Firebase.domain.Response
import com.example.focuschildapp.Firebase.domain.SendEmailVerificationResponse
import com.example.focuschildapp.Firebase.domain.SignUpResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {
    private var signUpResponse by mutableStateOf<SignUpResponse>(Response.Success(false))

    fun signUpWithEmailAndPassword(email: String, password: String) = viewModelScope.launch {
        signUpResponse = Response.Loading
        signUpResponse = repo.firebaseSignUpWithEmailAndPassword(email, password)
    }

}