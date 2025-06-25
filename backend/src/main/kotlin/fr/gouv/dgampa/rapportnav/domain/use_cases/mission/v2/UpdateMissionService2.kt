package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetActiveCrewForService
import org.slf4j.LoggerFactory
import java.util.*

@UseCase
class UpdateMissionService2(
    private val processMissionCrew: ProcessMissionCrew,
    private val getActiveCrewForService: GetActiveCrewForService,
) {
    private val logger = LoggerFactory.getLogger(UpdateMissionService2::class.java)

    fun execute(serviceId: Int, missionId: Int): List<MissionCrewEntity> {
        return try {
            val crew = getMissionCrew(serviceId = serviceId, missionId = missionId)
            processMissionCrew.execute(missionId = missionId, crew = crew)
        }
        catch (e: Exception) {
            logger.error("UpdateMissionService2 - failed to update crew after service changed", e)
            emptyList()
        }
    }

    fun execute(serviceId: Int, missionIdUUID: UUID): List<MissionCrewEntity> {
        return try {
            val crew = getMissionCrew(serviceId = serviceId, missionIdUUID = missionIdUUID)
            processMissionCrew.execute(missionIdUUID = missionIdUUID, crew = crew)
        } catch (e: Exception) {
            logger.error("UpdateMissionService2 - failed to update crew after service changed", e)
            emptyList()
        }
    }

    private fun getMissionCrew(
        serviceId: Int,
        missionId: Int? = null,
        missionIdUUID: UUID? = null
    ): List<MissionCrewEntity> {
        val crewFromNewService: List<AgentServiceEntity> = getActiveCrewForService.execute(serviceId = serviceId)
        return crewFromNewService.map {
            MissionCrewEntity(
                role = it.role,
                agent = it.agent,
                missionId = missionId,
                missionIdUUID = missionIdUUID,
            )
        }
    }
}
