package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.config.MandatoryForStats
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.utils.EntityCompletenessValidator
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action.NavActionIllegalImmigration
import java.time.ZonedDateTime
import java.util.*

class ActionIllegalImmigrationEntity(
    @MandatoryForStats
    val id: UUID,

    @MandatoryForStats
    val missionId: Int,

    var isCompleteForStats: Boolean? = null,
    var sourcesOfMissingDataForStats: List<MissionSourceEnum>? = null,

    @MandatoryForStats
    val startDateTimeUtc: ZonedDateTime,

    @MandatoryForStats
    val endDateTimeUtc: ZonedDateTime,

    val observations: String? = null,
    val latitude: Float? = null,
    val longitude: Float? = null,

    @MandatoryForStats
    val nbOfInterceptedVessels: Int? = null,

    @MandatoryForStats
    val nbOfInterceptedMigrants: Int? = null,

    @MandatoryForStats
    val nbOfSuspectedSmugglers: Int? = null,
) {

    constructor(
        id: UUID,
        missionId: Int,
        startDateTimeUtc: ZonedDateTime,
        endDateTimeUtc: ZonedDateTime,
        observations: String? = null,
        latitude: Float? = null,
        longitude: Float? = null,
        nbOfInterceptedVessels: Int? = null,
        nbOfInterceptedMigrants: Int? = null,
        nbOfSuspectedSmugglers: Int? = null,
    ) : this(
        id = id,
        missionId = missionId,
        isCompleteForStats = null,
        startDateTimeUtc = startDateTimeUtc,
        endDateTimeUtc = endDateTimeUtc,
        observations = observations,
        latitude = latitude,
        longitude = longitude,
        nbOfInterceptedVessels = nbOfInterceptedVessels,
        nbOfInterceptedMigrants = nbOfInterceptedMigrants,
        nbOfSuspectedSmugglers = nbOfSuspectedSmugglers,
    ) {
        // completeness for stats being computed at class instantiation in constructor
        this.isCompleteForStats = EntityCompletenessValidator.isCompleteForStats(this)
        this.sourcesOfMissingDataForStats = listOf(MissionSourceEnum.RAPPORTNAV)
    }

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

    fun toNavActionEntity(): NavActionEntity {
        return NavActionEntity(
            id = id,
            missionId = missionId,
            isCompleteForStats = isCompleteForStats,
            sourcesOfMissingDataForStats = sourcesOfMissingDataForStats,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            actionType = ActionType.ILLEGAL_IMMIGRATION,
            illegalImmigrationAction = this,
        )
    }
}
