package fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel

interface IAgentRoleRepository {
    fun findAll(): List<AgentRoleModel>
    fun save(agent: AgentRoleModel): AgentRoleModel
    fun deleteById(id: Int)
}
