package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionAntiPollutionRepository
import java.util.*

@UseCase
class DeleteActionAntiPollution(private val antiPollutionRepository: INavActionAntiPollutionRepository) {

    fun execute(id: UUID): Boolean {
        return try {
            if (this.antiPollutionRepository.existsById(id)) {
                this.antiPollutionRepository.deleteById(id = id)
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
