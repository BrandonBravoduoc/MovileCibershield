package com.example.movilecibershield.data.remote.api.product

import com.example.movilecibershield.data.model.user.CommuneCombo
import com.example.movilecibershield.data.model.user.RegionCombo
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApiService {

    @GET("api/v1/locations/regions")
    suspend fun getRegions(): List<RegionCombo>

    @GET("api/v1/locations/communes")
    suspend fun getCommunes(
        @Query("regionId") regionId: Long
    ): List<CommuneCombo>
}
