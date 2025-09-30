package fr.gouv.gmampa.rapportnav.mocks.mission.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEnvTargetEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InfractionEntity2
import java.util.*

object InfractionEntity2Mock {

    fun create(
        id: UUID = UUID.randomUUID(),
        observations: String? = null,
        natinfs: List<String> = listOf(),
        infractionType: InfractionTypeEnum? = null
    ): InfractionEntity2 {
        return InfractionEntity2(
            id = id,
            infractionType = infractionType,
            natinfs = natinfs,
            observations = observations,
        )
    }
}
