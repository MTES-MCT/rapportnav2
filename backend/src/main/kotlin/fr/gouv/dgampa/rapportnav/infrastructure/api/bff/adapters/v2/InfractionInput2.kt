package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEnvTargetEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.infraction.Infraction
import java.util.*

data class InfractionInput2(
    var id: String? = null,
    var missionId: Int? = null,
    var actionId: String? = null,
    var controlId: String? = null,
    var controlType: String? = null,
    var infractionType: InfractionTypeEnum? = null,
    var natinfs: List<String>? = null,
    var observations: String? = null,
    var target: InfractionTargetInput2? = null
) {
    fun setMissionIdAndActionId(missionId: Int, actionId: String) {
        this.actionId = actionId
        this.missionId = missionId
    }

    fun toInfractionEntity(): InfractionEntity {
        return Infraction(
            id = id ?: UUID.randomUUID().toString(),
            missionId = missionId!!,
            actionId = actionId!!,
            controlId = controlId?.let { UUID.fromString(controlId) },
            controlType = controlType?.let { ControlType.valueOf(it) },
            natinfs = natinfs,
            infractionType = infractionType,
            observations = observations,
        ).toInfractionEntity()
    }

    fun toInfractionEnvTargetEntity(): InfractionEnvTargetEntity {
        return InfractionEnvTargetEntity(
            actionId = actionId!!,
            missionId = missionId!!,
            id = target?.id?.let { UUID.fromString(it) } ?: UUID.randomUUID(),
            infractionId = id?.let { UUID.fromString(it) } ?: UUID.randomUUID(),
            vesselIdentifier = target?.vesselIdentifier,
            vesselSize = target?.vesselSize?.let { VesselSizeEnum.valueOf(it) },
            vesselType = target?.vesselType?.let { VesselTypeEnum.valueOf(it) },
            identityControlledPerson = target?.identityControlledPerson!!
        )
    }
}
