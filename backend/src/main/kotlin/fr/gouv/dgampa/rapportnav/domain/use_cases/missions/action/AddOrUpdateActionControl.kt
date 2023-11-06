package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionControlRepository

@UseCase
class AddOrUpdateActionControl(private val statusRepository: INavActionControlRepository) {
    fun execute(controlAction: ActionControlEntity): ActionControlEntity {
        val savedData = this.statusRepository.save(controlAction).toActionControlEntity()
        return savedData
    }
}
