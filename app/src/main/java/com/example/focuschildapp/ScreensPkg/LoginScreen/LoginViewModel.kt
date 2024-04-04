package com.example.focuschildapp.com.example.focuschildapp.ScreensPkg.LoginScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.focuschildapp.Firebase.domain.AuthRepository
import com.example.focuschildapp.Firebase.domain.Response
import com.example.focuschildapp.Firebase.domain.SignInResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {

    private var _signInResponse: MutableLiveData<SignInResponse> = MutableLiveData()
    val signInResponse: MutableLiveData<SignInResponse>
        get() = _signInResponse

    fun signInWithEmailAndPassword(email: String, password: String): Job {
        signInResponse.value = Response.Loading

        return viewModelScope.launch {
            try {
                val result = repo.firebaseSignInWithEmailAndPassword(email, password)
                signInResponse.value = result
            } catch (e: Exception) {
                signInResponse.value = Response.Failure(e)
            }
        }
    }
}