package com.vit.ant.pokemon.repository

import com.vit.ant.pokemon.network.NetworkProvider
import com.vit.ant.pokemon.network.dto.PokemonDetailsResponse
import com.vit.ant.pokemon.network.dto.PokemonResponse
import io.reactivex.Single

/**
 * Created by Vitiello Antonio
 */

object PokemonRepository {
    private var networkProvider = NetworkProvider()

    fun getPokemons(offset: Int, limit: Int): Single<PokemonResponse> {
        return networkProvider.getPokemons(offset, limit)
    }

    fun getPokemonDetails(id: Int): Single<PokemonDetailsResponse> {
        return networkProvider.getPokemonDetails(id)
    }

}