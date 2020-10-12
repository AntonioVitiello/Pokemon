package com.vit.ant.pokemon.network.dto.details


import com.fasterxml.jackson.annotation.JsonProperty

data class OfficialArtwork(
    @JsonProperty("front_default")
    val frontDefault: String?
)