package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMission
import org.slf4j.LoggerFactory

@UseCase
class ExportMissionAEMMultipleZipped(
    private val exportMissionAEMSingle: ExportMissionAEMSingle,
    private val getMissionById: GetMission,
    private val zipFiles: ZipFiles,
) {

    private val logger = LoggerFactory.getLogger(ExportMissionAEMMultipleZipped::class.java)


    /**
     * Returns a zip with several Rapports de Patrouille
     * There will be one file per complete mission
     *
     * @param missionIds a list of Mission Ids
     * @return a MissionExportEntity with file name and content
     */
    fun execute(missionIds: List<String>): MissionExportEntity? {
        try {
            val filesToZip = mutableListOf<MissionExportEntity>();

            // retrieve missions
            for (missionId in missionIds) {
                val mission = getMissionById.execute(missionId)

                // only keep complete missions
                if (mission != null && mission.completenessForStats?.status === CompletenessForStatsStatusEnum.COMPLETE) {
                    val output = exportMissionAEMSingle.createFile(mission = mission)
                    output?.let { filesToZip.add(it) }

                } else {
                    logger.info("ExportMissionAEMMultipleZipped - ignoring mission id=${mission?.id} because incomplete for stats")
                }
            }

            // zip all files together
            val output = zipFiles.execute(filesToZip)

            return MissionExportEntity(
                fileName = "tableaux-AEM.zip",
                fileContent = output
            )

        } catch (e: Exception) {
            logger.error("[ExportMissionAEMMultipleZipped] - error building zipped mission export ${e.message}")
            return null
        }
    }

}
