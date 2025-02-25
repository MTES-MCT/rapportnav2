package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnv
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.APIEnvMissionRepositoryV2
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Caching

@UseCase
class UpdateMissionEnv(
    private val getEnvMissionById2: GetEnvMissionById2,
    private val apiEnvRepo2: APIEnvMissionRepositoryV2

) {

    private val logger = LoggerFactory.getLogger(UpdateMissionEnv::class.java)

    @Caching(
        evict = [
            CacheEvict(value = ["envMission"], key = "#input.missionId"),
            CacheEvict(value = ["envMission2"], key = "#input.missionId"),
        ]
    )
    fun execute(mission: MissionEnvEntity): MissionEnvEntity? {
        val fromDbEnvMission = getEnvMissionById2.execute(mission.id) ?: return null
        val missionEntity = MissionEntity.fromMissionEnvEntity(mission)

        if (missionEntity === fromDbEnvMission) return null

        val missionRecomp = MissionEnv(
            id = fromDbEnvMission.id,
            missionSource = fromDbEnvMission.missionSource,
            isUnderJdp = fromDbEnvMission.isUnderJdp,
            isGeometryComputedFromControls = fromDbEnvMission.isGeometryComputedFromControls,
            hasMissionOrder = fromDbEnvMission.hasMissionOrder,
            observationsCacem = fromDbEnvMission.observationsCacem,
            observationsCnsp = fromDbEnvMission.observationsCnsp,
            facade = fromDbEnvMission.facade,
            geom = fromDbEnvMission.geom,
            completedBy = fromDbEnvMission.completedBy,
            openBy = fromDbEnvMission.openBy,
            controlUnits = fromDbEnvMission.controlUnits,
            startDateTimeUtc = mission.startDateTimeUtc,
            endDateTimeUtc = mission.endDateTimeUtc,
            observationsByUnit = mission.observationsByUnit,
            missionTypes = mission.missionTypes,
            )

        return try {
            apiEnvRepo2.update(missionRecomp)
        } catch (e: Exception) {
            logger.error("Update Mission failed", e)
            return null
        }
    }
}
