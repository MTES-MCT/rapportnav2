package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionNauticalEventRepository
import java.util.*

@UseCase
class DeleteActionNauticalEvent(private val nauticalEventRepository: INavActionNauticalEventRepository) {

    fun execute(id: UUID): Boolean {
        return try {
            if (this.nauticalEventRepository.existsById(id)) {
                this.nauticalEventRepository.deleteById(id = id)
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
