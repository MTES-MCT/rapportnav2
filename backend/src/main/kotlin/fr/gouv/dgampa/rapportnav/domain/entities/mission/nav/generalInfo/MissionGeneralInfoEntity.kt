package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo

import fr.gouv.dgampa.rapportnav.config.MandatoryForStats
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.JdpTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReinforcementTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReportTypeEnum
import fr.gouv.dgampa.rapportnav.domain.validation.RequiredFields
import fr.gouv.dgampa.rapportnav.domain.validation.ValidateThrowsBeforeSave
import fr.gouv.dgampa.rapportnav.domain.validation.ValidateWhenMissionFinished
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import jakarta.validation.constraints.Min
import java.util.*

@RequiredFields(groups = [ValidateWhenMissionFinished::class])
data class MissionGeneralInfoEntity(
    var id: Int? = null,
    var missionId: Int? = null,
    @MandatoryForStats
    @field:Min(value = 0L, groups = [ValidateThrowsBeforeSave::class], message = "La distance en milles nautiques doit être positive")
    var distanceInNauticalMiles: Float? = null,
    @MandatoryForStats
    @field:Min(value = 0L, groups = [ValidateThrowsBeforeSave::class], message = "La consommation de GO doit être positive")
    var consumedGOInLiters: Float? = null,
    @MandatoryForStats
    @field:Min(value = 0L, groups = [ValidateThrowsBeforeSave::class], message = "La consommation de carburant doit être positive")
    var consumedFuelInLiters: Float? = null,
    @field:Min(value = 0L, groups = [ValidateThrowsBeforeSave::class], message = "Les coûts d'exploitation doivent être positifs")
    var operatingCostsInEuro: Float? = null,
    @field:Min(value = 0L, groups = [ValidateThrowsBeforeSave::class], message = "Les coûts de carburant doivent être positifs")
    var fuelCostsInEuro: Float? = null,
    var service: ServiceEntity? = null,
    @MandatoryForStats
    @field:Min(value = 0L, groups = [ValidateThrowsBeforeSave::class], message = "Le nombre de navires reconnus doit être positif")
    var nbrOfRecognizedVessel: Int? = null,
    var isWithInterMinisterialService: Boolean? = false,
    var isAllAgentsParticipating: Boolean? = false,
    var isMissionArmed: Boolean? = false,
    var resources: List<LegacyControlUnitResourceEntity>? = listOf(),
    @field:Min(value = 0L, groups = [ValidateThrowsBeforeSave::class], message = "Le nombre d'heures en mer doit être positif")
    var nbHourAtSea: Int? = null,
    var missionReportType: MissionReportTypeEnum? = null,
    var reinforcementType: MissionReinforcementTypeEnum? = null,
    var interMinisterialServices: List<InterMinisterialServiceEntity>? = listOf(),
    var jdpType: JdpTypeEnum? = null,
    var missionIdUUID: UUID? = null,
    var isResourcesNotUsed: Boolean? = null
) {

    companion object {
        fun fromMissionGeneralInfoModel(model: MissionGeneralInfoModel): MissionGeneralInfoEntity {
            return MissionGeneralInfoEntity(
                id = model.id,
                missionId = model.missionId,
                distanceInNauticalMiles = model.distanceInNauticalMiles,
                consumedGOInLiters = model.consumedGOInLiters,
                consumedFuelInLiters = model.consumedFuelInLiters,
                operatingCostsInEuro = model.operatingCostsInEuro,
                fuelCostsInEuro = model.fuelCostsInEuro,
                service = model.service?.let { ServiceEntity.fromServiceModel(it) },
                nbrOfRecognizedVessel = model.nbrOfRecognizedVessel,
                isWithInterMinisterialService = model.isWithInterMinisterialService,
                isMissionArmed = model.isMissionArmed,
                nbHourAtSea = model.nbHourAtSea,
                missionReportType = model.missionReportType,
                reinforcementType = model.reinforcementType,
                interMinisterialServices = model.interMinisterialServices?.map { InterMinisterialServiceEntity.fromInterMinisterialServiceModel(it) }?: listOf(),
                jdpType = model.jdpType,
                missionIdUUID = model.missionIdUUID,
                isResourcesNotUsed = model.isResourcesNotUsed
            )
        }
    }


    fun toMissionGeneralInfoModel(): MissionGeneralInfoModel {
        return MissionGeneralInfoModel(
            id = id,
            missionId = missionId,
            distanceInNauticalMiles = distanceInNauticalMiles,
            consumedGOInLiters = consumedGOInLiters,
            consumedFuelInLiters = consumedFuelInLiters,
            operatingCostsInEuro = operatingCostsInEuro,
            fuelCostsInEuro = fuelCostsInEuro,
            service = service?.toServiceModel(),
            nbrOfRecognizedVessel = nbrOfRecognizedVessel,
            isWithInterMinisterialService = isWithInterMinisterialService,
            isMissionArmed = isMissionArmed,
            nbHourAtSea = nbHourAtSea,
            missionReportType = missionReportType,
            reinforcementType = reinforcementType,
            interMinisterialServices = interMinisterialServices?.map { it.toInterMinisterialServiceModel() },
            jdpType = jdpType,
            missionIdUUID = missionIdUUID,
            isResourcesNotUsed = isResourcesNotUsed
        )
    }
}

