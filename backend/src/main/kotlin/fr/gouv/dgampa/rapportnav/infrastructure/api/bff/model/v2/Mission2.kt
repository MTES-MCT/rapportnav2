package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2

data class Mission2(
    val id: Int,
    val status: MissionStatusEnum,
    val envData: MissionEnvData? = null,
    var actions: List<MissionAction?> = listOf(),
    val generalInfos: MissionGeneralInfo2? = null,
    val isCompleteForStats: Boolean? = null,
    val completenessForStats: CompletenessForStatsEntity? = null,
) {

    companion object {
        fun fromMissionEntity(mission: MissionEntity2): Mission2 {
            val completenessForStats = mission.isCompleteForStats()
            val status = mission.calculateMissionStatus(
                endDateTimeUtc = mission.envData.endDateTimeUtc,
                startDateTimeUtc = mission.envData.startDateTimeUtc
            )
            return Mission2(
                id = mission.id,
                status = status,
                completenessForStats = completenessForStats,
                envData = MissionEnvData.fromMissionEntity(mission.envData),
                isCompleteForStats = completenessForStats.sources?.isEmpty(),
                generalInfos = MissionGeneralInfo2.fromMissionGeneralInfoEntity(
                    generalInfo2 = mission.generalInfos
                ),
                actions = mission.actions.map { action -> MissionAction.fromMissionActionEntity(action) }
            )
        }
    }
}
