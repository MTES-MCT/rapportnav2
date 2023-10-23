package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlGensDeMerRepository

@UseCase
class AddOrUpdateControlGensDeMer(private val repository: IControlGensDeMerRepository) {
    fun execute(control: ControlGensDeMerEntity): ControlGensDeMerEntity {
        val savedData = this.repository.save(control).toControlGensDeMer()
        return savedData
    }
}
