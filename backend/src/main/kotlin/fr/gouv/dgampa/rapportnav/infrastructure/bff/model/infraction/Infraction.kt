package fr.gouv.dgampa.rapportnav.infrastructure.bff.model.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import java.util.*

data class Infraction(
    val id: String,
    val missionId: Int,
    val actionId: String,
    val controlId: UUID? = null,
    val controlType: ControlType? = null,
    val infractionType: String? = null,
    val natinfs: List<Natinf>? = null,
    val observations: String? = null,
    val target: InfractionTarget? = null,

    ) {
    fun toInfractionEntity(): InfractionEntity {
        val uuid = try {
            UUID.fromString(id)
        } catch (e: IllegalArgumentException) {
            // If the provided id is not a valid UUID, generate a new UUID
            UUID.randomUUID()
        }
        return InfractionEntity(
            id = uuid,
            missionId = missionId,
            actionId = actionId,
            controlId = controlId,
            controlType = controlType,
            infractionType = infractionType?.let { InfractionTypeEnum.valueOf(it) },
            natinfs = natinfs?.map { it.toNatinfEntity() },
            observations = observations,
        )
    }

    companion object {
        fun fromInfractionEntity(infraction: InfractionEntity) = Infraction(
            id = infraction.id.toString(),
            missionId = infraction.missionId,
            actionId = infraction.actionId,
            controlId = infraction.controlId,
            controlType = infraction.controlType,
            infractionType = infraction.infractionType?.toString(),
            natinfs = infraction.natinfs?.map { Natinf.fromNatinfEntity(it) },
            observations = infraction.observations,
            target = InfractionTarget.fromInfractionEntity(infraction)
        )

        fun fromEnvInfractionEntity(infraction: fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionEntity) =
            Infraction(
                id = infraction.id,
                missionId = 10,
                actionId = "",
//            missionId = infraction.missionId,
//            actionId = infraction.actionId,
                controlType = null,
                infractionType = infraction.infractionType.toString(),
//            natinfs = infraction.natinfs?.map{ Natinf.fromNatinfEntity(it) },
                natinfs = listOf(),
                observations = infraction.observations,
                target = InfractionTarget.fromEnvInfractionEntity(infraction)

            )
    }

}
