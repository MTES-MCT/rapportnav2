package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgent2Repository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel2
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgent2Repository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.time.Instant
import kotlin.jvm.optionals.getOrNull

@Repository
class JPAAgent2Repository(private val dbAgent2Repository: IDBAgent2Repository) :
    IAgent2Repository {
    override fun findAll(): List<AgentModel2> {
        return dbAgent2Repository.findAll()
    }

    override fun findAllActive(): List<AgentModel2> {
        return dbAgent2Repository.findAll().filter { it.disabledAt == null }
    }

    override fun findById(id: Int): AgentModel2? {
        return dbAgent2Repository.findByIdOrNull(id)
    }

    override fun findByServiceId(serviceId: Int): List<AgentModel2> {
        return dbAgent2Repository.findByServiceId(serviceId).filter { it.disabledAt == null }
    }

    override fun save(model: AgentModel2): AgentModel2 {
        return dbAgent2Repository.save(model)
    }

    override fun deleteById(id: Int) {
        dbAgent2Repository.deleteById(id)
    }

    override fun disabledById(id: Int) {
        val agent = dbAgent2Repository.findById(id).getOrNull() ?: return
        agent.disabledAt = Instant.now()
        dbAgent2Repository.save(agent)
    }
}
