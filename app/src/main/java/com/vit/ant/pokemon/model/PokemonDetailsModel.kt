package com.vit.ant.pokemon.model

import com.vit.ant.pokemon.PokemonApplication.Companion.IMAGE_URL

/**
 * Created by Vitiello Antonio
 * eg: https://pokeapi.co/api/v2/pokemon/1/
 */
data class PokemonDetailsModel(val id: Int) {

    var name: String? = null
    var height: Double? = null
    var weight: Double? = null
    var types: List<Type>? = null
    var stats: List<Stats>? = null

    val imageUrl: String
        get() = "$IMAGE_URL/$id.png"

    class Type {
        var name: String? = null
        var url: String? = null
    }

    class Stats {
        var base: Int? = null
        var effort: Int? = null
        var name: String? = null
        var url: String? = null
    }

}