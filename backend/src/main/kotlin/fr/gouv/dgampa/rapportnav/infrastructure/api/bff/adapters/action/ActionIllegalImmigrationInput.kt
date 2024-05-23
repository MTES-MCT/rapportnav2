package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionIllegalImmigrationEntity
import java.time.ZonedDateTime
import java.util.*

class ActionIllegalImmigrationInput(
    var id: UUID? = null,
    var missionId: Int,
    var startDateTimeUtc: ZonedDateTime,
    var endDateTimeUtc: ZonedDateTime,
    var observations: String? = null,
    var nbOfInterceptedVessels: Int? = null,
    var nbOfInterceptedMigrants: Int? = null,
    var nbOfSuspectedSmugglers: Int? = null,
    var latitude: Float? = null,
    var longitude: Float? = null
) {

    fun toActionIllegalImmigrationEntity(): ActionIllegalImmigrationEntity {
        return ActionIllegalImmigrationEntity(
            id = id ?: UUID.randomUUID(),
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            observations = observations,
            nbOfInterceptedVessels = nbOfInterceptedVessels,
            nbOfSuspectedSmugglers = nbOfSuspectedSmugglers,
            nbOfInterceptedMigrants = nbOfInterceptedMigrants,
            latitude = latitude,
            longitude = longitude
        )
    }
}
