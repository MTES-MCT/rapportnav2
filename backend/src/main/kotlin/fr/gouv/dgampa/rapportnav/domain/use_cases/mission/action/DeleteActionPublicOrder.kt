package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionPublicOrderRepository
import java.util.*

@UseCase
class DeleteActionPublicOrder(private val publicOrderRepository: INavActionPublicOrderRepository) {

    fun execute(id: UUID): Boolean {
        return try {
            if (this.publicOrderRepository.existsById(id)) {
                this.publicOrderRepository.deleteById(id = id)
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
