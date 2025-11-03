package fr.gouv.dgampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRepository

@UseCase
class CreateOrUpdateAgent(
    private val repo: IAgentRepository
) {
    fun execute(entity: AgentEntity): AgentEntity {
        return repo.save(entity.toAgentModel()).let { AgentEntity.fromAgentModel(it) }
    }
}
