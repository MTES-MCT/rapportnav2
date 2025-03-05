package fr.gouv.dgampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentServiceEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgentServiceRepository

@UseCase
class GetCrewByServiceId(
    private val agentServiceRepo: IDBAgentServiceRepository,
) {

    val rolePriority = listOf(
        "Commandant",
        "Second capitaine",
        "Second",
        "Chef mécanicien",
        "Second mécanicien",
        "Mécanicien électricien",
        "Mécanicien",
        "Chef de quart",
        "Maître d’équipage",
        "Agent pont",
        "Agent machine",
        "Agent mécanicien",
        "Électricien",
        "Cuisinier",
    )

    fun execute(serviceId: Int):  List<AgentServiceEntity> {
        // select from agent service by service
        val agents =  agentServiceRepo
            .findByServiceId(serviceId = serviceId)
            .mapNotNull{ it.toAgentServiceEntity() }
            .sortedBy { rolePriority.indexOf(it.role.title) }
        return agents
    }
}
