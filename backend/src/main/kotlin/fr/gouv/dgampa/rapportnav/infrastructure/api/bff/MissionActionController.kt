package fr.gouv.dgampa.rapportnav.infrastructure.api.bff

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.*
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionActionOutput
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class MissionActionController(
    private val getMission: GetMission,
    private val getNavActionById: GetNavActionById,
    private val getEnvActionById: GetEnvActionById,
    private val getFishActionById: GetFishActionById,
    private val getEnvActionByMissionId: GetEnvActionListByMissionId,
    private val getNavActionByMissionId: GetNavActionListByMissionId,
    private val getFIshListActionByMissionId: GetFishActionListByMissionId
) {
    private val logger = LoggerFactory.getLogger(MissionActionController::class.java)

    @QueryMapping
    fun actionList(@Argument missionId: Int): List<MissionActionOutput?> {
        val envActions = getEnvActionByMissionId.execute(missionId = missionId)
        val navActions = getNavActionByMissionId.execute(missionId = missionId)
        val fishActions = getFIshListActionByMissionId.execute(missionId = missionId)
        return (envActions + navActions + fishActions)
            .sortedByDescending { action -> action.startDateTimeUtc }
            .map { action -> MissionActionOutput.fromMissionActionEntity(action) }
    }

    @QueryMapping
    fun action(
        @Argument actionId: String,
        @Argument missionId: Int,
    ): MissionActionOutput? {
        val navAction = getNavActionById.execute(actionId = UUID.fromString(actionId))
        if (navAction != null) return MissionActionOutput.fromMissionActionEntity(navAction)
        val fishAction = getFishActionById.execute(missionId = missionId, actionId = actionId)
        if (fishAction != null) return MissionActionOutput.fromMissionActionEntity(fishAction)
        val envAction = getEnvActionById.execute(missionId = missionId, actionId = actionId) ?: return null
        return MissionActionOutput.fromMissionActionEntity(envAction)
    }
}
