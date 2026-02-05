package fr.gouv.dgampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRepository


@UseCase
class DeleteAgent(
    private val repo: IAgentRepository
) {
    fun execute(id: Int) {
        return repo.deleteById(id)
    }
}
