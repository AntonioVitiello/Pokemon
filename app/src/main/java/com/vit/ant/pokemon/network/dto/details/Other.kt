package com.vit.ant.pokemon.network.dto.details


import com.fasterxml.jackson.annotation.JsonProperty

data class Other(
    @JsonProperty("dream_world")
    val dreamWorld: DreamWorld?,
    @JsonProperty("official-artwork")
    val officialArtwork: OfficialArtwork?
)