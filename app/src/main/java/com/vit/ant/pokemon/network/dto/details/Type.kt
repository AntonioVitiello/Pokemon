package com.vit.ant.pokemon.network.dto.details


import com.fasterxml.jackson.annotation.JsonProperty

data class Type(
    @JsonProperty("slot")
    val slot: Int?,
    @JsonProperty("type")
    val type: TypeX?
)