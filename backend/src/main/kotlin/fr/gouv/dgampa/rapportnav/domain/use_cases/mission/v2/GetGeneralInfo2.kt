package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsByServiceId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetServiceByControlUnit
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.MissionCrew
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import org.slf4j.LoggerFactory
import java.util.*

@UseCase
class GetGeneralInfo2(
    private val createGeneralInfos: CreateGeneralInfos,
    private val getServiceByControlUnit: GetServiceByControlUnit,
    private val getAgentsCrewByMissionId: GetAgentsCrewByMissionId,
    private val getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId,
    private val getAgentsByServiceId: GetAgentsByServiceId,
) {
    private val logger = LoggerFactory.getLogger(GetGeneralInfo2::class.java)

    fun execute(
        missionId: Int,
        controlUnits: List<LegacyControlUnitEntity>? = null
    ): MissionGeneralInfoEntity2 {
        val services = fetchServices(controlUnits)
        return MissionGeneralInfoEntity2(
            services = services,
            crew = fetchCrew(missionId),
            data = fetchGeneralInfo(missionId = missionId, serviceId = services.firstOrNull()?.id)
        )
    }

    fun execute(
        missionIdUUID: UUID,
        controlUnits: List<LegacyControlUnitEntity>? = null
    ): MissionGeneralInfoEntity2 {
        val services = fetchServices(controlUnits)
        return MissionGeneralInfoEntity2(
            services = services,
            crew = fetchCrewUUID(missionIdUUID = missionIdUUID),
            data = fetchGeneralInfoUUID(missionIdUUID = missionIdUUID)
        )
    }

    private fun fetchGeneralInfo(missionId: Int, serviceId: Int? = null): MissionGeneralInfoEntity? {
        return try {
            val entity: MissionGeneralInfoEntity? = getMissionGeneralInfoByMissionId.execute(missionId = missionId)
            entity
                ?: createGeneralInfosRow(
                    missionId = missionId,
                    serviceId = serviceId
                )

        } catch (e: Exception) {
            logger.error("Error fetching Nav general info for missionId: {}", missionId, e)
            throw e
        }
    }

    private fun fetchGeneralInfoUUID(missionIdUUID: UUID, serviceId: Int? = null): MissionGeneralInfoEntity? {
        return try {
            val entity: MissionGeneralInfoEntity? = getMissionGeneralInfoByMissionId.execute(missionIdUUID = missionIdUUID)
            entity
                ?: createGeneralInfosRow(
                    missionIdUUID = missionIdUUID,
                    serviceId = serviceId
                )
        } catch (e: Exception) {
            logger.error("Error fetching Nav general info for missionId: {}", missionIdUUID, e)
            throw e
        }
    }

    fun fetchCrew(missionId: Int): List<MissionCrewEntity> {
        return try {
            getAgentsCrewByMissionId.execute(missionId)
        } catch (e: Exception) {
            logger.error("Error fetching Nav crew for missionId: {}", missionId, e)
            emptyList()
        }
    }

    fun fetchCrewUUID(missionIdUUID: UUID): List<MissionCrewEntity> {
        return try {
            getAgentsCrewByMissionId.execute(missionIdUUID = missionIdUUID)
        } catch (e: Exception) {
            logger.error("Error fetching Nav crew for missionId: {}", missionIdUUID, e)
            emptyList()
        }
    }

    private fun fetchServices(controlUnits: List<LegacyControlUnitEntity>?): List<ServiceEntity> {
        return try {
            getServiceByControlUnit.execute(controlUnits)
        } catch (e: Exception) {
            logger.error("Error fetching Nav services for controlUnits: {}", controlUnits, e)
            emptyList()
        }
    }

    private fun createGeneralInfosRow(
        missionId: Int? = null,
        missionIdUUID: UUID? = null,
        serviceId: Int? = null
    ): MissionGeneralInfoEntity? {
        return createGeneralInfos.execute(
            missionId = missionId,
            missionIdUUID = missionIdUUID,
            generalInfo2 = MissionGeneralInfo2(
                serviceId = serviceId,
                crew = createMissionCrew(
                    missionId = missionId,
                    missionIdUUID = missionIdUUID,
                    serviceId = serviceId
                )?.map { MissionCrew.fromMissionCrewEntity(it) }
            ),
        )?.data
    }

    private fun createMissionCrew(missionId: Int? = null, missionIdUUID: UUID? = null, serviceId: Int? = null): List<MissionCrewEntity>? {
        return serviceId?.let { serviceId ->
            val serviceAgents: List<AgentServiceEntity> = getAgentsByServiceId.execute(serviceId)
            val crew: List<MissionCrewEntity>? = serviceAgents.map {
                MissionCrewEntity.fromAgentServiceEntity(
                    agentService = it,
                    missionId = missionId,
                    missionIdUUID = missionIdUUID
                )
            }
            crew
        }
    }
}
