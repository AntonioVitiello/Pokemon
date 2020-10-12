package com.vit.ant.pokemon.network.dto.details


import com.fasterxml.jackson.annotation.JsonProperty

data class PokemonDetailsResponse(
    @JsonProperty("abilities")
    val abilities: List<Ability>?,
    @JsonProperty("base_experience")
    val baseExperience: Int?,
    @JsonProperty("forms")
    val forms: List<Form>?,
    @JsonProperty("game_indices")
    val gameIndices: List<GameIndice>?,
    @JsonProperty("height")
    val height: Int?,
    @JsonProperty("held_items")
    val heldItems: List<Any>?,
    @JsonProperty("id")
    val id: Int?,
    @JsonProperty("is_default")
    val isDefault: Boolean?,
    @JsonProperty("location_area_encounters")
    val locationAreaEncounters: String?,
    @JsonProperty("moves")
    val moves: List<Move>?,
    @JsonProperty("name")
    val name: String?,
    @JsonProperty("order")
    val order: Int?,
    @JsonProperty("species")
    val species: Species?,
    @JsonProperty("sprites")
    val sprites: Sprites?,
    @JsonProperty("stats")
    val stats: List<Stat>?,
    @JsonProperty("types")
    val types: List<Type>?,
    @JsonProperty("weight")
    val weight: Int?
)