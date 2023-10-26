package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlNavigationRepository

@UseCase
class AddOrUpdateControlNavigation(private val controlNavigationRepository: IControlNavigationRepository) {
    fun execute(control: ControlNavigationEntity): ControlNavigationEntity {
        val savedData = this.controlNavigationRepository.save(control).toControlNavigationEntity()
        return savedData
    }
}
