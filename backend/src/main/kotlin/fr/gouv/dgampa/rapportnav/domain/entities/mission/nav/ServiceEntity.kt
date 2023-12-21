package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity

data class ServiceEntity(
    val id: Int? = null,
    val name: String,
    val agents: MutableSet<AgentEntity?> = HashSet(),
    val serviceLinked: ServiceEntity? = null,
    val controlUnits: List<Int>? = null
)
