package com.example.focuschildapp.ScreensPkg.RegisterScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.focuschildapp.Firebase.domain.AuthRepository
import com.example.focuschildapp.Firebase.domain.Response
import com.example.focuschildapp.Firebase.domain.SendEmailVerificationResponse
import com.example.focuschildapp.Firebase.domain.SignUpResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {

    private var _signUpResponse: MutableLiveData<SignUpResponse> = MutableLiveData()
    val signUpResponse: MutableLiveData<SignUpResponse>
        get() = _signUpResponse

    fun signUpWithEmailAndPassword(email: String, password: String): Job {
        signUpResponse.value = Response.Loading

        return viewModelScope.launch {
            try {
                val result = repo.firebaseSignUpWithEmailAndPassword(email, password)
                signUpResponse.value = result
            } catch (e: Exception) {
                signUpResponse.value = Response.Failure(e)
            }
        }
    }

}

