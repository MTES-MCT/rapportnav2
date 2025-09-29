package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.tags

import java.time.ZonedDateTime

data class TagEntity(
    val id: Int,
    val name: String,
    val startedAt: ZonedDateTime? = null,
    val endedAt: ZonedDateTime? = null,
    val subTags: List<TagEntity>,
)
