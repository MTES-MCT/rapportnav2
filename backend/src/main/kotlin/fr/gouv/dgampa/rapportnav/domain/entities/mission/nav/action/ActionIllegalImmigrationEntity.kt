package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action.NavActionIllegalImmigration
import java.time.ZonedDateTime
import java.util.*

class ActionIllegalImmigrationEntity(
    val id: UUID,
    val missionId: Int,
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime,
    val observations: String? = null,
    val latitude: Float? = null,
    val longitude: Float? = null,
    val nbOfInterceptedVessels: Int? = null,
    val nbOfInterceptedMigrants: Int? = null,
    val nbOfSuspectedSmugglers: Int? = null,
) {
    fun toNavActionIllegalImmigration(): NavActionIllegalImmigration {
        return NavActionIllegalImmigration(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            observations = observations,
            nbOfInterceptedMigrants = nbOfInterceptedMigrants,
            nbOfInterceptedVessels = nbOfInterceptedVessels,
            nbOfSuspectedSmugglers = nbOfSuspectedSmugglers,
            latitude = latitude,
            longitude = longitude
        )
    }

    fun toNavAction(): NavActionEntity {
        return NavActionEntity(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            actionType = ActionType.ILLEGAL_IMMIGRATION,
            illegalImmigrationAction = this
        )
    }
}
