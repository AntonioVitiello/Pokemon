package com.vit.ant.pokemon.di

import android.content.Context
import com.vit.ant.pokemon.BuildConfig
import com.vit.ant.pokemon.network.ApiService
import com.vit.ant.pokemon.repository.PokemonRepository
import com.vit.ant.pokemon.viewmodel.DetailPokemonViewModel
import com.vit.ant.pokemon.viewmodel.PokemonListViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Created by Antonio Vitiello on 05/01/2021.
 */
@Module
@InstallIn(ActivityComponent::class)
object PokemonModule {

    private const val ENDPOINT = "https://pokeapi.co/api/v2/"
    private val httpClient = OkHttpClient.Builder().apply {
        writeTimeout(60, TimeUnit.SECONDS)
        readTimeout(60, TimeUnit.SECONDS)
        connectTimeout(60, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            addNetworkInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
    }.build()


    @Provides
    fun provideApiService(): ApiService {

        return Retrofit.Builder()
            .baseUrl(ENDPOINT)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create())
            .callbackExecutor(Executors.newCachedThreadPool())
            .client(httpClient)
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    fun providePokemonRepository(apiService: ApiService): PokemonRepository = PokemonRepository(apiService)

}