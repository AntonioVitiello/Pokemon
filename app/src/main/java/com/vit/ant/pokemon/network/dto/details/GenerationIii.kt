package com.vit.ant.pokemon.network.dto.details


import com.fasterxml.jackson.annotation.JsonProperty

data class GenerationIii(
    @JsonProperty("emerald")
    val emerald: Emerald?,
    @JsonProperty("firered-leafgreen")
    val fireredLeafgreen: FireredLeafgreen?,
    @JsonProperty("ruby-sapphire")
    val rubySapphire: RubySapphire?
)