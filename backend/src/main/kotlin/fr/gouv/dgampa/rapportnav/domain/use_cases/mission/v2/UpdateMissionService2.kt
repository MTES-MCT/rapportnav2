package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetActiveCrewForService
import fr.gouv.dgampa.rapportnav.domain.utils.isValidUUID
import org.slf4j.LoggerFactory

@UseCase
class UpdateMissionService2(
    private val processMissionCrew: ProcessMissionCrew,
    private val getActiveCrewForService: GetActiveCrewForService,
) {
    private val logger = LoggerFactory.getLogger(UpdateMissionService2::class.java)

    fun execute(
        serviceId: Int,
        missionId: String
    ): Boolean? {
        return try {
            // get active agents for current service
            val crewFromNewService: List<AgentServiceEntity> = getActiveCrewForService.execute(serviceId = serviceId)


            var missionCrew: List<MissionCrewEntity>

            if (isValidUUID(missionId)) {
                missionCrew = crewFromNewService.map { MissionCrewEntity(agent = it.agent, role = it.role, missionIdString = missionId) }
            } else {
                missionCrew = crewFromNewService.map { MissionCrewEntity(agent = it.agent, role = it.role, missionId = missionId.toInt()) }
            }

            // update the crew with members from new service
            processMissionCrew.execute(missionId = missionId, crew = missionCrew)

            true
        }
        catch (e: Exception) {
            logger.error("UpdateMissionService2 - failed to update crew after service changed", e)
            false
        }
    }
}
