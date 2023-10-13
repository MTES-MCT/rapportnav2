package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMer
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlGensDeMerRepository

@UseCase
class AddOrUpdateControlGensDeMer(private val repository: IControlGensDeMerRepository) {
    fun execute(control: ControlGensDeMer): ControlGensDeMer {
        val savedData = this.repository.save(control)
        return savedData
    }
}
