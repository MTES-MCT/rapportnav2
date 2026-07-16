package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionCrewRepository
import org.slf4j.LoggerFactory

@UseCase
data class DeleteMissionCrew(
    private val crewRepository: IMissionCrewRepository
) {

    private val logger = LoggerFactory.getLogger(DeleteMissionCrew::class.java)

    fun execute(id: Int): Boolean {
        return try {
            crewRepository.deleteById(id = id)
        } catch (e: Exception) {
            val errorType = if (e is NoSuchElementException) "NoSuchElementException" else "Exception"
            logger.error("DeleteMissionCrew - $errorType: ${e.message}")
            false
        }
    }
}
