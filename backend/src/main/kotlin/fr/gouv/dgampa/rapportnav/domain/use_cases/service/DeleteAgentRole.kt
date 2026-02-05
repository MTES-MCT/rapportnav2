package fr.gouv.dgampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRoleRepository


@UseCase
class DeleteAgentRole(
    private val repo: IAgentRoleRepository
) {
    fun execute(id: Int) {
        return repo.deleteById(id)
    }
}
