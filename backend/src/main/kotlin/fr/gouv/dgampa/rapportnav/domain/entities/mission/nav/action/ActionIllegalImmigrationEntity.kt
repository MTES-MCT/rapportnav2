package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.config.MandatoryForStats
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.utils.EntityCompletenessValidator
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action.NavActionIllegalImmigration
import java.time.Instant
import java.util.*

class ActionIllegalImmigrationEntity(
    @MandatoryForStats
    override val id: UUID,

    @MandatoryForStats
    override val missionId: String,

    override var isCompleteForStats: Boolean? = null,
    override var sourcesOfMissingDataForStats: List<MissionSourceEnum>? = null,

    @MandatoryForStats
    override val startDateTimeUtc: Instant,

    @MandatoryForStats
    override val endDateTimeUtc: Instant? = null,

    override val observations: String? = null,
    val latitude: Float? = null,
    val longitude: Float? = null,

    @MandatoryForStats
    val nbOfInterceptedVessels: Int? = null,

    @MandatoryForStats
    val nbOfInterceptedMigrants: Int? = null,

    @MandatoryForStats
    val nbOfSuspectedSmugglers: Int? = null,
) : BaseAction {

    constructor(
        id: UUID,
        missionId: String,
        startDateTimeUtc: Instant,
        endDateTimeUtc: Instant? = null,
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
