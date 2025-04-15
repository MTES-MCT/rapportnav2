package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetEnvMissions
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetControlUnitsForUser
import org.slf4j.LoggerFactory
import java.time.Instant

@UseCase
class GetMissions(
    private val getEnvMissions: GetEnvMissions,
    private val getNavMissions: GetNavMissions,
    private val getMission2: GetMission2,
    private val getControlUnitsForUser: GetControlUnitsForUser
) {

    private val logger = LoggerFactory.getLogger(GetMissions::class.java)

    fun execute(startDateTimeUtc: Instant, endDateTimeUtc: Instant? = null): List<MissionEntity2?> {

        var envMissions: List<MissionEntity>?  = getEnvMissions.execute(
            startedAfterDateTime = startDateTimeUtc,
            startedBeforeDateTime = endDateTimeUtc,
            pageNumber = null,
            pageSize = null,
            controlUnits = getControlUnitsForUser.execute()
        )

        val navMissions = getNavMissions.execute(
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc
        )

        val envMissionsEntity2 = envMissions?.map { getMission2.execute(envMission = it) }.orEmpty()

        val navMissionsEntity2 = navMissions?.map { getMission2.execute(envMission = it) }.orEmpty()

        return(envMissionsEntity2 + navMissionsEntity2).sortedByDescending { it?.data?.startDateTimeUtc }

    }
}
