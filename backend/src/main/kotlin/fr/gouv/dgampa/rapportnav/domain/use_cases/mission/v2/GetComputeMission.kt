package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import java.util.UUID

@UseCase
class GetComputeMission(
    private val getNavMissionById2: GetNavMissionById2,
    private val getComputeEnvMission: GetComputeEnvMission,
    private val getComputeNavMission: GetComputeNavMission
) {
    fun execute(id: UUID): MissionEntity {
        val navMission = getNavMissionById2.execute(id = id)
            ?: throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "GetComputeMission: mission not found for id=$id"
            )

        val externalId = navMission.externalId?.toIntOrNull()

        return if (externalId != null) {
            getComputeEnvMission.execute(externalId = externalId)
        } else {
            getComputeNavMission.execute(navMission = navMission)
        }
    }
}
