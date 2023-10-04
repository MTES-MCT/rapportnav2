package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.status

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatus
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository

@UseCase
class AddOrUpdateStatus(private val statusRepository: INavActionStatusRepository) {
    fun execute(statusAction: ActionStatus): ActionStatus {
        val savedData = this.statusRepository.save(statusAction)
        return savedData
    }
}
