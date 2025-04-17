package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.config.MandatoryForStats
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.utils.EntityCompletenessValidator
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action.NavActionNauticalEvent
import java.time.Instant
import java.util.*

class ActionNauticalEventEntity(
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
) : BaseAction {

    constructor(
        id: UUID,
        missionId: String,
        startDateTimeUtc: Instant,
        endDateTimeUtc: Instant? = null,
        observations: String? = null
    ) : this(
        id = id,
        missionId = missionId,
        isCompleteForStats = null,
        startDateTimeUtc = startDateTimeUtc,
        endDateTimeUtc = endDateTimeUtc,
        observations = observations
    ) {
        // completeness for stats being computed at class instantiation in constructor
        this.isCompleteForStats = EntityCompletenessValidator.isCompleteForStats(this)
        this.sourcesOfMissingDataForStats = listOf(MissionSourceEnum.RAPPORTNAV)
    }

    fun toNavActionNauticalEvent(): NavActionNauticalEvent {
        return NavActionNauticalEvent(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            observations = observations
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
            actionType = ActionType.NAUTICAL_EVENT,
            nauticalEventAction = this
        )
    }
}
