package com.vit.ant.pokemon.network.dto.details


import com.fasterxml.jackson.annotation.JsonProperty

data class GenerationI(
    @JsonProperty("red-blue")
    val redBlue: RedBlue?,
    @JsonProperty("yellow")
    val yellow: Yellow?
)