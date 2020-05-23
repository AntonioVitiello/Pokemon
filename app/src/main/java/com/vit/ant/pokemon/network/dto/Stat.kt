package com.vit.ant.pokemon.network.dto


import com.fasterxml.jackson.annotation.JsonProperty

data class Stat(
    @JsonProperty("base_stat")
    val baseStat: Int?,
    @JsonProperty("effort")
    val effort: Int?,
    @JsonProperty("stat")
    val stat: StatX?
)