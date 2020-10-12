package com.vit.ant.pokemon.network.dto.details


import com.fasterxml.jackson.annotation.JsonProperty

data class GenerationIi(
    @JsonProperty("crystal")
    val crystal: Crystal?,
    @JsonProperty("gold")
    val gold: Gold?,
    @JsonProperty("silver")
    val silver: Silver?
)