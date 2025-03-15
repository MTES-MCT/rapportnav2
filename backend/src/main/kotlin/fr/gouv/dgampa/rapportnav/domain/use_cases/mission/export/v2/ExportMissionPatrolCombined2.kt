package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMission2
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatDateTime
import org.slf4j.LoggerFactory

@UseCase
class ExportMissionPatrolCombined2(
    private val formatDateTime: FormatDateTime,
    private val exportMissionPatrolSingle: ExportMissionPatrolSingle2,
    private val getEnvMissionById2: GetEnvMissionById2,
    private val getMission2: GetMission2,
) {

    private val logger = LoggerFactory.getLogger(ExportMissionPatrolCombined2::class.java)


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
            var missions = mutableListOf<MissionEntity2>()

            for (missionId in missionIds) {
                val envMission: MissionEntity? = getEnvMissionById2.execute(missionId)

                if (envMission == null) {
                    logger.error("[RapportDePatrouille] - Mission not found for missionId: $missionId")
                    return null
                }
                val mission = getMission2.execute(envMission)
                if (mission != null) {
                    missions.add(mission)
                }
            }

            // bundle actions and other stuff
            val firstMission = missions.first() // Take all other fields from the first mission
            val combinedActions = missions.flatMap { it.actions } // Aggregate all actions from all missions
            val mission =
                firstMission.copy(actions = combinedActions.sortedByDescending { action -> action.startDateTimeUtc }) // Create a new instance with aggregated actions

            // create file
            val output = exportMissionPatrolSingle.createFile(mission = mission)

            return MissionExportEntity(
                fileName = "rapports-patrouille-combinés_${formatDateTime.formatDate(mission.envData.startDateTimeUtc)}.odt",
                fileContent = output?.fileContent.orEmpty()
            )

        } catch (e: Exception) {
            logger.error("[RapportDePatrouille] - Error while generating report : ${e.message}")
            return null
        }
    }

}
