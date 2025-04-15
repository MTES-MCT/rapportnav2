package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.MissionNavInput
import fr.gouv.dgampa.rapportnav.infrastructure.exceptions.BackendRequestErrorCode
import fr.gouv.dgampa.rapportnav.infrastructure.exceptions.BackendRequestException
import org.slf4j.LoggerFactory

@UseCase
class UpdateMissionNav(
    private val repository: IMissionNavRepository,
    private val getNavMissionById2: GetNavMissionById2
) {

    private val logger = LoggerFactory.getLogger(UpdateMissionNav::class.java)

    fun execute(input: MissionNavInput) : MissionNavEntity? {

        if (input.id === null) throw BackendRequestException(message = "ID for input is missing", code = BackendRequestErrorCode.BODY_MISSING_DATA)

        val missionFromDb = getNavMissionById2.execute(input.id.toString()) ?: return null
        val missionFromDbInput = MissionNavInput.fromMissionEntity(missionFromDb)

        if (input.equals(missionFromDbInput)) return null

        try {
            val updatedMission = repository.save(input.toMissionNavEntity(missionFromDb = missionFromDb))
            return MissionNavEntity.fromMissionModel(updatedMission)
        }
        catch (e: Exception) {
            logger.error("Error while updating mission nav with id : ${input.id}", e)
            return null
        }
    }
}
