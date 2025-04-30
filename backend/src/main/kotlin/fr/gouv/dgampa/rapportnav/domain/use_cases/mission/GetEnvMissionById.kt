package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.ExtendedEnvMissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.AttachControlsToActionControl
import io.sentry.Sentry
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.MultiPoint
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable

fun createMockMultiPoint(coordinates: List<Coordinate>): MultiPoint {
    val factory = GeometryFactory()
    val points = coordinates.map { factory.createPoint(it) }
    return factory.createMultiPoint(points.toTypedArray())
}

@UseCase
class GetEnvMissionById(
    private val monitorEnvApiRepo: IEnvMissionRepository,
    private val attachControlsToActionControl: AttachControlsToActionControl,
    private val getEnvMissions: GetEnvMissions,
    private val getFakeActionData: FakeActionData
) {
    private val logger = LoggerFactory.getLogger(GetEnvMissionById::class.java)

    fun getMissionWithControls(envMission: MissionEntity? = null): ExtendedEnvMissionEntity? {
        if (envMission == null) {
            return null
        }
        var mission = ExtendedEnvMissionEntity.fromEnvMission(envMission)

        // need to attach nav controls to calculate correctly the completeness
        mission.actions?.map {
            if (it?.controlAction != null) {
                val actionWithControls = attachControlsToActionControl.toEnvAction(
                    actionId = it.controlAction.action?.id.toString(),
                    action = it
                )
                // recompute completeness once extra data has been attached
                actionWithControls.controlAction?.computeCompleteness()
                ExtendedEnvActionEntity(
                    controlAction = actionWithControls.controlAction
                )
            } else {
                it?.surveillanceAction?.computeCompleteness()
                ExtendedEnvActionEntity(
                    surveillanceAction = it?.surveillanceAction
                )
            }
        }
        return mission
    }


    /**
     * Executes the process of attaching additional data to a mission coming from MonitorEnv
     * If the mission is given as input, it will use it, otherwise it will fetch it from MonitorEnv.
     * Some extra data is attached such as Controls, Infractions, a status for completeness...
     *
     *
     * @param missionId
     * @param inputEnvMission a MonitorEnv Mission
     * @return A fully constructed MissionEntity containing extra data coming from RapportNav.
     */
    @Cacheable(value = ["envMission"], key = "#missionId")
    fun execute(missionId: Int, inputEnvMission: MissionEntity? = null): ExtendedEnvMissionEntity? {

        try {
            val envMission: MissionEntity? = inputEnvMission ?: monitorEnvApiRepo.findMissionById(missionId.toString())
            var mission = getMissionWithControls(envMission)
            return mission
        } catch (e: Exception) {
            logger.error("GetEnvMissionById failed loading EnvMission", e)
            Sentry.captureMessage("GetEnvMissionById failed loading EnvMission")
            Sentry.captureException(e)
            return null
//            var envMission = getEnvMissions.mockedMissions.find { missionId == it.id }!!
//            envMission.envActions = getFakeActionData.getFakeEnvControls() + getFakeActionData.getFakeEnvSurveillance()
//            var mission = getMissionWithControls(envMission)
//            return mission
        }


    }
}
