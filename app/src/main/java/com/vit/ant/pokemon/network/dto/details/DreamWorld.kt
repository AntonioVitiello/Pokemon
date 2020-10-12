package com.vit.ant.pokemon.network.dto.details


import com.fasterxml.jackson.annotation.JsonProperty

data class DreamWorld(
    @JsonProperty("front_default")
    val frontDefault: String?,
    @JsonProperty("front_female")
    val frontFemale: Any?
)