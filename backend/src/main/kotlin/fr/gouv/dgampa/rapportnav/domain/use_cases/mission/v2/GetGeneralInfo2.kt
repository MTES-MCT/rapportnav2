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
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.crew.GetAgentsCrewByMissionIdString
import fr.gouv.dgampa.rapportnav.domain.utils.isValidUUID
import org.slf4j.LoggerFactory

@UseCase
class GetGeneralInfo2(
    private val getServiceByControlUnit: GetServiceByControlUnit,
    private val getAgentsCrewByMissionId: GetAgentsCrewByMissionId,
    private val getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId,
    private val getAgentsCrewByMissionIdString: GetAgentsCrewByMissionIdString,
    private val getMissionGeneralInfoByMissionIdString: GetMissionGeneralInfoByMissionIdString
) {
    private val logger = LoggerFactory.getLogger(GetGeneralInfo2::class.java)

    fun execute(
        missionId: String,
        controlUnits: List<LegacyControlUnitEntity>? = null
    ): MissionGeneralInfoEntity2 {
        return MissionGeneralInfoEntity2(
            crew = fetchCrew(missionId),
            services = fetchServices(controlUnits),
            data = fetchGeneralInfo(missionId)
        )
    }

    private fun fetchGeneralInfo(missionId: String): MissionGeneralInfoEntity? {
        return try {
            if (isValidUUID(missionId)) {
                getMissionGeneralInfoByMissionIdString.execute(missionId)
            } else {
                getMissionGeneralInfoByMissionId.execute(missionId.toInt())
            }

        } catch (e: Exception) {
            logger.error("Error fetching Nav general info for missionId: {}", missionId, e)
            throw e
        }
    }

    fun fetchCrew(missionId: String): List<MissionCrewEntity> {
        return try {
            if (isValidUUID(missionId)) {
                getAgentsCrewByMissionIdString.execute(missionId)
            } else {
                getAgentsCrewByMissionId.execute(missionId.toInt())
            }

        } catch (e: Exception) {
            logger.error("Error fetching Nav crew for missionId: {}", missionId, e)
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
