package com.example.movilecibershield.data.repository

import com.example.movilecibershield.data.model.user.CommuneCombo
import com.example.movilecibershield.data.model.user.RegionCombo
import com.example.movilecibershield.data.remote.api.product.LocationApiService
import com.example.movilecibershield.data.utils.extractErrorMessage
import retrofit2.HttpException
import java.io.IOException

class LocationRepository(
    private val api: LocationApiService
) {

    suspend fun getRegions(): RepoResult<List<RegionCombo>> {
        return try {
            val response = api.getRegions()
            RepoResult(data = response)
        } catch (e: HttpException) {
            RepoResult(error = extractErrorMessage(e))
        } catch (e: IOException) {
            RepoResult(error = "Sin conexión a internet")
        } catch (e: Exception) {
            RepoResult(error = "Error inesperado al cargar regiones")
        }
    }

    suspend fun getCommunes(regionId: Long): RepoResult<List<CommuneCombo>> {
        return try {
            val response = api.getCommunes(regionId)
            RepoResult(data = response)
        } catch (e: HttpException) {
            RepoResult(error = extractErrorMessage(e))
        } catch (e: IOException) {
            RepoResult(error = "Sin conexión a internet")
        } catch (e: Exception) {
            RepoResult(error = "Error inesperado al cargar comunas")
        }
    }
}
