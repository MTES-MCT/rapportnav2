package fr.gouv.dgampa.rapportnav.infrastructure.api.adapters.outputs

import fr.gouv.dgampa.rapportnav.domain.entities.crew.CrewEntity
import java.time.ZonedDateTime

data class CrewDataOutput(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val isArchived: Boolean,
) {
    companion object {
        fun fromCrew(crew: CrewEntity): CrewDataOutput {
            requireNotNull(crew.id) {
                "a crew must have an id"
            }
            return CrewDataOutput(
                id = crew.id,
                firstName = crew.firstName,
                lastName = crew.lastName,
                isArchived = crew.isArchived,
            )
        }
    }
}
