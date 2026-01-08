package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgentRepository
import org.springframework.stereotype.Repository
import tools.jackson.databind.json.JsonMapper
import java.time.Instant
import kotlin.jvm.optionals.getOrNull

@Repository
class JPAAgentRepository(
    private val dbAgentRepository: IDBAgentRepository,
    private val mapper: JsonMapper
) : IAgentRepository {

    override fun findById(id: Int): AgentModel? {
        return dbAgentRepository.findById(id).getOrNull()
    }

    override fun findAll(): List<AgentModel> {
        return dbAgentRepository.findAll()
    }

    override fun save(agent: AgentModel): AgentModel {
        return dbAgentRepository.save(agent)
    }

    override fun deleteById(id: Int) {
        val agent = dbAgentRepository.findById(id).getOrNull() ?: return
        agent.deletedAt = Instant.now()
        dbAgentRepository.save(agent)
    }
}
