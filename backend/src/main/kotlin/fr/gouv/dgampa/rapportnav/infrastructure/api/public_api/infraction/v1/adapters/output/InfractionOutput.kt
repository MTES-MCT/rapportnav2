package fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.infraction.v1.adapters.output

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import java.util.UUID

data class InfractionOutput(
    val id: UUID,
    var observations: String? = null,
    var natinfs: List<String> = listOf(),
    val infractionType: InfractionTypeEnum? = null
)
