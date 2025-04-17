package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatDateTime
import org.slf4j.LoggerFactory

@UseCase
class ExportMissionAEMCombined(
    private val formatDateTime: FormatDateTime,
    private val exportMissionAEMSingle: ExportMissionAEMSingle,
    private val getMissionById: GetMission,
) {

    private val logger = LoggerFactory.getLogger(ExportMissionAEMCombined::class.java)


    /**
     * Returns a merged Rapport de Patrouille
     * Taking several missions and combining them into one
     *
     * @param missionIds a list of Mission Ids
     * @return a MissionExportEntity with file name and content
     */
    fun execute(missionIds: List<String>): MissionExportEntity? {

        try {

            // retrieve missions
            var missions = mutableListOf<MissionEntity>()

            for (missionId in missionIds) {
                val mission = getMissionById.execute(missionId)
                if (mission != null) {
                    missions.add(mission)
                }
            }

            // bundle actions and other stuff
            val firstMission = missions.first() // Take all other fields from the first mission
            val combinedActions = missions.flatMap { it.actions.orEmpty() } // Aggregate all actions from all missions
            val mission =
                firstMission.copy(actions = MissionEntity.sortActions(combinedActions)) // Create a new instance with aggregated actions

            // create file
            val output = exportMissionAEMSingle.createFile(mission = mission)


            return MissionExportEntity(
                fileName = "tableaux-AEM-combinés_${formatDateTime.formatDate(missions.first().startDateTimeUtc)}.ods",
                fileContent = output?.fileContent.orEmpty()
            )

        } catch (e: Exception) {
            logger.error("[AEM] - error while generating report : ${e.message}")
            return null
        }
    }

}
