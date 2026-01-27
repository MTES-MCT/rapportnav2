package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import org.slf4j.LoggerFactory
import java.util.*

@UseCase
class GetNavMissionById2(
    private val repository: IMissionNavRepository
) {
    private val logger = LoggerFactory.getLogger(GetNavMissionById2::class.java)

    fun execute(id: UUID): MissionNavEntity? {
        return try {
            val mission = repository.finById(id).orElse(null) ?: return null
            MissionNavEntity.fromMissionModel(mission)
        } catch (e: Exception) {
            logger.error("GetNavMissionById2 failed loading mission id=$id", e)
            throw BackendInternalException(
                message = "Failed to fetch nav mission $id",
                originalException = e
            )
        }
    }
}
