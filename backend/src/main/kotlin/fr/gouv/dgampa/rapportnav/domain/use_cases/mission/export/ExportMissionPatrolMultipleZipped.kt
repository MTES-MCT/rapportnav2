package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeEnvMission
import org.slf4j.LoggerFactory

@UseCase
class ExportMissionPatrolMultipleZipped(
    private val exportMissionPatrolSingle: ExportMissionPatrolSingle,
    private val getComputeEnvMission: GetComputeEnvMission,
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
    fun execute(missionIds: List<Int>): MissionExportEntity {
        if (missionIds.isEmpty()) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "No mission IDs provided for zipped export"
            )
        }

        var mission: MissionEntity?
        val filesToZip = mutableListOf<MissionExportEntity>()

        // retrieve missions
        for (missionId in missionIds) {
            try {
                mission = getComputeEnvMission.execute(missionId = missionId)
            } catch (e: BackendUsageException) {
                throw e
            } catch (e: BackendInternalException) {
                throw e
            } catch (e: Exception) {
                throw BackendInternalException(
                    message = "Failed to retrieve mission id=$missionId for patrol export",
                    originalException = e
                )
            }

            try {
                // only keep complete missions
                if (mission != null && mission.isCompleteForStats().status === CompletenessForStatsStatusEnum.COMPLETE) {
                    val output = exportMissionPatrolSingle.createFile(mission = mission)
                    output?.let { filesToZip.add(it) }
                } else {
                    logger.info("ExportMissionPatrolMultipleZipped - ignoring mission id=${mission?.id} because incomplete for stats")
                }
            } catch (e: BackendUsageException) {
                throw e
            } catch (e: BackendInternalException) {
                throw e
            } catch (e: Exception) {
                throw BackendInternalException(
                    message = "Failed to create zipped patrol export",
                    originalException = e
                )
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
