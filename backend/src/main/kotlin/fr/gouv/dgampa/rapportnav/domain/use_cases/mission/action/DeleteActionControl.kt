package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionControlRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.DeleteControlByActionId
import java.util.*

@UseCase
class DeleteActionControl(
    private val controlRepository: INavActionControlRepository,
    private val deleteControlByActionId: DeleteControlByActionId
) {
    fun execute(id: UUID): Boolean {
        if (this.controlRepository.existsById(id)) {

            // delete controls manually as cascading not in place in Models
            deleteControlByActionId.deleteAll(actionId = id)

            controlRepository.deleteById(id)
            return true
        }
        return false
    }
}
