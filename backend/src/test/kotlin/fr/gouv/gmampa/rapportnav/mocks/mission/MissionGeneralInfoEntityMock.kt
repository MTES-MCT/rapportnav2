package fr.gouv.gmampa.rapportnav.mocks.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.InterMinisterialServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.JdpTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReinforcementTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReportTypeEnum
import java.util.UUID

object MissionGeneralInfoEntityMock {
    fun create(
        id: Int? = null,
        missionId: Int? = null,
        missionIdUUID: UUID? = UUID.fromString("135689d8-3163-426c-aa26-e20eb9eb5f2e"),
        distanceInNauticalMiles: Float? = null,
        consumedGOInLiters: Float? = null,
        consumedFuelInLiters: Float? = null,
        operatingCostsInEuro: Float? = null,
        fuelCostsInEuro: Float? = null,
        service: ServiceEntity? = null,
        nbrOfRecognizedVessel: Int? = null,
        isWithInterMinisterialService: Boolean? = false,
        isAllAgentsParticipating: Boolean? = false,
        isMissionArmed: Boolean? = false,
        resources: List<LegacyControlUnitResourceEntity>? = listOf(),
        nbHourAtSea: Int? = null,
        missionReportType: MissionReportTypeEnum? = null,
        reinforcementType: MissionReinforcementTypeEnum? = null,
        interMinisterialServices: List<InterMinisterialServiceEntity>? = listOf(),
        jdpType: JdpTypeEnum? = null,
        isResourcesNotUsed: Boolean? = null
    ) = MissionGeneralInfoEntity(
        id = id,
        missionId = missionId,
        missionIdUUID = missionIdUUID,
        distanceInNauticalMiles = distanceInNauticalMiles,
        consumedGOInLiters = consumedGOInLiters,
        consumedFuelInLiters = consumedFuelInLiters,
        operatingCostsInEuro = operatingCostsInEuro,
        fuelCostsInEuro = fuelCostsInEuro,
        service = service,
        nbrOfRecognizedVessel = nbrOfRecognizedVessel,
        isWithInterMinisterialService = isWithInterMinisterialService,
        isAllAgentsParticipating = isAllAgentsParticipating,
        isMissionArmed = isMissionArmed,
        resources = resources,
        jdpType = jdpType,
        nbHourAtSea = nbHourAtSea,
        missionReportType = missionReportType,
        reinforcementType = reinforcementType,
        interMinisterialServices = interMinisterialServices,
        isResourcesNotUsed = isResourcesNotUsed
    )
}
