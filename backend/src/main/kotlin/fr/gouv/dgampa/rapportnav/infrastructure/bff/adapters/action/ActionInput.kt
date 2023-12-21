package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum

data class ActionByIdInput(
    val id: Int,
    val missionId: Int,
    val missionSource: MissionSourceEnum,
    val type: ActionTypeEnum
)
