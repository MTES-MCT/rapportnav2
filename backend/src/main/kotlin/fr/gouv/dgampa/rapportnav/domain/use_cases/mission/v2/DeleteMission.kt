package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import java.util.UUID

@UseCase
class DeleteMission(
    private val getNavMissionById2: GetNavMissionById2,
    private val deleteNavMission: DeleteNavMission,
    private val deleteEnvMission: DeleteEnvMission,
) {
    fun execute(id: UUID?, serviceId: Int?) {
        if (id == null) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "DeleteMission: mission id is required"
            )
        }

        val navMission = getNavMissionById2.execute(id = id)
            ?: throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "DeleteMission: mission not found for id=$id"
            )

        val externalId = navMission.externalId?.toIntOrNull()
        if (externalId != null) {
            deleteEnvMission.execute(id = externalId, serviceId = serviceId)
        }

        deleteNavMission.execute(id = id, serviceId = serviceId)
    }
}
