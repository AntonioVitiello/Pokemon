package com.vit.ant.pokemon.network.dto.details


import com.fasterxml.jackson.annotation.JsonProperty

data class Ability(
    @JsonProperty("ability")
    val ability: AbilityX?,
    @JsonProperty("is_hidden")
    val isHidden: Boolean?,
    @JsonProperty("slot")
    val slot: Int?
)