package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
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
        if (missionIds.isEmpty()) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "No mission IDs provided for combined AEM export"
            )
        }

        val missions = missionIds.map { getComputeEnvMission.execute(missionId = it) }

        val firstMission = missions.first()
        val combinedActions = missions.flatMap { it.actions.orEmpty() }
        val mission = firstMission.copy(
            actions = combinedActions.sortedByDescending { it.startDateTimeUtc }
        )

        val output = exportMissionAEMSingle.createFile(mission)

        return MissionExportEntity(
            fileName = "tableaux-AEM-combinés_${formatDateTime.formatDate(firstMission.data?.startDateTimeUtc)}.ods",
            fileContent = output?.fileContent.orEmpty()
        )
    }
}
