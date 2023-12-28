package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import java.util.*

data class AgentEntity(
    val id: Int? = null,
    val firstName: String,
    val lastName: String,
    val deletedAt: Date? = null,
    val services: MutableSet<ServiceEntity?> = HashSet(),
)
