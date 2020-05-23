package com.vit.ant.pokemon.model

import com.vit.ant.pokemon.PokemonApplication.Companion.IMAGE_URL

/**
 * Created by Vitiello Antonio
 * eg: https://pokeres.bastionbot.org/images/pokemon/1.png
 */
data class PokemonModel(val name: String, val id: Int){

    val imageUrl: String
        get() = "$IMAGE_URL/$id.png"

}