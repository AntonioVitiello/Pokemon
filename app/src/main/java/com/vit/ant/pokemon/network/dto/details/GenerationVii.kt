package com.vit.ant.pokemon.network.dto.details


import com.fasterxml.jackson.annotation.JsonProperty

data class GenerationVii(
    @JsonProperty("icons")
    val icons: Icons?,
    @JsonProperty("ultra-sun-ultra-moon")
    val ultraSunUltraMoon: UltraSunUltraMoon?
)