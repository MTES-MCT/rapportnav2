package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlGensDeMerRepository
import java.util.*

@UseCase
class GetControlGensDeMerByActionId(private val repository: IControlGensDeMerRepository) {
    fun execute(actionControlId: String): ControlGensDeMerEntity? {
        if (this.repository.existsByActionControlId(actionControlId = actionControlId)) {
            return this.repository.findByActionControlId(actionControlId = actionControlId).toControlGensDeMerEntity()
        }
        return null
    }
}
