package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionPassengerRepository
import org.slf4j.LoggerFactory

@UseCase
data class DeleteMissionPassenger(
    private val repo: IMissionPassengerRepository
) {

    private val logger = LoggerFactory.getLogger(DeleteMissionPassenger::class.java)

    fun execute(id: Int): Boolean {
        return try {
            repo.deleteById(id = id)
        } catch (e: Exception) {
            val errorType = if (e is NoSuchElementException) "NoSuchElementException" else "Exception"
            logger.error("DeleteMissionPassenger - $errorType: ${e.message}")
            false
        }
    }
}
