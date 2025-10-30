package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentRoleEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRoleRepository

@UseCase
class GetAgentRoles(private val agentRoleRepository: IAgentRoleRepository) {

    fun execute(): List<AgentRoleEntity> {
        return agentRoleRepository.findAll().map { AgentRoleEntity.fromAgentRoleModel(it) }
    }
}
