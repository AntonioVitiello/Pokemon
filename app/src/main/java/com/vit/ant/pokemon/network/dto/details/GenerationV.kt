package com.vit.ant.pokemon.network.dto.details


import com.fasterxml.jackson.annotation.JsonProperty

data class GenerationV(
    @JsonProperty("black-white")
    val blackWhite: BlackWhite?
)