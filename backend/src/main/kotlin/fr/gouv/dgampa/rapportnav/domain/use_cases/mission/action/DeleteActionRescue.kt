package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionRescueRepository
import java.util.*

@UseCase
class DeleteActionRescue(private val rescueRepository: INavActionRescueRepository) {

    fun execute(id: UUID): Boolean {
        return try {
            if (this.rescueRepository.existsById(id)) {
                this.rescueRepository.deleteById(id = id)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            // TODO add log
            false
        }

    }
}
