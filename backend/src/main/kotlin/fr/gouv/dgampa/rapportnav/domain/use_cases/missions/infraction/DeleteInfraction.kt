package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.infraction

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction.IInfractionRepository
import java.util.*

@UseCase
class DeleteInfraction(private val statusRepository: IInfractionRepository) {
    fun execute(id: UUID): Boolean {
        return try {
            statusRepository.deleteById(id = id)
            true
        } catch (e: Exception) {
            // TODO add log
            false
        }

    }
}
