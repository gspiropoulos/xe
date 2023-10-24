package com.example.xe.domain

import com.example.xe.domain.model.ResultItem

interface AutoCompleteRepository {
    suspend fun getLocations(query : String): List<ResultItem>
}