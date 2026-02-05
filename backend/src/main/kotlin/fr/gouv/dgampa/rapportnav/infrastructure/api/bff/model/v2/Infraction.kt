package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InfractionEntity
import java.util.*

class Infraction(
    val id: UUID? = null,
    var observations: String? = null,
    var natinfs: List<String> = listOf(),
    val infractionType: InfractionTypeEnum? = null
) {
    fun toInfractionEntity(): InfractionEntity {
        return InfractionEntity(
            id = id?: UUID.randomUUID(),
            natinfs = natinfs,
            observations = observations,
            infractionType = infractionType
        )
    }

    companion object {
        fun fromInfractionEntity(entity: InfractionEntity): Infraction {
            return Infraction(
                id = entity.id,
                natinfs = entity.natinfs,
                observations = entity.observations,
                infractionType = entity.infractionType
            )
        }
    }
}
