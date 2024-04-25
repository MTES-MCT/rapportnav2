package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionRepresentationRepository
import java.util.*

@UseCase
class DeleteActionRepresentation(private val representationRepository: INavActionRepresentationRepository) {

    fun execute(id: UUID): Boolean {
        return try {
            if (this.representationRepository.existsById(id)) {
                this.representationRepository.deleteById(id = id)
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
