package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.themes

import java.time.ZonedDateTime

data class ThemeEntity(
    val id: Int,
    val name: String,
    val startedAt: ZonedDateTime? = null,
    val endedAt: ZonedDateTime? = null,
    val subThemes: List<ThemeEntity> = emptyList(),
)
