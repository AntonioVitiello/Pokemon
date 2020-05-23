package com.vit.ant.pokemon.network

import com.vit.ant.pokemon.network.dto.PokemonDetailsResponse
import com.vit.ant.pokemon.network.dto.PokemonResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Vitiello Antonio
 */


interface ApiService {

    @GET("pokemon")
    fun getPokemons(@Query("offset") offset: Int, @Query("limit") limit: Int): Single<PokemonResponse>

    @GET("pokemon/{id}")
    fun getPokemonDetails(@Path("id") id: Int): Single<PokemonDetailsResponse>

}