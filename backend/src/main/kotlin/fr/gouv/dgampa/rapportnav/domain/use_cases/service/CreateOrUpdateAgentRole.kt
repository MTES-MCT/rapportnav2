package fr.gouv.dgampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentRoleEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRoleRepository

@UseCase
class CreateOrUpdateAgentRole(
    private val repo: IAgentRoleRepository
) {
    fun execute(entity: AgentRoleEntity): AgentRoleEntity {
        return repo.save(entity.toAgentRoleModel()).let { AgentRoleEntity.fromAgentRoleModel(it) }
    }
}
