package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatDateTime
import org.slf4j.LoggerFactory

@UseCase
class ExportMissionPatrolCombined(
    private val formatDateTime: FormatDateTime,
    private val exportMissionPatrolSingle: ExportMissionPatrolSingle,
    private val getMission: GetMission,
) {

    private val logger = LoggerFactory.getLogger(ExportMissionPatrolCombined::class.java)


    /**
     * Returns a merged Rapport de Patrouille
     * Taking several missions and combining them into one
     *
     * @param missionIds a list of Mission Ids
     * @return a MissionExportEntity with file name and content
     */
    fun execute(missionIds: List<Int>): MissionExportEntity? {
        try {
            if (missionIds.isEmpty()) return null

            // retrieve missions
            var missions = mutableListOf<MissionEntity>()

            for (missionId in missionIds) {
                val mission = getMission.execute(missionId)
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
            val output = exportMissionPatrolSingle.createFile(mission = mission)

            return MissionExportEntity(
                fileName = "rapports-patrouille-combinés_${formatDateTime.formatDate(mission.startDateTimeUtc)}.odt",
                fileContent = output?.fileContent.orEmpty()
            )

        } catch (e: Exception) {
            logger.error("[RapportDePatrouille] - Error while generating report : ${e.message}")
            return null
        }
    }

}