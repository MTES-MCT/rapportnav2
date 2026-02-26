package fr.gouv.dgampa.rapportnav.infrastructure.api.admin.adapters.outputs

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.JdpTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReinforcementTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReportTypeEnum
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import java.time.Instant
import java.util.UUID

data class AdminGeneralInfosOutput(
    val id: Int?,
    val missionId: Int?,
    val missionIdUUID: UUID?,
    val serviceId: Int?,
    val distanceInNauticalMiles: Float?,
    val consumedGOInLiters: Float?,
    val consumedFuelInLiters: Float?,
    val operatingCostsInEuro: Float?,
    val fuelCostsInEuro: Float?,
    val nbrOfRecognizedVessel: Int?,
    val isWithInterMinisterialService: Boolean?,
    val isMissionArmed: Boolean?,
    val missionReportType: MissionReportTypeEnum?,
    val reinforcementType: MissionReinforcementTypeEnum?,
    val nbHourAtSea: Int?,
    val jdpType: JdpTypeEnum?,
    val isResourcesNotUsed: Boolean?,
    val createdAt: Instant?,
    val updatedAt: Instant?,
    val createdBy: Int?,
    val updatedBy: Int?
) {
    companion object {
        fun fromModel(model: MissionGeneralInfoModel): AdminGeneralInfosOutput {
            return AdminGeneralInfosOutput(
                id = model.id,
                missionId = model.missionId,
                missionIdUUID = model.missionIdUUID,
                serviceId = model.service?.id,
                distanceInNauticalMiles = model.distanceInNauticalMiles,
                consumedGOInLiters = model.consumedGOInLiters,
                consumedFuelInLiters = model.consumedFuelInLiters,
                operatingCostsInEuro = model.operatingCostsInEuro,
                fuelCostsInEuro = model.fuelCostsInEuro,
                nbrOfRecognizedVessel = model.nbrOfRecognizedVessel,
                isWithInterMinisterialService = model.isWithInterMinisterialService,
                isMissionArmed = model.isMissionArmed,
                missionReportType = model.missionReportType,
                reinforcementType = model.reinforcementType,
                nbHourAtSea = model.nbHourAtSea,
                jdpType = model.jdpType,
                isResourcesNotUsed = model.isResourcesNotUsed,
                createdAt = model.createdAt,
                updatedAt = model.updatedAt,
                createdBy = model.createdBy,
                updatedBy = model.updatedBy
            )
        }
    }
}
