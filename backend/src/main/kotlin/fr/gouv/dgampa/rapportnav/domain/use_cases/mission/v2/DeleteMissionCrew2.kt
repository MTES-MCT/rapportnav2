package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionCrewRepository
import org.slf4j.LoggerFactory

@UseCase
data class DeleteMissionCrew2(
    private val crewRepository: IMissionCrewRepository
) {

    private val logger = LoggerFactory.getLogger(DeleteMissionCrew2::class.java)

    fun execute(id: Int): Boolean {
        return try {
            crewRepository.deleteById(id = id)
        } catch (e: NoSuchElementException) {
            logger.error("DeleteMissionCrew - NoSuchElementException: ${e.message}")
            return false
        }
        catch (e: Exception) {
            logger.error("DeleteMissionCrew: Exception: ${e.message}")
            return false
        }
    }
}
