package com.vit.ant.pokemon.network.dto.details


import com.fasterxml.jackson.annotation.JsonProperty

data class RedBlue(
    @JsonProperty("back_default")
    val backDefault: String?,
    @JsonProperty("back_gray")
    val backGray: String?,
    @JsonProperty("front_default")
    val frontDefault: String?,
    @JsonProperty("front_gray")
    val frontGray: String?
)