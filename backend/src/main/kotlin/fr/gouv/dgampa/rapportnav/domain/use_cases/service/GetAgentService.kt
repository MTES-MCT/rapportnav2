package fr.gouv.dgampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentServiceEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgentServiceRepository

@UseCase
class GetAgentService(
    private val agentServiceRepo: IDBAgentServiceRepository
) {
    fun execute(): List<AgentServiceEntity> {
        return agentServiceRepo.findAll().map { AgentServiceEntity.fromAgentServiceModel(it) }
    }
}
