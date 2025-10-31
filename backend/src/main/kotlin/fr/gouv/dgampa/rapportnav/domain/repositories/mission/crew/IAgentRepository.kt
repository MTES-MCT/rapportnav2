package fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel

interface IAgentRepository {
    fun findById(id: Int): AgentModel?
    fun findAll(): List<AgentModel>
    fun save(agent: AgentModel): AgentModel
    fun deleteById(id: Int)
}
