package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.ExportModeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.ExportReportTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionExternalId
import org.slf4j.LoggerFactory
import java.util.UUID

@UseCase
class ExportMissionReports(
    private val exportMissionPatrolSingle: ExportMissionPatrolSingle,
    private val exportMissionPatrolCombined: ExportMissionPatrolCombined,
    private val exportMissionPatrolMultipleZipped: ExportMissionPatrolMultipleZipped,
    private val exportMissionAEMSingle: ExportMissionAEMSingle,
    private val exportMissionAEMCombined: ExportMissionAEMCombined,
    private val exportMissionAEMMultipleZipped: ExportMissionAEMMultipleZipped,
    private val getMissionExternalId: GetMissionExternalId,
) {

    private val logger = LoggerFactory.getLogger(ExportMissionReports::class.java)

    fun execute(
        missionIds: List<UUID>,
        exportMode: ExportModeEnum,
        reportType: ExportReportTypeEnum
    ): MissionExportEntity {
        if (missionIds.isEmpty()) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "No mission IDs provided for export"
            )
        }

        // Downstream export use cases (and the /analytics entry point) work with the
        // MonitorEnv externalId (monitorEnvId), so resolve the local mission table ids
        // (MissionModel UUIDs) into their externalId before dispatching. Nav-only missions
        // (no externalId) aren't supported by the MonitorEnv-based exporters, so skip them
        // instead of aborting the whole export when one is included in the selection.
        val externalIds = missionIds.mapNotNull { missionId ->
            val externalId = getMissionExternalId.execute(missionId)
            if (externalId == null) {
                logger.warn("ExportMissionReports: skipping nav-only mission $missionId (no MonitorEnv externalId)")
            }
            externalId
        }

        if (externalIds.isEmpty()) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "None of the selected missions can be exported (no MonitorEnv externalId)"
            )
        }

        return when (reportType) {
            ExportReportTypeEnum.AEM -> when (exportMode) {
                ExportModeEnum.INDIVIDUAL_MISSION -> {
                    logger.info("ExportMissionAEM - running export INDIVIDUAL_MISSION")
                    exportMissionAEMSingle.execute(externalIds.first())
                }
                ExportModeEnum.COMBINED_MISSIONS_IN_ONE -> {
                    logger.info("ExportMissionAEM - running export COMBINED_MISSIONS_IN_ONE")
                    exportMissionAEMCombined.execute(externalIds)
                }
                ExportModeEnum.MULTIPLE_MISSIONS_ZIPPED -> {
                    logger.info("ExportMissionAEM - running export MULTIPLE_MISSIONS_ZIPPED")
                    exportMissionAEMMultipleZipped.execute(externalIds)
                }
            }

            ExportReportTypeEnum.PATROL -> when (exportMode) {
                ExportModeEnum.INDIVIDUAL_MISSION -> {
                    logger.info("ExportMissionPatrol - running export INDIVIDUAL_MISSION")
                    exportMissionPatrolSingle.execute(externalIds.first())
                }
                ExportModeEnum.COMBINED_MISSIONS_IN_ONE -> {
                    logger.info("ExportMissionPatrol - running export COMBINED_MISSIONS_IN_ONE")
                    exportMissionPatrolCombined.execute(externalIds)
                }
                ExportModeEnum.MULTIPLE_MISSIONS_ZIPPED -> {
                    logger.info("ExportMissionPatrol - running export MULTIPLE_MISSIONS_ZIPPED")
                    exportMissionPatrolMultipleZipped.execute(externalIds)
                }
            }

            ExportReportTypeEnum.ALL -> throw BackendInternalException(
                message = "Export type 'ALL' is not yet implemented"
            )
        }
    }
}
