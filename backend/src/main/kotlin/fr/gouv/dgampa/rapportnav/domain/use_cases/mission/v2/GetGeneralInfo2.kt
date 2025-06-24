package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetServiceByControlUnit
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import org.slf4j.LoggerFactory
import java.util.*

@UseCase
class GetGeneralInfo2(
    private val getServiceByControlUnit: GetServiceByControlUnit,
    private val getAgentsCrewByMissionId: GetAgentsCrewByMissionId,
    private val getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId,
) {
    private val logger = LoggerFactory.getLogger(GetGeneralInfo2::class.java)

    fun execute(
        missionId: Int,
        controlUnits: List<LegacyControlUnitEntity>? = null
    ): MissionGeneralInfoEntity2 {
        return MissionGeneralInfoEntity2(
            crew = fetchCrew(missionId),
            services = fetchServices(controlUnits),
            data = fetchGeneralInfo(missionId)
        )
    }

    fun execute(
        missionIdUUID: UUID,
        controlUnits: List<LegacyControlUnitEntity>? = null
    ): MissionGeneralInfoEntity2 {
        return MissionGeneralInfoEntity2(
            services = fetchServices(controlUnits),
            crew = fetchCrewUUID(missionIdUUID = missionIdUUID),
            data = fetchGeneralInfoUUID(missionIdUUID = missionIdUUID)
        )
    }

    private fun fetchGeneralInfo(missionId: Int): MissionGeneralInfoEntity? {
        return try {
            getMissionGeneralInfoByMissionId.execute(missionId)
        } catch (e: Exception) {
            logger.error("Error fetching Nav general info for missionId: {}", missionId, e)
            throw e
        }
    }

    private fun fetchGeneralInfoUUID(missionIdUUID: UUID): MissionGeneralInfoEntity? {
        return try {
            getMissionGeneralInfoByMissionId.execute(missionIdUUID = missionIdUUID)
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
}
