package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlAdministrativeRepository
import java.util.*

@UseCase
class GetControlAdministrativeByActionId(private val repository: IControlAdministrativeRepository) {
    fun execute(actionControlId: UUID): ControlAdministrativeEntity? {
        if (this.repository.existsByActionControlId(actionControlId = actionControlId)) {
            return this.repository.findByActionControlId(actionControlId = actionControlId).toControlAdministrativeEntity()
        }

        return null
    }
}
