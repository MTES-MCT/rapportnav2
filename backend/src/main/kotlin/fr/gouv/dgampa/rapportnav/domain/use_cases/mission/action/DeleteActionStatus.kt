package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import java.util.*

@UseCase
class DeleteActionStatus(private val statusRepository: INavActionStatusRepository) {
    fun execute(id: UUID): Boolean {
        return try {
            if (this.statusRepository.existsById(id)) {
                this.statusRepository.deleteById(id = id)
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
