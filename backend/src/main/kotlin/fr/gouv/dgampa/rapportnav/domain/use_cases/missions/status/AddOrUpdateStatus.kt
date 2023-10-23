package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.status

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository

@UseCase
class AddOrUpdateStatus(private val statusRepository: INavActionStatusRepository) {
    fun execute(statusAction: ActionStatusEntity): ActionStatusEntity {
        val savedData = this.statusRepository.save(statusAction)
        return savedData
    }
}
