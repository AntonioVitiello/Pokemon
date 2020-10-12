package com.vit.ant.pokemon.network.dto.details


import com.fasterxml.jackson.annotation.JsonProperty

data class Move(
    @JsonProperty("move")
    val move: MoveX?,
    @JsonProperty("version_group_details")
    val versionGroupDetails: List<VersionGroupDetail>?
)