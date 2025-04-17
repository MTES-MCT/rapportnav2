package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEnvTargetEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.infraction.Infraction
import java.util.*

data class Infraction(
    var id: String? = null,
    var missionId: String? = null,
    var actionId: String? = null,
    var controlId: String? = null,
    var controlType: ControlType? = null,
    var infractionType: InfractionTypeEnum? = null,
    var natinfs: List<String>? = null,
    var observations: String? = null,
    var target: InfractionTarget? = null
) {
    fun setMissionIdAndActionId(missionId: String, actionId: String) {
        this.actionId = actionId
        this.missionId = missionId
    }

    fun toInfractionEntity(): InfractionEntity {
        return Infraction(
            id = id ?: UUID.randomUUID().toString(),
            missionId = missionId!!,
            actionId = actionId!!,
            controlId = controlId?.let { UUID.fromString(controlId) },
            controlType = controlType ,
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
            vesselSize = target?.vesselSize,
            vesselType = target?.vesselType,
            identityControlledPerson = target?.identityControlledPerson!!
        )
    }

    companion object {
        fun fromInfractionEntity(infraction: InfractionEntity?): fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Infraction {
            return Infraction(
                id = infraction?.id?.toString(),
                missionId = infraction?.missionId,
                actionId = infraction?.actionId,
                controlId = infraction?.controlId?.toString(),
                controlType = infraction?.controlType,
                infractionType = infraction?.infractionType,
                natinfs = infraction?.natinfs,
                observations = infraction?.observations,
                target = InfractionTarget.fromInfractionEntity(infraction?.target)
            )

        }

        fun fromEnvInfractionEntity(infraction: fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionEntity) =
            Infraction(
                id = infraction.id,
                missionId = "10",
                actionId = "",
                controlType = null,
                infractionType = infraction.infractionType,
                natinfs = infraction.natinf,
                observations = infraction.observations,
                target = InfractionTarget.fromInfractionEnvEntity(infraction)
            )
    }
}
