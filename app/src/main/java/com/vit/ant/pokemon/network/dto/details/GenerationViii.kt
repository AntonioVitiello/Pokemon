package com.vit.ant.pokemon.network.dto.details


import com.fasterxml.jackson.annotation.JsonProperty

data class GenerationViii(
    @JsonProperty("icons")
    val icons: IconsX?
)