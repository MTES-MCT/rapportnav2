package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionBAAEMRepository
import java.util.*

@UseCase
class DeleteActionBAAEMPermanence(private val baaemPermanenceRepository: INavActionBAAEMRepository) {

    fun execute(id: UUID): Boolean {
        return try {
            if (this.baaemPermanenceRepository.existsById(id)) {
                this.baaemPermanenceRepository.deleteById(id = id)
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
