package com.vit.ant.pokemon.network.dto.list


import com.fasterxml.jackson.annotation.JsonProperty

data class Result(
    @JsonProperty("name")
    val name: String?,
    @JsonProperty("url")
    val url: String?
)