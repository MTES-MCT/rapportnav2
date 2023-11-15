package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import java.util.*

data class AgentEntity(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val deletedAt: Date?,
    val services: Set<ServiceEntity>
)
