package com.vit.ant.pokemon.network.dto


import com.fasterxml.jackson.annotation.JsonProperty

data class PokemonResponse(
    @JsonProperty("count")
    val count: Int?,
    @JsonProperty("next")
    val next: String?,
    @JsonProperty("previous")
    val previous: Any?,
    @JsonProperty("results")
    val results: List<Result>?
)