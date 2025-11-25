package fr.gouv.dgampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgent2Repository


@UseCase
class DisableAgent(
    private val repo: IAgent2Repository
) {
    fun execute(id: Int) {
        return repo.disabledById(id)
    }
}
