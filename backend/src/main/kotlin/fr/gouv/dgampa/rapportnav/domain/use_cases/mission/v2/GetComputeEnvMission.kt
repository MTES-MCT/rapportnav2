package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetMissionAction

@UseCase
class GetComputeEnvMission(
    private val getGeneralInfos2: GetGeneralInfo2,
    private val getMissionAction: GetMissionAction,
    private val getEnvMissionById2: GetEnvMissionById2,
) {
    fun execute(missionId: Int? = null, envMission: MissionEntity? = null): MissionEntity2 {
        if (missionId == null && envMission == null) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "Either missionId or envMission must be provided"
            )
        }

        val mission = envMission ?: getEnvMissionById2.execute(missionId!!)
            ?: throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "Env mission not found: $missionId"
            )

        val id = mission.id
            ?: throw BackendInternalException(message = "Mission has no id")

        val actions = getMissionAction.execute(missionId = id)
        val generalInfos = getGeneralInfos2.execute(missionId = id, controlUnits = mission.controlUnits)

        return MissionEntity2(
            id = id,
            data = mission,
            actions = actions,
            generalInfos = generalInfos
        )
    }
}

