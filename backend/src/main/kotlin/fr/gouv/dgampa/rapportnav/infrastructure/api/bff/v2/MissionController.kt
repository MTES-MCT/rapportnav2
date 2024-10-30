package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.ExportMissionRapportPatrouille
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.ExportMissionAEM
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.AddOrUpdateMissionGeneralInfo
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetControlUnitsForUser
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserFromToken
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.MissionsFetchEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.Mission
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller


@Controller
class MissionController(
    private val getUserFromToken: GetUserFromToken,
    private val getEnvMissions: GetEnvMissions,
    private val getMission: GetMission,
    private val getControlUnitsForUser: GetControlUnitsForUser,
    private val fakeMissionData: FakeMissionData,
) {

    private val logger = LoggerFactory.getLogger(MissionController::class.java)


    /**
     * Retrieves a list of missions from the environment and adds fictive missions specific to the user.
     *
     * This function queries missions within a date range defined in the `MissionsFetchEnvInput` parameters, specifically `startDateTimeUtc` and `endDateTimeUtc`.
     * It filters missions to include only those matching the user's control units and combines them with fictive missions for a comprehensive list.
     *
     * @param envInput An instance of `MissionsFetchEnvInput` containing the filter criteria, including `startDateTimeUtc` and `endDateTimeUtc`.
     * @return A list of missions that includes both retrieved and fictive missions for the user, or null if an exception occurs.
     * @throws Exception if there is an error during the mission retrieval process.
     */
    @QueryMapping
    fun missionsV2(@Argument envInput: MissionsFetchEnvInput): List<Mission>? {
        try {
            // query MonitorEnv with the following filters
            val envMissions = getEnvMissions.execute(
                startedAfterDateTime = envInput.startDateTimeUtc,
                startedBeforeDateTime = envInput.endDateTimeUtc,
                pageNumber = null,
                pageSize = null,
                controlUnits = getControlUnitsForUser.execute()
            )

            // reconstruct the Missions with full data in order to have the right status/completeness
            val fullMissions = envMissions?.map { getMission.execute(it.id, it) }

            // transform the data for the API
            val missions = fullMissions?.mapNotNull { it?.let { Mission.fromMissionEntity(it) } } ?: emptyList()


            // temporarily add fictive missions
            val user = getUserFromToken.execute()
            val fakeMissions = fakeMissionData.getFakeMissionsforUser(user).map { Mission.fromMissionEntity(it) }

            return missions + fakeMissions
        } catch (e: Exception) {
            logger.error("MissionController (ULAM v2) - failed to load missions from MonitorEnv", e)
            throw Exception(e)
        }
    }
}
