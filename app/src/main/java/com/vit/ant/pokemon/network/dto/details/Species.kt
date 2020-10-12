package com.vit.ant.pokemon.network.dto.details


import com.fasterxml.jackson.annotation.JsonProperty

data class Species(
    @JsonProperty("name")
    val name: String?,
    @JsonProperty("url")
    val url: String?
)