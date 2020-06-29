package com.vit.ant.pokemon.model

import android.os.Parcelable
import com.vit.ant.pokemon.PokemonApplication.Companion.IMAGE_URL
import kotlinx.android.parcel.Parcelize


/**
 * Created by Vitiello Antonio
 * eg: https://pokeres.bastionbot.org/images/pokemon/1.png
 */
@Parcelize
data class PokemonModel(val name: String, val id: Int) : Parcelable {

    val imageUrl: String
        get() = "$IMAGE_URL/$id.png"


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PokemonModel

        return id == other.id && name == other.name
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + id
        return result
    }

}