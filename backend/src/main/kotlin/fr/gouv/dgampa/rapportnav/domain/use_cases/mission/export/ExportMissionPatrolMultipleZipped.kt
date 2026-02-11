package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeEnvMission
import fr.gouv.dgampa.rapportnav.infrastructure.utils.ZipUtils
import org.slf4j.LoggerFactory

@UseCase
class ExportMissionPatrolMultipleZipped(
    private val exportMissionPatrolSingle: ExportMissionPatrolSingle,
    private val getComputeEnvMission: GetComputeEnvMission,
) {

    private val logger = LoggerFactory.getLogger(ExportMissionPatrolMultipleZipped::class.java)

    /**
     * Returns a zip with several Rapports de Patrouille
     * There will be one file per complete mission
     *
     * @param missionIds a list of Mission Ids
     * @return a MissionExportEntity with file name and content
     */
    fun execute(missionIds: List<Int>): MissionExportEntity {
        if (missionIds.isEmpty()) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "No mission IDs provided for zipped export"
            )
        }

        val filesToZip = mutableListOf<MissionExportEntity>()

        for (missionId in missionIds) {
            val mission = getComputeEnvMission.execute(missionId = missionId)

            if (mission.isCompleteForStats().status === CompletenessForStatsStatusEnum.COMPLETE) {
                exportMissionPatrolSingle.createFile(mission)?.let { filesToZip.add(it) }
            } else {
                logger.info("ExportMissionPatrolMultipleZipped - ignoring mission id=${mission?.id} because incomplete for stats")
            }
        }

        return MissionExportEntity(
            fileName = "rapports-patrouille.zip",
            fileContent = ZipUtils.zipToBase64(filesToZip)
        )
    }
}
