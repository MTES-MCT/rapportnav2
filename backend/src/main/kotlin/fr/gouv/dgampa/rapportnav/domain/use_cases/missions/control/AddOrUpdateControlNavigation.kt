package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigation
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlNavigationRepository

@UseCase
class AddOrUpdateControlNavigation(private val controlNavigationRepository: IControlNavigationRepository) {
    fun execute(control: ControlNavigation): ControlNavigation {
        val savedData = this.controlNavigationRepository.save(control)
        return savedData
    }
}
