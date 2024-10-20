package com.example.sprintm6.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Delete
import com.example.sprintm6.model.User
import com.example.sprintm6.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepo: UserRepository
): ViewModel() {

    private val _isLoading: MutableStateFlow<Boolean> by lazy {
        MutableStateFlow<Boolean>(false)
    }

    val isLoading: Flow<Boolean> get() = _isLoading

    val users: Flow<List<User>> by lazy {
        userRepo.getAllUser()
    }
    fun addUser(){
        if(!_isLoading.value) {
            viewModelScope.launch(Dispatchers.IO){
                _isLoading.value = true
                userRepo.getNewUser()
                _isLoading.value = false
            }
        }
    }
    fun deleteUser(toDelete: User){
        viewModelScope.launch (Dispatchers.IO) {
            userRepo.deleteUser(toDelete)
        }
    }

}