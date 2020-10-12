package com.vit.ant.pokemon.network.dto.details


import com.fasterxml.jackson.annotation.JsonProperty

data class VersionGroupDetail(
    @JsonProperty("level_learned_at")
    val levelLearnedAt: Int?,
    @JsonProperty("move_learn_method")
    val moveLearnMethod: MoveLearnMethod?,
    @JsonProperty("version_group")
    val versionGroup: VersionGroup?
)