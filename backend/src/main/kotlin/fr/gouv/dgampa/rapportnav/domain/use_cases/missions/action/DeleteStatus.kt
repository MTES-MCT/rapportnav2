package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import java.time.ZonedDateTime
import java.util.*

@UseCase
class DeleteStatus(private val statusRepository: INavActionStatusRepository) {
    fun execute(id: UUID): Boolean {
        if (this.statusRepository.existsById(id)) {
            val statusAction = this.statusRepository.findById(id = id)
            statusAction.deletedAt = ZonedDateTime.now()
            this.statusRepository.save(statusAction.toActionStatusEntity())
            return true
        }
        return false
    }
}
