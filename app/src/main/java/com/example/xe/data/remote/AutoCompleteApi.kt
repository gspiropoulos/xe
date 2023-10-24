package com.example.xe.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface AutoCompleteApi {

    @GET("{input}")
    suspend fun getLocations(@Query("input") input: String): List<ResultsDtoItem>
}
