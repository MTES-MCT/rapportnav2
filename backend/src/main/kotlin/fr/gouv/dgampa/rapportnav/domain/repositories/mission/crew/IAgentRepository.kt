package fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel

interface IAgentRepository {
    fun deleteById(id: Int)
    fun disabledById(id: Int)
    fun findAll(): List<AgentModel>
    fun findById(id: Int): AgentModel?
    fun findAllActive(): List<AgentModel>
    fun save(model: AgentModel): AgentModel
    fun findByServiceId(serviceId: Int): List<AgentModel>
}
