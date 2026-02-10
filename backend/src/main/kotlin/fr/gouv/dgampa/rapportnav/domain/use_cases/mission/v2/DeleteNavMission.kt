package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.DeleteNavAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetNavActionListByOwnerId
import java.util.*

@UseCase
class DeleteNavMission(
    private val missionRepo: IMissionNavRepository,
    private val deleteNavAction: DeleteNavAction,
    private val getNavActionListByOwnerId: GetNavActionListByOwnerId
) {
    fun execute(id: UUID? = null, serviceId: Int? = null) {
        if (id == null) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "DeleteNavMission: mission id is required"
            )
        }

        val mission = missionRepo.finById(id = id).orElse(null)
            ?: throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "DeleteNavMission: mission not found for id=$id"
            )

        if (mission.serviceId != serviceId) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION,
                message = "DeleteNavMission: mission does not belong to this service"
            )
        }

        if (!listOf(MissionSourceEnum.RAPPORT_NAV, MissionSourceEnum.RAPPORT_NAV).contains(mission.missionSource)) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION,
                message = "DeleteNavMission: mission source must be RAPPORT_NAV"
            )
        }

        deleteActions(ownerId = mission.id)
        missionRepo.deleteById(mission.id)
    }

    private fun deleteActions(ownerId: UUID): List<Unit> {
        return getNavActionListByOwnerId.execute(ownerId = ownerId).map { deleteNavAction.execute(id = it.id) }
    }
}
