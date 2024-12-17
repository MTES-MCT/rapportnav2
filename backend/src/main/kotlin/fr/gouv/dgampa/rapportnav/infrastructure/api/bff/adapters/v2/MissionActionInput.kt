package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType

data class MissionActionInput(
    val id: String,
    val missionId: Int,
    val actionType: ActionType,
    val source: MissionSourceEnum,
    val env: MissionEnvActionDataInput? = null,
    val nav: MissionNavActionDataInput? = null,
    val fish: MissionFishActionDataInput? = null
){
}
