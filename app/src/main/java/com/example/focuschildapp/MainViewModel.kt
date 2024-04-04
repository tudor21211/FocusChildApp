package com.example.focuschildapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.focuschildapp.Firebase.domain.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {
    init {
        getAuthState()
    }

    fun getAuthState() = repo.getAuthState(viewModelScope)

    //TODO: Delete
    val isEmailVerified get() = repo.currentUser?.isEmailVerified ?: false

}