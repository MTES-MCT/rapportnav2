package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMission
import org.slf4j.LoggerFactory

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
    fun execute(missionIds: List<String>): MissionExportEntity? {
        if (missionIds.isEmpty()) return null

        var mission: MissionEntity? = null
        val filesToZip = mutableListOf<MissionExportEntity>();
        var output: String? = null

        // retrieve missions
        for (missionId in missionIds) {
            try {
                mission = getMissionById.execute(missionId)
            } catch (e: Exception) {
                logger.error("[ExportMissionPatrolMultipleZipped] - error retrieving mission id=$missionId", e)
                return null
            }

            try {

                // only keep complete missions
                if (mission != null && mission.completenessForStats?.status === CompletenessForStatsStatusEnum.COMPLETE) {
                    val output = exportMissionPatrolSingle.createFile(mission = mission)
                    output?.let { filesToZip.add(it) }
                } else {
                    logger.info("ExportMissionPatrolMultipleZipped - ignoring mission id=${mission?.id} because incomplete for stats")
                }
            } catch (e: Exception) {
                logger.error("[ExportMissionPatrolMultipleZipped] - error building zipped mission patrol export", e)
                return null
            }

        }

        // zip all files together
        output = zipFiles.execute(filesToZip)



        return output?.let {
            MissionExportEntity(
                fileName = "rapports-patrouille.zip",
                fileContent = output
            )
        }


    }

}
