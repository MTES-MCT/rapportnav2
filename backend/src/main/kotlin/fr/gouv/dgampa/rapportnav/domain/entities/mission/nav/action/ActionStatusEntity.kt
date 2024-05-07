package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.config.DependentFieldValue
import fr.gouv.dgampa.rapportnav.config.MandatoryForStats
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.DOCKED_STATUS_AS_STRING
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.UNAVAILABLE_STATUS_AS_STRING
import fr.gouv.dgampa.rapportnav.domain.utils.EntityCompletenessValidator
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action.NavActionStatus
import java.time.ZonedDateTime
import java.util.*

data class ActionStatusEntity(
    @MandatoryForStats
    val id: UUID,

    @MandatoryForStats
    val missionId: Int,

    var isCompleteForStats: Boolean? = null,
    var sourcesOfMissingDataForStats: List<MissionSourceEnum>? = null,

    @MandatoryForStats
    val startDateTimeUtc: ZonedDateTime,

    @MandatoryForStats
    val status: ActionStatusType,

    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(
                field = "status",
                value = arrayOf(DOCKED_STATUS_AS_STRING, UNAVAILABLE_STATUS_AS_STRING)
            ),
        ]
    )
    val reason: ActionStatusReason? = null,

    val observations: String? = null,
) {

    constructor(
        id: UUID,
        missionId: Int,
        startDateTimeUtc: ZonedDateTime,
        status: ActionStatusType,
        reason: ActionStatusReason?,
        observations: String?
    ) : this(
        id = id,
        missionId = missionId,
        isCompleteForStats = null,
        startDateTimeUtc = startDateTimeUtc,
        status = status,
        reason = reason,
        observations = observations
    ) {
        // completeness for stats being computed at class instantiation in constructor
        this.isCompleteForStats = EntityCompletenessValidator.isCompleteForStats(this)
        this.sourcesOfMissingDataForStats = listOf(MissionSourceEnum.RAPPORTNAV)
    }

    fun toNavActionEntity(): NavActionEntity {
        return NavActionEntity(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = null,
            actionType = ActionType.STATUS,
            statusAction = this,
            isCompleteForStats = isCompleteForStats,
            sourcesOfMissingDataForStats = sourcesOfMissingDataForStats,
        )
    }

    fun toNavActionStatus(): NavActionStatus {
        return NavActionStatus(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            status = status,
            reason = reason,
            observations = observations,
        )
    }
}
