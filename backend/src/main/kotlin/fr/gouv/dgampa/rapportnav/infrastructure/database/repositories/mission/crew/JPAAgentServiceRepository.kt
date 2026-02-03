package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentServiceRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentServiceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgentServiceRepository
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class JPAAgentServiceRepository(private val dbAgentServiceRepository: IDBAgentServiceRepository) :
    IAgentServiceRepository {
    override fun findAll(): List<AgentServiceModel> {
        return dbAgentServiceRepository.findAll();
    }

    override fun findByServiceId(serviceId: Int): List<AgentServiceModel> {
        return dbAgentServiceRepository.findByServiceId(serviceId);
    }

    override fun save(model: AgentServiceModel): AgentServiceModel {
        return dbAgentServiceRepository.save(model);
    }

    override fun deleteById(id: Int) {
        val agentService = dbAgentServiceRepository.findById(id).getOrNull() ?: return
        dbAgentServiceRepository.save(agentService)
    }

}
