package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentServiceRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentServiceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgentServiceRepository
import org.springframework.stereotype.Repository

@Repository
class JPAAgentServiceRepository(private val dbAgentServiceRepository: IDBAgentServiceRepository) :
    IAgentServiceRepository {
    override fun findAll(): List<AgentServiceModel> {
        return dbAgentServiceRepository.findAll();
    }

    override fun findByServiceId(serviceId: Int): List<AgentServiceModel> {
        return dbAgentServiceRepository.findByServiceId(serviceId);
    }
}
