package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.status

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import java.util.*

@UseCase
class DeleteStatus(private val statusRepository: INavActionStatusRepository) {
    fun execute(id: UUID): Boolean {
        if (this.statusRepository.existsById(id)) {
            this.statusRepository.deleteById(id)
            return true
        }
        return false
    }
}
