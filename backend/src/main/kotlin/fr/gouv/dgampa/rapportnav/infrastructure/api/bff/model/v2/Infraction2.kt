package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InfractionEntity2
import java.util.*

class Infraction2(
    val id: UUID? = null,
    var controlId: UUID? = null,
    var observations: String? = null,
    var natinfs: List<String> = listOf(),
    val infractionType: InfractionTypeEnum? = null
) {
    fun toInfractionEntity(): InfractionEntity2 {
        return InfractionEntity2(
            id = id?: UUID.randomUUID(),
            natinfs = natinfs,
            controlId = controlId,
            observations = observations,
            infractionType = infractionType
        )
    }

    companion object {
        fun fromInfractionEntity(entity: InfractionEntity2): Infraction2 {
            return Infraction2(
                id = entity.id,
                natinfs = entity.natinfs,
                controlId = entity.controlId,
                observations = entity.observations,
                infractionType = entity.infractionType
            )
        }
    }
}
