package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository

@UseCase
class AddOrUpdateActionStatus(private val statusRepository: INavActionStatusRepository) {
    fun execute(statusAction: ActionStatusEntity): ActionStatusEntity {
        val savedData = statusRepository.save(statusAction).toActionStatusEntity()
        return savedData
    }
}
