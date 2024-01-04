package fr.gouv.dgampa.rapportnav.infrastructure.bff

import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.AddOrUpdateMissionGeneralInfo
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetControlUnitsForUser
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserFromToken
import fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.generalInfo.MissionGeneralInfoInput
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.Mission
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.generalInfo.MissionGeneralInfo
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import java.time.ZoneId
import java.time.ZonedDateTime


@Controller
class MissionController(
    private val getUserFromToken: GetUserFromToken,
    private val getEnvMissions: GetEnvMissions,
    private val getNavMissionById: GetNavMissionById,
    private val getEnvMissionById: GetEnvMissionById,
    private val getFishActionsByMissionId: GetFishActionsByMissionId,
    private val updateEnvMission: UpdateEnvMission,
    private val getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId,
    private val addOrUpdateMissionGeneralInfo: AddOrUpdateMissionGeneralInfo,
    private val getControlUnitsForUser: GetControlUnitsForUser,
    private val fakeMissionData: FakeMissionData,
) {

    private val logger = LoggerFactory.getLogger(MissionController::class.java)


    @QueryMapping
    fun missions(): List<Mission>? {
        try {
            // query with the following filters
            var missions = getEnvMissions.execute(
                startedAfterDateTime = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")),
                startedBeforeDateTime = null,
                pageNumber = null,
                pageSize = null,
                controlUnits = getControlUnitsForUser.execute()
            )?.map { Mission.fromMissionEntity(it) }.orEmpty()

            // temporarily add fictive missions
            val user = getUserFromToken.execute()
            val fakeMissions = fakeMissionData.getFakeMissionsforUser(user).map { Mission.fromMissionEntity(it) }

            return missions + fakeMissions
        } catch (e: Exception) {
            logger.error("MissionController - failed to load missions from MonitorEnv", e)
            throw Exception(e)
        }
    }

    @QueryMapping
    fun mission(@Argument missionId: Int): Mission? {
        if (missionId in fakeMissionData.getEmptyMissionIds()) {
            var fakeMission = fakeMissionData.emptyMission(missionId)
            val navMission = getNavMissionById.execute(missionId = missionId)
            fakeMission.actions =
                fakeMission.actions?.plus(navMission.actions.map { MissionActionEntity.NavAction(it) })
            return Mission.fromMissionEntity(fakeMission)
        } else if (missionId in fakeMissionData.getFullMissionIds()) {
            val fakeMission = fakeMissionData.fullMission(missionId)
            val navMission = getNavMissionById.execute(missionId = missionId)
            fakeMission.actions =
                fakeMission.actions?.plus(navMission.actions.map { MissionActionEntity.NavAction(it) })
            return Mission.fromMissionEntity(fakeMission)
        } else {
            val envMission = getEnvMissionById.execute(missionId = missionId) ?: return null

            val fishMissionActions = getFishActionsByMissionId.execute(missionId = missionId)
            val navMission = getNavMissionById.execute(missionId = missionId)

            return Mission.fromMissionEntity(MissionEntity(envMission, navMission, fishMissionActions))
        }
    }

    @QueryMapping
    fun missionGeneralInfo(@Argument missionId: Int): MissionGeneralInfo? {
        val info = getMissionGeneralInfoByMissionId.execute(missionId)
            ?.let { MissionGeneralInfo.fromMissionGeneralInfoEntity(it) }
        return info
    }

    @SchemaMapping(typeName = "Mission", field = "generalInfo")
    fun missionGeneralInfoForMission(mission: Mission): MissionGeneralInfo? {
        val info = getMissionGeneralInfoByMissionId.execute(mission.id)
            ?.let { MissionGeneralInfo.fromMissionGeneralInfoEntity(it) }
        return info
    }

    @MutationMapping
    fun updateMissionGeneralInfo(@Argument info: MissionGeneralInfoInput): MissionGeneralInfo? {
        val data = info.toMissionGeneralInfo().toMissionGeneralInfoEntity()
        val savedData = addOrUpdateMissionGeneralInfo.execute(data)
        val returnData = MissionGeneralInfo.fromMissionGeneralInfoEntity(savedData)
        return returnData
    }

}
