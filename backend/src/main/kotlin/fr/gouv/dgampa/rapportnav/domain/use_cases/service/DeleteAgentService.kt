package fr.gouv.dgampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentServiceRepository


@UseCase
class DeleteAgentService(
    private val repo: IAgentServiceRepository
) {
    fun execute(id: Int) {
        return repo.deleteById(id)
    }
}
