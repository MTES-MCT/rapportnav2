package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity

open class MissionActionOutput(
    open val id: String,
    open val missionId: Int,
    open val actionType: ActionType,
    open val source: MissionSourceEnum,
    open val status: ActionStatusType? = null,
    open val summaryTags: List<String>? = null,
    open val isCompleteForStats: Boolean? = null,
    open val controlsToComplete: List<ControlType>? = null,
    open val completenessForStats: CompletenessForStatsEntity? = null,
    open val sourcesOfMissingDataForStats: List<MissionSourceEnum>? = null,
    open val data: MissionActionDataOutput
) {
    companion object {
        fun fromMissionActionEntity(action: MissionActionEntity?): MissionActionOutput? {
            if(action == null) return null
            return when (action) {
                is MissionNavActionEntity -> MissionNavActionOutput.fromMissionActionEntity(action)
                is MissionEnvActionEntity -> MissionEnvActionOutput.fromMissionActionEntity(action)
                is MissionFishActionEntity -> MissionFishActionOutput.fromMissionActionEntity(action)
                else -> null
            }
        }
    }
}

