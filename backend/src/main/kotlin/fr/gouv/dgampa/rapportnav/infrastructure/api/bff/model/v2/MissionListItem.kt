package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.JdpTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReportTypeEnum
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.MissionCrew
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.LegacyControlUnitResource
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import java.time.Instant

/**
 * Light projection of a mission for the **list** page (ULAM + PAM). Carries only the fields the frontend
 * list actually renders — see `use-mission-list.tsx#getMissionListItem` — so the list endpoint no longer
 * ships the full [Mission] payload (all actions, full general info, geometry, ...).
 *
 * Notably it exposes `actionCount` instead of the action list (the list only uses the count, for the PAM
 * export-dialog label). Everything else — including `completenessForStats` — is produced exactly as the full
 * [Mission] response, so the only difference the frontend sees is a smaller payload.
 */
data class MissionListItem(
    val id: Int? = null,
    val idUUID: String? = null,
    val status: MissionStatusEnum,
    val startDateTimeUtc: Instant? = null,
    val endDateTimeUtc: Instant? = null,
    val missionSource: MissionSourceEnum? = null,
    val openBy: String? = null,
    val observationsByUnit: String? = null,
    val isUnderJdp: Boolean? = false,
    val controlUnits: List<LegacyControlUnitEntity> = listOf(),
    val completenessForStats: CompletenessForStatsEntity? = null,
    val crew: List<MissionCrew>? = null,
    val serviceId: Int? = null,
    val resources: List<LegacyControlUnitResource>? = listOf(),
    val missionReportType: MissionReportTypeEnum? = null,
    val jdpType: JdpTypeEnum? = null,
    val isResourcesNotUsed: Boolean? = null,
    val actionCount: Int = 0,
) {
    companion object {
        /**
         * Builds the light list item from a fully-computed [MissionEntity] (same compute path as the detail
         * read), so:
         *  - `completenessForStats` is computed on the fly via [MissionEntity.isCompleteForStats] — identical
         *    to the full [Mission] response (real VALID/INVALID/INCOMPLETE status + sources),
         *  - `actionCount` is the size of the loaded action list.
         *
         * The mission's [MissionData] / [MissionGeneralInfo2] sub-DTOs are reused as the single source of
         * truth for field extraction, so every value here is identical to the full [Mission] response.
         */
        fun fromMissionEntity(mission: MissionEntity): MissionListItem {
            val data = mission.data?.let { MissionData.fromMissionEntity(it) }
            val generalInfos = MissionGeneralInfo2.fromMissionGeneralInfoEntity(
                generalInfo2 = mission.generalInfos,
                isUnderJdp = mission.data?.isUnderJdp
            )
            val status = mission.calculateMissionStatus(
                endDateTimeUtc = mission.data?.endDateTimeUtc,
                startDateTimeUtc = mission.data?.startDateTimeUtc!!
            )
            return MissionListItem(
                id = mission.id,
                idUUID = mission.idUUID?.toString(),
                status = status,
                startDateTimeUtc = data?.startDateTimeUtc,
                endDateTimeUtc = data?.endDateTimeUtc,
                missionSource = data?.missionSource,
                openBy = data?.openBy,
                observationsByUnit = data?.observationsByUnit,
                isUnderJdp = data?.isUnderJdp,
                controlUnits = data?.controlUnits ?: listOf(),
                completenessForStats = mission.isCompleteForStats(),
                crew = generalInfos.crew,
                serviceId = generalInfos.service?.id,
                resources = generalInfos.resources,
                missionReportType = generalInfos.missionReportType,
                jdpType = generalInfos.jdpType,
                isResourcesNotUsed = generalInfos.isResourcesNotUsed,
                actionCount = mission.actions?.size ?: 0,
            )
        }
    }
}
