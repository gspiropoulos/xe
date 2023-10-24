package com.example.xe.data.repository

import com.example.xe.data.remote.AutoCompleteApi
import com.example.xe.data.remote.toResultItem
import com.example.xe.domain.AutoCompleteRepository
import com.example.xe.domain.model.ResultItem
import javax.inject.Inject

class AutoCompleteRepositoryImpl @Inject constructor(
    private val api: AutoCompleteApi
) : AutoCompleteRepository {
    override suspend fun getLocations(query: String): List<ResultItem> {
        return api.getLocations(query).map { it.toResultItem() }
    }
}