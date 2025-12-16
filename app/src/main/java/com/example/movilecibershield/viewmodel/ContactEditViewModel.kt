package com.example.movilecibershield.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movilecibershield.data.model.user.CommuneCombo
import com.example.movilecibershield.data.model.user.ContactUpdateWithAddress
import com.example.movilecibershield.data.model.user.RegionCombo
import com.example.movilecibershield.data.repository.LocationRepository
import com.example.movilecibershield.data.repository.UserRepository
import kotlinx.coroutines.launch

class ContactEditViewModel(
    private val locationRepository: LocationRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    var regions by mutableStateOf<List<RegionCombo>>(emptyList())
        private set

    var communes by mutableStateOf<List<CommuneCombo>>(emptyList())
        private set

    var selectedRegion by mutableStateOf<RegionCombo?>(null)
    var selectedCommune by mutableStateOf<CommuneCombo?>(null)
    var updateStatus by mutableStateOf<String?>(null)
        private set

    fun loadRegions() {
        viewModelScope.launch {
            locationRepository.getRegions().data?.let {
                regions = it
            }
        }
    }

    fun loadCommunes(regionId: Long) {
        viewModelScope.launch {
            locationRepository.getCommunes(regionId).data?.let {
                communes = it
            }
        }
    }

    fun updateContact(dto: ContactUpdateWithAddress) {
        viewModelScope.launch {
            updateStatus = "LOADING"
            val result = userRepository.updateContact(dto)
            if (result.data != null) {
                updateStatus = "SUCCESS"
            } else {
                updateStatus = "ERROR"
            }
        }
    }

    fun clearStatus() {
        updateStatus = null
    }
}
