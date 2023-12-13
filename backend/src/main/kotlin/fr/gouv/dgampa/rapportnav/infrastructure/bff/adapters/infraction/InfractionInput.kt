package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.infraction.Infraction
import java.util.*

data class InfractionInput(
    val id: String? = null,
    val missionId: Int,
    val actionId: String,
    val controlId: String? = null,
    val controlType: String,
    val infractionType: String? = null,
    val natinfs: List<NatinfInput>? = null,
    val observations: String? = null,
) {
    fun toInfraction(): Infraction {
        return Infraction(
            id = id ?: UUID.randomUUID().toString(),
            missionId = missionId,
            actionId = actionId,
            controlId = controlId?.let { UUID.fromString(it) },
            controlType = ControlType.valueOf(controlType),
            natinfs = natinfs?.map { it.toNatinf() },
            infractionType = infractionType?.let { InfractionTypeEnum.valueOf(infractionType) },
            observations = observations,
        )
    }
}
