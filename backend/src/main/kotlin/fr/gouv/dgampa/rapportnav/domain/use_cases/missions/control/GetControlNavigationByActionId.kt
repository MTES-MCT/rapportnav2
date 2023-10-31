package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlNavigationRepository
import java.util.*

@UseCase
class GetControlNavigationByActionId(private val repository: IControlNavigationRepository) {
    fun execute(actionControlId: String): ControlNavigationEntity? {
        if (this.repository.existsByActionControlId(actionControlId = actionControlId)) {
            return this.repository.findByActionControlId(actionControlId = actionControlId).toControlNavigationEntity()
        }
        return null
    }
}
