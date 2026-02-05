package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatDateTime

@UseCase
class ExportMissionAEMCombined(
    private val formatDateTime: FormatDateTime,
    private val exportMissionAEMSingle: ExportMissionAEMSingle,
    private val getComputeEnvMission: GetComputeEnvMission,
) {

    /**
     * Returns a merged Rapport de Patrouille
     * Taking several missions and combining them into one
     *
     * @param missionIds a list of Mission Ids
     * @return a MissionExportEntity with file name and content
     */
    fun execute(missionIds: List<Int>): MissionExportEntity {
        try {

            // retrieve missions
            var missions = mutableListOf<MissionEntity>()

            for (missionId in missionIds) {
                val mission = getComputeEnvMission.execute(missionId = missionId)
                if (mission != null) {
                    missions.add(mission)
                }
            }

            // bundle actions and other stuff
            val firstMission = missions.first() // Take all other fields from the first mission
            val combinedActions = missions.flatMap { it.actions.orEmpty() } // Aggregate all actions from all missions
            val mission =
                firstMission.copy(actions = combinedActions.sortedByDescending { it?.startDateTimeUtc }) // Create a new instance with aggregated actions

            // create file
            val output = exportMissionAEMSingle.createFile(mission = mission)


            return MissionExportEntity(
                fileName = "tableaux-AEM-combinés_${formatDateTime.formatDate(missions.first().data?.startDateTimeUtc)}.ods",
                fileContent = output?.fileContent.orEmpty()
            )

        } catch (e: BackendUsageException) {
            throw e
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to create combined AEM report",
                originalException = e
            )
        }
    }

}
