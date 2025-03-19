package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMission2
import org.slf4j.LoggerFactory

@UseCase
class ExportMissionPatrolMultipleZipped2(
    private val exportMissionPatrolSingle: ExportMissionPatrolSingle2,
    private val getMission2: GetMission2,
    private val zipFiles: ZipFiles,
) {

    private val logger = LoggerFactory.getLogger(ExportMissionPatrolMultipleZipped2::class.java)

    /**
     * Returns a zip with several Rapports de Patrouille
     * There will be one file per complete mission
     *
     * @param missionIds a list of Mission Ids
     * @return a MissionExportEntity with file name and content
     */
    fun execute(missionIds: List<Int>): MissionExportEntity? {
        if (missionIds.isEmpty()) return null

        var mission: MissionEntity2?
        val filesToZip = mutableListOf<MissionExportEntity>()

        // retrieve missions
        for (missionId in missionIds) {
            try {
                mission = getMission2.execute(missionId = missionId)
            } catch (e: Exception) {
                logger.error("[ExportMissionPatrolMultipleZipped] - error retrieving mission id=$missionId", e)
                return null
            }

            try {

                // only keep complete missions
                if (mission != null && mission.isCompleteForStats().status === CompletenessForStatsStatusEnum.COMPLETE) {
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
        var output = zipFiles.execute(filesToZip)

        return MissionExportEntity(
            fileName = "rapports-patrouille.zip",
            fileContent = output
        )


    }

}
