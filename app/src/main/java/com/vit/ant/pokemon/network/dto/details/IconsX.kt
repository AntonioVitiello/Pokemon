package com.vit.ant.pokemon.network.dto.details


import com.fasterxml.jackson.annotation.JsonProperty

data class IconsX(
    @JsonProperty("front_default")
    val frontDefault: String?,
    @JsonProperty("front_female")
    val frontFemale: Any?
)