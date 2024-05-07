package fr.gouv.dgampa.rapportnav.domain.repositories.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ControlPlansEntity
import java.time.ZonedDateTime

interface IEnvMissionRepository {

    fun findMissionById(missionId: Int): MissionEntity?
    fun findAllMissions(
        pageNumber: Int? = null,
        pageSize: Int? = null,
        startedAfterDateTime: ZonedDateTime? = null,
        startedBeforeDateTime: ZonedDateTime? = null,
        missionSources: List<String>? = null,
        missionTypes: List<String>? = null,
        missionStatuses: List<String>? = null,
        seaFronts: List<String>? = null,
        controlUnits: List<Int>? = null
    ): List<MissionEntity>?

    fun findAllControlPlans(): ControlPlansEntity?

}
