package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import java.time.ZonedDateTime
import java.util.*

data class NavActionEntity(
    val id: UUID,
    val missionId: Int,
    var isCompleteForStats: Boolean? = null,
    val sourcesOfMissingDataForStats: List<MissionSourceEnum>? = null,
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime?,
    val actionType: ActionType,
    val controlAction: ActionControlEntity? = null,
    val statusAction: ActionStatusEntity? = null,
    val freeNoteAction: ActionFreeNoteEntity? = null,
    val rescueAction: ActionRescueEntity? = null,
    val nauticalEventAction: ActionNauticalEventEntity? = null,
    val vigimerAction: ActionVigimerEntity? = null,
    val baaemPermanenceAction: ActionBAAEMPermanenceEntity? = null,
    val antiPollutionAction: ActionAntiPollutionEntity? = null,
    val publicOrderAction: ActionPublicOrderEntity? = null,
    val representationAction: ActionRepresentationEntity? = null,
    val illegalImmigrationAction: ActionIllegalImmigrationEntity? = null
)
