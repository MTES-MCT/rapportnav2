package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionControlRepository
import java.util.*

@UseCase
class DeleteControl(private val controlRepository: INavActionControlRepository) {
    fun execute(id: UUID): Boolean {
        if (this.controlRepository.existsById(id)) {
            this.controlRepository.deleteById(id)
            return true
        }
        return false
    }
}
