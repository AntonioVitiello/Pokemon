package com.vit.ant.pokemon.network.dto.list


import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class PokemonResponse(
    @JsonIgnore
    @JsonProperty("count")
    val count: Int?,
    @JsonIgnore
    @JsonProperty("next")
    val next: String?,
    @JsonIgnore
    @JsonProperty("previous")
    val previous: Any?,
    @JsonProperty("results")
    val results: List<Result>?
)