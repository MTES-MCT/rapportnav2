package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.ExtendedEnvMissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.NavMissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedFishActionEntity
import org.slf4j.LoggerFactory
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity as EnvMissionEntity

@UseCase
class GetMission(
    private val getEnvMissionById: GetEnvMissionById,
    private val getNavMissionById: GetNavMissionById,
    private val getFishActionsByMissionId: GetFishActionsByMissionId,
) {
    private val logger = LoggerFactory.getLogger(GetMission::class.java)

    /**
     * Executes the process of attaching additional data to a mission coming from MonitorEnv
     * There are cases, especially for accurate completeness status of objects for stats purposes,
     * where the mission coming from MonitorEnv is not sufficient.
     * Therefore, some additional data coming from RapportNav needs to be added (controls, infractions...)
     * in order to have an accurate vision of what's going on.
     * This function takes a Mission coming from MonitorEnv, attach Nav data to it, fetch all Fish and Nav actions.
     *
     *
     * @param missionId a required Mission id
     * @param envMission an optional MonitorEnv Mission to reuse instead of fetching
     * @return A fully constructed MissionEntity containing the extended environmental mission,
     *         fish mission actions, and nav mission data.
     */
    fun execute(missionId: String?, envMission: EnvMissionEntity? = null): MissionEntity? {
        require(missionId != null) { "GetMission - missionId cannot be null" }

        val envMissionWithExtraData: ExtendedEnvMissionEntity = requireNotNull(
            getEnvMissionById.execute(missionId = missionId, inputEnvMission = envMission)
        ) { "GetMission - ExtendedEnvMissionEntity cannot be null" }

        val fishActionsWithExtraData: List<ExtendedFishActionEntity> = getFishActionsByMissionId.execute(
            missionId = missionId
        )

        val navMission: NavMissionEntity =
            getNavMissionById.execute(
                missionId = missionId,
                controlUnits = envMissionWithExtraData.mission.controlUnits
            )

        return MissionEntity(
            envMission = envMissionWithExtraData,
            fishMissionActions = fishActionsWithExtraData,
            navMission = navMission
        )
    }

}
