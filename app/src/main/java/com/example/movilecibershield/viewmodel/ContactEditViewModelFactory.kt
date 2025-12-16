package com.example.movilecibershield.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movilecibershield.data.repository.LocationRepository
import com.example.movilecibershield.data.repository.UserRepository

class ContactEditViewModelFactory(
    private val locationRepository: LocationRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactEditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContactEditViewModel(
                locationRepository = locationRepository,
                userRepository = userRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
