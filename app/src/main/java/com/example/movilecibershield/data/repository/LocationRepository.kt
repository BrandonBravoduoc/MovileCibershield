package com.example.movilecibershield.data.repository

import com.example.movilecibershield.data.model.user.CommuneCombo
import com.example.movilecibershield.data.model.user.RegionCombo
import com.example.movilecibershield.data.remote.api.product.LocationApiService

class LocationRepository(
    private val api: LocationApiService
) {

    suspend fun getRegions(): RepoResult<List<RegionCombo>> = try {
        RepoResult(data = api.getRegions())
    } catch (e: Exception) {
        RepoResult(error = "Error al cargar regiones")
    }

    suspend fun getCommunes(regionId: Long): RepoResult<List<CommuneCombo>> = try {
        RepoResult(data = api.getCommunes(regionId))
    } catch (e: Exception) {
        RepoResult(error = "Error al cargar comunas")
    }
}
