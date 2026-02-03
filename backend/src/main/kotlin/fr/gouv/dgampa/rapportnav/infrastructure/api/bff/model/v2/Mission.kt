package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2

data class Mission2(
    val id: Int? = null,
    val idUUID: String? = null,
    val status: MissionStatusEnum,
    val data: MissionData? = null,
    var actions: List<MissionAction?> = listOf(),
    val generalInfos: MissionGeneralInfo2? = null,
    val isCompleteForStats: Boolean? = null,
    val completenessForStats: CompletenessForStatsEntity? = null,
) {

    companion object {
        fun fromMissionEntity(mission: MissionEntity): Mission2 {
            val completenessForStats = mission.isCompleteForStats()
            val status = mission.calculateMissionStatus(
                endDateTimeUtc = mission.data?.endDateTimeUtc,
                startDateTimeUtc = mission.data?.startDateTimeUtc!!
            )
            return Mission2(
                id = mission.id,
                status = status,
                idUUID = mission.idUUID?.toString(),
                completenessForStats = completenessForStats,
                data = MissionData.fromMissionEntity(mission.data),
                isCompleteForStats = completenessForStats.sources?.isEmpty(),
                generalInfos = MissionGeneralInfo2.fromMissionGeneralInfoEntity(
                    generalInfo2 = mission.generalInfos,
                    isUnderJdp = mission.data.isUnderJdp
                ),
                actions = mission.actions?.map { action -> MissionAction.fromMissionActionEntity(action) } ?: listOf()
            )
        }
    }
}
