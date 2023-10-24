package com.example.xe.di

import com.example.xe.common.Constants
import com.example.xe.data.remote.AutoCompleteApi
import com.example.xe.data.repository.AutoCompleteRepositoryImpl
import com.example.xe.domain.AutoCompleteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideApi(): AutoCompleteApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AutoCompleteApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(api: AutoCompleteApi): AutoCompleteRepository {
        return AutoCompleteRepositoryImpl(api)
    }
}