package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.FormalNoticeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import java.util.*

data class InfractionEntity(
    val id: UUID,
    var missionId: Int,
    var actionId: String,
    var controlId: UUID? = null,
    var controlType: ControlType? = null,
    val infractionType: InfractionTypeEnum? = null,
    var observations: String? = null,
    var natinfs: List<NatinfEntity>? = null,
    var target: InfractionEnvTargetEntity? = null
)
