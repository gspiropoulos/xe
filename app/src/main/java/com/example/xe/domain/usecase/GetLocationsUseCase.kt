package com.example.xe.domain.usecase

import com.example.xe.common.Constants
import com.example.xe.common.Resource
import com.example.xe.domain.AutoCompleteRepository
import com.example.xe.domain.model.ResultItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetLocationsUseCase @Inject constructor(
    private val repository: AutoCompleteRepository
) {
    operator fun invoke(prefix: String): Flow<Resource<List<ResultItem>>> = flow {
        try {
            val locations = repository.getLocations(prefix)
            emit(Resource.Success(locations))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: Constants.httpError))
        } catch (e: IOException) {
            emit(Resource.Error(Constants.ioError))
        }
    }
}