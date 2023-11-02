package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlAdministrativeRepository

@UseCase
class GetControlAdministrativeByActionId(private val repository: IControlAdministrativeRepository) {
    fun execute(actionControlId: String): ControlAdministrativeEntity? {
        if (this.repository.existsByActionControlId(actionControlId = actionControlId)) {
            val controlModel = this.repository.findByActionControlId(actionControlId = actionControlId)
            if (controlModel.deletedAt == null) {
                return controlModel.toControlAdministrativeEntity()
            }
            return null
        }
        return null
    }
}
