package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlSecurityRepository

@UseCase
class GetControlSecurityByActionId(private val repository: IControlSecurityRepository) {
    fun execute(actionControlId: String): ControlSecurityEntity? {
        if (this.repository.existsByActionControlId(actionControlId = actionControlId)) {
            return this.repository.findByActionControlId(actionControlId = actionControlId).toControlSecurityEntity()
        }
        return null
    }
}
