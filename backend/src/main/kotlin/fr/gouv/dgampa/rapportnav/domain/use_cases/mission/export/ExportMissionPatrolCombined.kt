package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatDateTime

@UseCase
class ExportMissionPatrolCombined(
    private val formatDateTime: FormatDateTime,
    private val exportMissionPatrolSingle: ExportMissionPatrolSingle,
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
        if (missionIds.isEmpty()) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "No mission IDs provided for combined export"
            )
        }

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
            val combinedActions = missions.flatMap { it.actions!! } // Aggregate all actions from all missions
            val mission =
                firstMission.copy(actions = combinedActions.sortedByDescending { action -> action.startDateTimeUtc }) // Create a new instance with aggregated actions

            // create file
            val output = exportMissionPatrolSingle.createFile(mission = mission)

            return MissionExportEntity(
                fileName = "rapports-patrouille-combinés_${formatDateTime.formatDate(mission.data?.startDateTimeUtc)}.odt",
                fileContent = output?.fileContent.orEmpty()
            )

        } catch (e: BackendUsageException) {
            throw e
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to create combined patrol report",
                originalException = e
            )
        }
    }

}
