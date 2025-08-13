package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetEnvMissions
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetControlUnitsForUser
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserFromToken
import org.slf4j.LoggerFactory
import java.time.Instant

@UseCase
class GetMissions(
    private val getEnvMissions: GetEnvMissions,
    private val getNavMissions: GetNavMissions,
    private val getComputeEnvMission: GetComputeEnvMission,
    private val getComputeNavMission: GetComputeNavMission,
    private val getControlUnitsForUser: GetControlUnitsForUser,
    private val getUserFromToken: GetUserFromToken,
) {
    private val logger = LoggerFactory.getLogger(GetMissions::class.java)

    fun execute(startDateTimeUtc: Instant, endDateTimeUtc: Instant? = null): List<MissionEntity2?> {
        val user: User? = getUserFromToken.execute()
        val envEntities: List<MissionEntity>?  = getEnvMissions.execute(
            startedAfterDateTime = startDateTimeUtc,
            startedBeforeDateTime = endDateTimeUtc,
            pageNumber = null,
            pageSize = null,
            controlUnits = getControlUnitsForUser.execute()
        )
        val navEntities = getNavMissions.execute(
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            serviceId = user?.serviceId,
        )

        val envMissions = envEntities?.map { getComputeEnvMission.execute(envMission = it) }.orEmpty()
        val navMissions = navEntities?.map { getComputeNavMission.execute(navMission = it) }.orEmpty()

        return(envMissions + navMissions).sortedByDescending { it?.data?.startDateTimeUtc }

    }
}
