package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControl
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionControlRepository

@UseCase
class AddOrUpdateControl(private val statusRepository: INavActionControlRepository) {
    fun execute(controlAction: ActionControl): ActionControl {
        val savedData = this.statusRepository.save(controlAction)
        return savedData
    }
}
