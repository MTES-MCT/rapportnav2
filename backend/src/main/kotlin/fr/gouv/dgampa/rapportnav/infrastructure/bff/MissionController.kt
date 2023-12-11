package fr.gouv.dgampa.rapportnav.infrastructure.bff

import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.GeneralInfo.AddOrUpdateMissionGeneralInfo
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.GeneralInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.crew.GetServiceById
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserFromToken
import fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.generalInfo.MissionGeneralInfoInput
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.Mission
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.generalInfo.MissionGeneralInfo
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller


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
    private val getServiceById: GetServiceById,
) {

    @QueryMapping
    fun missions(): List<Mission> {

        // get which controlUnit is the user linked to
        val user = getUserFromToken.execute()
        val service = user?.serviceId?.let { serviceId ->
            getServiceById.execute(user.serviceId)
        }

        // query with the following filters
        return getEnvMissions.execute(
            startedAfterDateTime = null,
            startedBeforeDateTime = null,
            pageNumber = null,
            pageSize = null,
            controlUnits = service?.controlUnits
        ).map { Mission.fromMissionEntity(it) }
    }

    @QueryMapping
    fun mission(@Argument missionId: Int): Mission {
        val envMission = getEnvMissionById.execute(missionId = missionId)
        val fishMissionActions = getFishActionsByMissionId.execute(missionId = missionId)
        val navMission = getNavMissionById.execute(missionId = missionId)

        return Mission.fromMissionEntity(MissionEntity(envMission, navMission, fishMissionActions))
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
