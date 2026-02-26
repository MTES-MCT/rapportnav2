package fr.gouv.dgampa.rapportnav.infrastructure.api.admin.adapters.input

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.JdpTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReinforcementTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReportTypeEnum
import java.util.UUID

data class AdminGeneralInfosUpdateInput(
    val id: Int? = null,
    val missionId: Int? = null,
    val missionIdUUID: UUID? = null,
    val serviceId: Int? = null,
    val distanceInNauticalMiles: Float? = null,
    val consumedGOInLiters: Float? = null,
    val consumedFuelInLiters: Float? = null,
    val operatingCostsInEuro: Float? = null,
    val fuelCostsInEuro: Float? = null,
    val nbrOfRecognizedVessel: Int? = null,
    val isWithInterMinisterialService: Boolean? = null,
    val isMissionArmed: Boolean? = null,
    val missionReportType: MissionReportTypeEnum? = null,
    val reinforcementType: MissionReinforcementTypeEnum? = null,
    val nbHourAtSea: Int? = null,
    val jdpType: JdpTypeEnum? = null,
    val isResourcesNotUsed: Boolean? = null
)
