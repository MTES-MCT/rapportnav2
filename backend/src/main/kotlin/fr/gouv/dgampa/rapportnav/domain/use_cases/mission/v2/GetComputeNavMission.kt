package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeNavActionListByMissionId
import java.util.*

@UseCase
class GetComputeNavMission(
    private val getGeneralInfo2: GetGeneralInfo2,
    private val getNavMissionById2: GetNavMissionById2,
    private val getComputeNavActionListByMissionId: GetComputeNavActionListByMissionId
) {
    fun execute(missionId: UUID? = null, navMission: MissionNavEntity? = null): MissionEntity2 {
        if (missionId == null && navMission == null) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "Either missionId or navMission must be provided"
            )
        }

        val mission = navMission ?: getNavMissionById2.execute(missionId!!)
            ?: throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "Nav mission not found: $missionId"
            )

        val generalInfos = getGeneralInfo2.execute(missionIdUUID = mission.id, serviceId = navMission?.serviceId)
        val actions = getComputeNavActionListByMissionId.execute(ownerId = mission.id)

        return MissionEntity2(
            idUUID = mission.id,
            actions = actions,
            generalInfos = generalInfos,
            data = MissionEntity.fromMissionNavEntity(entity = mission)
        )
    }
}
