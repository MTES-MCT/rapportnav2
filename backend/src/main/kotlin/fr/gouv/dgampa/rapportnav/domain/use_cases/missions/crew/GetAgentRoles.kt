package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.crew

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRoleRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel

@UseCase
class GetAgentRoles(private val agentRoleRepository: IAgentRoleRepository) {

  fun execute(): List<AgentRoleModel>{
    return agentRoleRepository.findAll()
  }
}
