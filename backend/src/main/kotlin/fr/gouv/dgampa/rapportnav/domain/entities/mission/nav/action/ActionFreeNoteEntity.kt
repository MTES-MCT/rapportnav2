package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.config.MandatoryForStats
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.utils.EntityCompletenessValidator
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action.NavActionFreeNote
import java.time.Instant
import java.util.*

data class ActionFreeNoteEntity(
    @MandatoryForStats
    override val id: UUID,

    @MandatoryForStats
    override val missionId: Int,

    override var isCompleteForStats: Boolean? = null,
    override var sourcesOfMissingDataForStats: List<MissionSourceEnum>? = null,

    @MandatoryForStats
    override val startDateTimeUtc: Instant,
    override val endDateTimeUtc: Instant? = null,

    override val observations: String? = null
) : BaseAction {
    constructor(
        id: UUID,
        missionId: Int,
        startDateTimeUtc: Instant,
        observations: String?
    ) : this(
        id = id,
        missionId = missionId,
        isCompleteForStats = null,
        startDateTimeUtc = startDateTimeUtc,
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
            actionType = ActionType.NOTE,
            isCompleteForStats = isCompleteForStats,
            sourcesOfMissingDataForStats = sourcesOfMissingDataForStats,
            freeNoteAction = this
        )
    }

    fun toNavActionFreeNote(): NavActionFreeNote {
        return NavActionFreeNote(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            observations = observations
        )
    }
}
