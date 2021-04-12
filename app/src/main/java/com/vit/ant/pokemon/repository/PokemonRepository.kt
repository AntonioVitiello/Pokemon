package com.vit.ant.pokemon.repository

import com.vit.ant.pokemon.network.ApiService
import com.vit.ant.pokemon.network.dto.details.PokemonDetailsResponse
import com.vit.ant.pokemon.network.dto.list.PokemonResponse
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Vitiello Antonio
 */

class PokemonRepository @Inject constructor(private val apiService: ApiService) {

    fun getPokemons(offset: Int, limit: Int): Single<PokemonResponse> {
        return apiService.getPokemons(offset, limit)
    }

    fun getPokemonDetails(id: Int): Single<PokemonDetailsResponse> {
        return apiService.getPokemonDetails(id)
    }

}