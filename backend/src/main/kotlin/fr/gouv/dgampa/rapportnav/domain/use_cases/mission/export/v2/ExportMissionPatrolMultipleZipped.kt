package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMission
import org.slf4j.LoggerFactory
import java.io.File

@UseCase
class ExportMissionPatrolMultipleZipped(
    private val exportMissionPatrolSingle: ExportMissionPatrolSingle,
    private val getMissionById: GetMission,
    private val zipFiles: ZipFiles,
) {

    private val logger = LoggerFactory.getLogger(ExportMissionPatrolMultipleZipped::class.java)

    /**
     * Returns a zip with several Rapports de Patrouille
     * There will be one file per complete mission
     *
     * @param missionIds a list of Mission Ids
     * @return a MissionExportEntity with file name and content
     */
    fun execute(missionIds: List<Int>): MissionExportEntity? {
        try {
            if (missionIds.isEmpty()) return null

            val filesToZip = mutableListOf<File>();

            // retrieve missions
            for (missionId in missionIds) {
                val mission = getMissionById.execute(missionId)

                // only keep complete missions
                if (mission != null && mission.completenessForStats?.status === CompletenessForStatsStatusEnum.COMPLETE) {
                    val output = exportMissionPatrolSingle.createFile(mission = mission)
                    filesToZip.add(File(output?.fileContent.orEmpty()))

                }
            }

            // zip all files together
            val output = zipFiles.execute(filesToZip)

            return MissionExportEntity(
                fileName = "rapports-patrouille.zip",
                fileContent = output
            )

        } catch (e: Exception) {
            logger.error("[ExportMissionPatrolMultipleZipped] - error building zipped mission patrol export ${e.message}")
            return null
        }
    }

}
