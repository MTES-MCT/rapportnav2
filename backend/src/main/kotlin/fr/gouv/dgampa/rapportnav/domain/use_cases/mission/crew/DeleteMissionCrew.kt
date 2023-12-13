package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionCrewRepository

@UseCase
data class DeleteMissionCrew(private val crewRepository: IMissionCrewRepository) {

    fun execute(id: Int): Boolean {

        return try {
            crewRepository.deleteById(id = id)
        } catch (e: NoSuchElementException) {
            // TODO add log
            return false
        }
    }
}
