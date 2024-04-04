package com.example.focuschildapp.com.example.focuschildapp.ScreensPkg.MainPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.focuschildapp.Firebase.domain.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainPageViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {

    init {
        getAuthState()
    }

    fun getAuthState() = repo.getAuthState(viewModelScope)

    fun getUserEmail() = repo.currentUser?.email
}