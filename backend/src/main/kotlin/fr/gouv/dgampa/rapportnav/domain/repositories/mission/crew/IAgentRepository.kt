package fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel2

interface IAgent2Repository {
    fun deleteById(id: Int)
    fun disabledById(id: Int)
    fun findAll(): List<AgentModel2>
    fun findById(id: Int): AgentModel2?
    fun findAllActive(): List<AgentModel2>
    fun save(model: AgentModel2): AgentModel2
    fun findByServiceId(serviceId: Int): List<AgentModel2>
}
