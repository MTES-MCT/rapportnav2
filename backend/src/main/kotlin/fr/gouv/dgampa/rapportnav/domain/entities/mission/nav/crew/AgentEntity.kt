package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew

import java.util.*

data class AgentEntity(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val deletedAt: Date?
)

typealias Agent = AgentEntity