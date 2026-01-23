package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetServiceByControlUnit

@UseCase
class DeleteEnvMission(
    private val missionRepo: IEnvMissionRepository,
    private val getEnvMissionById2: GetEnvMissionById2,
    private val getServiceByControlUnit: GetServiceByControlUnit
) {
    fun execute(id: Int?, serviceId: Int?) {
        if (id == null) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "DeleteEnvMission: mission id is required"
            )
        }

        val mission = getEnvMissionById2.execute(missionId = id)
            ?: throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "DeleteEnvMission: mission not found for id=$id"
            )

        val serviceIds = getServiceByControlUnit.execute(controlUnits = mission.controlUnits).map { it.id }

        if (!serviceIds.contains(serviceId)) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION,
                message = "DeleteEnvMission: mission does not belong to this service"
            )
        }

        if (mission.envActions?.isNotEmpty() == true) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION,
                message = "DeleteEnvMission: cannot delete mission with existing actions"
            )
        }

        missionRepo.deleteMission(missionId = id)
    }
}
