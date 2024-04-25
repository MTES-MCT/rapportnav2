package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionIllegalImmigrationRepository
import java.util.*

@UseCase
class DeleteActionIllegalImmigration(private val illegalImmigrationRepository: INavActionIllegalImmigrationRepository) {

    fun execute(id: UUID): Boolean {
        return try {
            if (this.illegalImmigrationRepository.existsById(id)) {
                this.illegalImmigrationRepository.deleteById(id = id)
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
