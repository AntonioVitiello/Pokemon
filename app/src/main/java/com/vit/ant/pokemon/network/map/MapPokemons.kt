package com.vit.ant.pokemon.network.map

import com.vit.ant.pokemon.model.PokemonDetailsModel
import com.vit.ant.pokemon.model.PokemonModel
import com.vit.ant.pokemon.network.dto.PokemonDetailsResponse
import com.vit.ant.pokemon.network.dto.PokemonResponse
import com.vit.ant.pokemon.tools.Utils.urlToId

/**
 * Created by Vitiello Antonio
 */
fun mapPokemon(pokemonResponse: PokemonResponse): List<PokemonModel> {
    return arrayListOf<PokemonModel>().apply {
        pokemonResponse.results?.forEach { result ->
            if (result.name != null && result.url != null) {
                add(PokemonModel(result.name.capitalize(), urlToId(result.url)))
            }
        }
    }
}

fun mapPokemonDetails(pokemonResponse: PokemonDetailsResponse): PokemonDetailsModel {
    return PokemonDetailsModel(pokemonResponse.id!!).apply {
        name = pokemonResponse.name?.capitalize()
        height = pokemonResponse.height?.let { it / 10.0 } //The height of this Pokémon in decimetres.
        weight = pokemonResponse.weight?.let { it / 10.0 } //The weight of this Pokémon in hectograms.
        types = pokemonResponse.types?.map {
            PokemonDetailsModel.Type().apply {
                name = it.type?.name
                url = it.type?.url
            }
        }
        stats = pokemonResponse.stats?.map {
            PokemonDetailsModel.Stats().apply {
                base = it.baseStat
                effort = it.effort
                name = it.stat?.name
                url = it.stat?.url
            }
        }?.sortedWith(Comparator { o1, o2 ->
            val length1 = o1.name?.length ?: 0
            val length2 = o2.name?.length ?: 0
            return@Comparator -length1.compareTo(length2)
        })
    }
}

