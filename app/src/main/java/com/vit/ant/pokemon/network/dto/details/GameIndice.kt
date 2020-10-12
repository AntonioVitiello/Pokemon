package com.vit.ant.pokemon.network.dto.details


import com.fasterxml.jackson.annotation.JsonProperty

data class GameIndice(
    @JsonProperty("game_index")
    val gameIndex: Int?,
    @JsonProperty("version")
    val version: Version?
)