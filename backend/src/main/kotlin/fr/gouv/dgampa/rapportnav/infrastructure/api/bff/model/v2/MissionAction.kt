package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "source",
    visible = true,
)
@JsonSubTypes(
    JsonSubTypes.Type(MissionFishAction::class, name = "MONITORENV"),
    JsonSubTypes.Type(MissionEnvAction::class, name = "MONITORFISH"),
    JsonSubTypes.Type(MissionNavAction::class, name = "RAPPORTNAV"),
)
abstract class MissionAction(
    open val id: String? = null,
    open val missionId: Int,
    open val actionType: ActionType,
    open val source: MissionSourceEnum,
    open val status: ActionStatusType? = null,
    open val summaryTags: List<String>? = null,
    open val isCompleteForStats: Boolean? = null,
    open val controlsToComplete: List<ControlType>? = null,
    open val completenessForStats: CompletenessForStatsEntity? = null,
    open val sourcesOfMissingDataForStats: List<MissionSourceEnum>? = null,
    open val data: MissionActionData? = null
){
    companion object {
        fun fromMissionActionEntity(action: MissionActionEntity?): MissionAction? {
            if(action == null) return null
            return when (action) {
                is MissionNavActionEntity -> MissionNavAction.fromMissionActionEntity(action)
                is MissionEnvActionEntity -> MissionEnvAction.fromMissionActionEntity(action)
                is MissionFishActionEntity -> MissionFishAction.fromMissionActionEntity(action)
                else -> null
            }
        }
    }
}
