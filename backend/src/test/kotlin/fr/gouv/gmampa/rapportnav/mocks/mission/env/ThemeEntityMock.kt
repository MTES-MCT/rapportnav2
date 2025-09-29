package fr.gouv.gmampa.rapportnav.mocks.mission.env

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.themes.ThemeEntity
import java.time.ZonedDateTime

object ThemeEntityMock {
    fun create(
        id: Int = 112,
        name: String = "Pêche loisir (autre que PAP)",
        startedAt: ZonedDateTime? = null,
        endedAt: ZonedDateTime? = null,
        subThemes: List<ThemeEntity> = listOf(ThemeEntity(
            id = 113,
            name = "Peche embarquée"
        )),
    ): ThemeEntity {
        return ThemeEntity(
            id = id,
            name = name,
            startedAt = startedAt,
            endedAt= endedAt,
            subThemes = subThemes,
        )
    }
}
