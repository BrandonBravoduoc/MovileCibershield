package com.example.movilecibershield.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movilecibershield.data.model.user.*
import com.example.movilecibershield.data.repository.LocationRepository
import com.example.movilecibershield.data.repository.UserRepository
import com.example.movilecibershield.data.utils.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ContactEditViewModel(
    private val locationRepository: LocationRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    var regions = emptyList<RegionCombo>()
        private set

    var communes = emptyList<CommuneCombo>()
        private set

    var selectedRegion: RegionCombo? = null
    var selectedCommune: CommuneCombo? = null

    var updateStatus: String? = null
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun loadRegions() {
        viewModelScope.launch {
            val result = locationRepository.getRegions()
            result.data?.let { regions = it }
            result.error?.let { _uiEvent.send(UiEvent.ShowSnackbar(it)) }
        }
    }

    fun loadCommunes(regionId: Long) {
        viewModelScope.launch {
            val result = locationRepository.getCommunes(regionId)
            result.data?.let { communes = it }
            result.error?.let { _uiEvent.send(UiEvent.ShowSnackbar(it)) }
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
                _uiEvent.send(UiEvent.ShowSnackbar(result.error ?: "Error al actualizar contacto"))
            }
        }
    }

    fun clearStatus() {
        updateStatus = null
    }
}
