package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionPassengerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetServiceByControlUnit
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.Service
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger.GetMissionPassengers
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import java.util.*

@UseCase
class GetGeneralInfo2(
    private val getServiceById: GetServiceById,
    private val createGeneralInfos: CreateGeneralInfos,
    private val getServiceByControlUnit: GetServiceByControlUnit,
    private val getAgentsCrewByMissionId: GetAgentsCrewByMissionId,
    private val getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId,
    private val getMissionPassengers: GetMissionPassengers
) {
    fun execute(
        missionId: UUID,
        controlUnits: List<LegacyControlUnitEntity>? = null,
        serviceId: Int? = null
    ): MissionGeneralInfoEntity2 {
        val services = fetchServices(controlUnits)
        return MissionGeneralInfoEntity2(
            services = services,
            crew = fetchCrew(missionId),
            passengers = fetchPassengers(missionId),
            data = fetchGeneralInfo(missionId, serviceId = serviceId ?: services.firstOrNull()?.id)
        )
    }

    private fun fetchGeneralInfo(missionId: UUID, serviceId: Int? = null): MissionGeneralInfoEntity? {
        return getMissionGeneralInfoByMissionId.execute(missionId = missionId)
            ?: createGeneralInfos(missionId = missionId, serviceId = serviceId)
    }

    fun fetchCrew(missionId: UUID): List<MissionCrewEntity> {
        return getAgentsCrewByMissionId.execute(missionId)
    }

    fun fetchPassengers(missionId: UUID): List<MissionPassengerEntity> {
        return getMissionPassengers.execute(missionId)
    }

    private fun fetchServices(controlUnits: List<LegacyControlUnitEntity>?): List<ServiceEntity> {
        return getServiceByControlUnit.execute(controlUnits)
    }

    private fun createGeneralInfos(
        missionId: UUID? = null,
        serviceId: Int? = null
    ): MissionGeneralInfoEntity? {
        val service: ServiceEntity? = getServiceById.execute(serviceId)
        return createGeneralInfos.execute(
            missionId = missionId,
            generalInfo2 = MissionGeneralInfo2(service = service?.let { Service.fromServiceEntity(it) })
        ).data
    }
}
