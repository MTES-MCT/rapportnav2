package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.FormalNoticeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEnvTargetEntity
import java.util.*

data class InfractionWithNewTargetInput(
    val id: String? = null,
    val missionId: Int,
    val actionId: String,
    val controlId: String? = null,
    val formalNotice: String? = null,
    val natinfs: List<NatinfInput>? = null,
    val observations: String? = null,
    val controlType: String,
    val vesselIdentifier: String,
    val vesselType: String,
    val vesselSize: String,
    val identityControlledPerson: String,
) {
    fun toInfractionEntity(target: InfractionEnvTargetEntity? = null): InfractionEntity {
        val newInfractionId = UUID.randomUUID()
        val newTarget = target ?: InfractionEnvTargetEntity(
            id = UUID.randomUUID(),
            actionId = actionId,
            missionId = missionId,
            infractionId = newInfractionId,
            vesselSize = VesselSizeEnum.valueOf(vesselSize),
            vesselType = VesselTypeEnum.valueOf(vesselType),
            vesselIdentifier = vesselIdentifier,
            identityControlledPerson = identityControlledPerson
        )

        return InfractionEntity(
            id = id?.let { UUID.fromString(it) } ?: newInfractionId,
            missionId = missionId,
            actionId = actionId,
            controlId = controlId?.let { UUID.fromString(it) },
            controlType = ControlType.valueOf(controlType),
            formalNotice = formalNotice?.let { FormalNoticeEnum.valueOf(it) },
//            natinfs = natinfs?.map { it.toNatinfEntity() } ?: emptyList(),
            observations = observations,
            target = newTarget
        )
    }
}
