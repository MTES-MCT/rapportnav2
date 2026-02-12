package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.ExportModeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.ExportReportTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import org.slf4j.LoggerFactory

@UseCase
class ExportMissionReports(
    private val exportMissionPatrolSingle: ExportMissionPatrolSingle,
    private val exportMissionPatrolCombined: ExportMissionPatrolCombined,
    private val exportMissionPatrolMultipleZipped: ExportMissionPatrolMultipleZipped,
    private val exportMissionAEMSingle: ExportMissionAEMSingle,
    private val exportMissionAEMCombined: ExportMissionAEMCombined,
    private val exportMissionAEMMultipleZipped: ExportMissionAEMMultipleZipped,
) {

    private val logger = LoggerFactory.getLogger(ExportMissionReports::class.java)

    fun execute(
        missionIds: List<Int>,
        exportMode: ExportModeEnum,
        reportType: ExportReportTypeEnum
    ): MissionExportEntity {
        if (missionIds.isEmpty()) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "No mission IDs provided for export"
            )
        }

        return when (reportType) {
            ExportReportTypeEnum.AEM -> when (exportMode) {
                ExportModeEnum.INDIVIDUAL_MISSION -> {
                    logger.info("ExportMissionAEM - running export INDIVIDUAL_MISSION")
                    exportMissionAEMSingle.execute(missionIds.first())
                }
                ExportModeEnum.COMBINED_MISSIONS_IN_ONE -> {
                    logger.info("ExportMissionAEM - running export COMBINED_MISSIONS_IN_ONE")
                    exportMissionAEMCombined.execute(missionIds)
                }
                ExportModeEnum.MULTIPLE_MISSIONS_ZIPPED -> {
                    logger.info("ExportMissionAEM - running export MULTIPLE_MISSIONS_ZIPPED")
                    exportMissionAEMMultipleZipped.execute(missionIds)
                }
            }

            ExportReportTypeEnum.PATROL -> when (exportMode) {
                ExportModeEnum.INDIVIDUAL_MISSION -> {
                    logger.info("ExportMissionPatrol - running export INDIVIDUAL_MISSION")
                    exportMissionPatrolSingle.execute(missionIds.first())
                }
                ExportModeEnum.COMBINED_MISSIONS_IN_ONE -> {
                    logger.info("ExportMissionPatrol - running export COMBINED_MISSIONS_IN_ONE")
                    exportMissionPatrolCombined.execute(missionIds)
                }
                ExportModeEnum.MULTIPLE_MISSIONS_ZIPPED -> {
                    logger.info("ExportMissionPatrol - running export MULTIPLE_MISSIONS_ZIPPED")
                    exportMissionPatrolMultipleZipped.execute(missionIds)
                }
            }

            ExportReportTypeEnum.ALL -> throw BackendInternalException(
                message = "Export type 'ALL' is not yet implemented"
            )
        }
    }
}
