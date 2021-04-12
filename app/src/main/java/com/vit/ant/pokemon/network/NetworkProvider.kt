package com.vit.ant.pokemon.network

import android.util.Log
import com.vit.ant.pokemon.BuildConfig
import com.vit.ant.pokemon.network.dto.list.PokemonResponse
import com.vit.ant.pokemon.network.dto.details.PokemonDetailsResponse
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Created by Vitiello Antonio
 */
class NetworkProvider {
    companion object {
        const val TAG = "NetworkProvider"
        const val ENDPOINT = "https://pokeapi.co/api/v2/"

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

        private var apiService = Retrofit.Builder()
            .baseUrl(ENDPOINT)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create())
            .callbackExecutor(Executors.newCachedThreadPool())
            .client(httpClient)
            .build()
            .create(ApiService::class.java)
    }

    fun getPokemons(offset: Int, limit: Int): Single<PokemonResponse> {
        return apiService.getPokemons(offset, limit)
    }

    fun getPokemonDetails(id: Int): Single<PokemonDetailsResponse> {
        return apiService.getPokemonDetails(id)
    }

}