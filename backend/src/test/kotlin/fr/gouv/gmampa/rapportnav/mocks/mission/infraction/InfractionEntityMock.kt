package fr.gouv.gmampa.rapportnav.mocks.mission.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InfractionEntity
import java.util.*

object InfractionEntityMock {

    fun create(
        id: UUID = UUID.randomUUID(),
        observations: String? = null,
        natinfs: List<String> = listOf(),
        infractionType: InfractionTypeEnum? = null
    ): fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InfractionEntity {
        return InfractionEntity(
            id = id,
            infractionType = infractionType,
            natinfs = natinfs,
            observations = observations,
        )
    }
}
