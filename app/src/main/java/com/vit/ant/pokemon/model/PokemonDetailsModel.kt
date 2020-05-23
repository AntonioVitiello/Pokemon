package com.vit.ant.pokemon.model

import android.os.Parcelable
import com.vit.ant.pokemon.PokemonApplication.Companion.IMAGE_URL
import kotlinx.android.parcel.Parcelize

/**
 * Created by Vitiello Antonio
 * eg: https://pokeapi.co/api/v2/pokemon/1/
 */
@Parcelize
data class PokemonDetailsModel(
    val id: Int,
    var name: String? = null,
    var height: Double? = null,
    var weight: Double? = null,
    var types: List<Type>? = null,
    var stats: List<Stats>? = null
) : Parcelable {

    val imageUrl: String
        get() = "$IMAGE_URL/$id.png"

    @Parcelize
    data class Type(
        var name: String? = null,
        var url: String? = null
    ) : Parcelable

    @Parcelize
    data class Stats(
        var base: Int? = null,
        var effort: Int? = null,
        var name: String? = null,
        var url: String? = null
    ) : Parcelable

}