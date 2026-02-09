package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.CrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.PassengerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.GeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetServiceByControlUnit
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger.GetMissionPassengers
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.Service
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo
import java.util.*

@UseCase
class GetGeneralInfo(
    private val getServiceById: GetServiceById,
    private val createGeneralInfos: CreateGeneralInfos,
    private val getServiceByControlUnit: GetServiceByControlUnit,
    private val getAgentsCrewByMissionId: GetAgentsCrewByMissionId,
    private val getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId,
    private val getMissionPassengers: GetMissionPassengers
) {
    fun execute(
        missionId: Int,
        controlUnits: List<LegacyControlUnitEntity>? = null,
    ): MissionGeneralInfoEntity {
        val services = fetchServices(controlUnits)
        val serviceId = services.firstOrNull()?.id
        return MissionGeneralInfoEntity(
            services = services,
            crew = fetchCrew(missionId),
            passengers = fetchPassengers(missionId),
            data = fetchGeneralInfo(missionId = missionId, serviceId = serviceId)
        )
    }

    fun execute(
        missionIdUUID: UUID,
        controlUnits: List<LegacyControlUnitEntity>? = null,
        serviceId: Int? = null
    ): MissionGeneralInfoEntity {
        val services = fetchServices(controlUnits)
        return MissionGeneralInfoEntity(
            services = services,
            crew = fetchCrewUUID(missionIdUUID = missionIdUUID),
            passengers = fetchPassengersUUID(missionIdUUID = missionIdUUID),
            data = fetchGeneralInfoUUID(missionIdUUID = missionIdUUID, serviceId = serviceId)
        )
    }

    private fun fetchGeneralInfo(missionId: Int, serviceId: Int? = null): GeneralInfoEntity? {
        return getMissionGeneralInfoByMissionId.execute(missionId = missionId)
            ?: createGeneralInfos(missionId = missionId, serviceId = serviceId)
    }

    private fun fetchGeneralInfoUUID(missionIdUUID: UUID, serviceId: Int? = null): GeneralInfoEntity? {
        return getMissionGeneralInfoByMissionId.execute(missionIdUUID = missionIdUUID)
            ?: createGeneralInfos(missionIdUUID = missionIdUUID, serviceId = serviceId)
    }

    fun fetchCrew(missionId: Int): List<CrewEntity> {
        return getAgentsCrewByMissionId.execute(missionId)
    }

    fun fetchPassengers(missionId: Int): List<PassengerEntity> {
        return getMissionPassengers.execute(missionId)
    }

    fun fetchCrewUUID(missionIdUUID: UUID): List<CrewEntity> {
        return getAgentsCrewByMissionId.execute(missionIdUUID = missionIdUUID)
    }

    fun fetchPassengersUUID(missionIdUUID: UUID): List<PassengerEntity> {
        return getMissionPassengers.execute(missionIdUUID = missionIdUUID)
    }

    private fun fetchServices(controlUnits: List<LegacyControlUnitEntity>?): List<ServiceEntity> {
        return getServiceByControlUnit.execute(controlUnits)
    }

    private fun createGeneralInfos(
        missionId: Int? = null,
        missionIdUUID: UUID? = null,
        serviceId: Int? = null
    ): GeneralInfoEntity? {
        val service: ServiceEntity? = getServiceById.execute(serviceId)
        return createGeneralInfos.execute(
            missionId = missionId,
            missionIdUUID = missionIdUUID,
            generalInfo2 = MissionGeneralInfo(service = service?.let { Service.fromServiceEntity(it) })
        ).data
    }
}
