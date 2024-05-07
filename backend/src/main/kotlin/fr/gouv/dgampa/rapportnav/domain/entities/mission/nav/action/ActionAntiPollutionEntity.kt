package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.config.MandatoryForStats
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.utils.EntityCompletenessValidator
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action.NavActionAntiPollution
import java.time.ZonedDateTime
import java.util.*

class ActionAntiPollutionEntity(
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

    val latitude: Double? = null,

    val longitude: Double? = null,

    val detectedPollution: Boolean? = null,

    val pollutionObservedByAuthorizedAgent: Boolean? = null,

    val diversionCarriedOut: Boolean? = null,
) {

    constructor(
        id: UUID,
        missionId: Int,
        startDateTimeUtc: ZonedDateTime,
        endDateTimeUtc: ZonedDateTime,
        observations: String?,
        latitude: Double? = null,
        longitude: Double? = null,
        detectedPollution: Boolean? = null,
        pollutionObservedByAuthorizedAgent: Boolean? = null,
        diversionCarriedOut: Boolean? = null,
    ) : this(
        id = id,
        missionId = missionId,
        isCompleteForStats = null,
        startDateTimeUtc = startDateTimeUtc,
        endDateTimeUtc = endDateTimeUtc,
        observations = observations,
        latitude = latitude,
        longitude = longitude,
        detectedPollution = detectedPollution,
        pollutionObservedByAuthorizedAgent = pollutionObservedByAuthorizedAgent,
        diversionCarriedOut = diversionCarriedOut
    ) {
        // completeness for stats being computed at class instantiation in constructor
        this.isCompleteForStats = EntityCompletenessValidator.isCompleteForStats(this)
        this.sourcesOfMissingDataForStats = listOf(MissionSourceEnum.RAPPORTNAV)
    }

    fun toNavActionAntiPollution(): NavActionAntiPollution {
        return NavActionAntiPollution(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            observations = observations,
            latitude = latitude,
            longitude = longitude,
            detectedPollution = detectedPollution,
            pollutionObservedByAuthorizedAgent = pollutionObservedByAuthorizedAgent,
            diversionCarriedOut = diversionCarriedOut
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
            actionType = ActionType.ANTI_POLLUTION,
            antiPollutionAction = this
        )
    }
}
