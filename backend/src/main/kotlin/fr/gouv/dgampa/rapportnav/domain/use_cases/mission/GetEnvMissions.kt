package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import org.springframework.cache.annotation.Cacheable
import java.time.Instant


@UseCase
class GetEnvMissions(
    private val monitorEnvApiRepo: IEnvMissionRepository,
) {

    @Cacheable(value = ["envMissions"])
    fun execute(
        startedAfterDateTime: Instant? = null,
        startedBeforeDateTime: Instant? = null,
        pageNumber: Int? = null,
        pageSize: Int? = null,
        controlUnits: List<Int>? = null
    ): List<MissionEntity>? {
        val envMissions: List<MissionEntity>? =
            monitorEnvApiRepo.findAllMissions(
                startedAfterDateTime = startedAfterDateTime,
                startedBeforeDateTime = startedBeforeDateTime,
                pageNumber = pageNumber,
                pageSize = pageSize,
                controlUnits = controlUnits,
                missionSources = listOf(MissionSourceEnum.MONITORENV, MissionSourceEnum.MONITORFISH, MissionSourceEnum.RAPPORT_NAV).map { it.toString() }
            )

        return envMissions
    }
}
