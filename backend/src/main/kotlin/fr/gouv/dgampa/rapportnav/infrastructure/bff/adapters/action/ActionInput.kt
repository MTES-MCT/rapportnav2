package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import java.time.ZonedDateTime
import java.util.*

data class ActionByIdInput(
    val id: Int,
    val missionId: Int,
    val missionSource: MissionSourceEnum,
    val type: ActionTypeEnum
)
