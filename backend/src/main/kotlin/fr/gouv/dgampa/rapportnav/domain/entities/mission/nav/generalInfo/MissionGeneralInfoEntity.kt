package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo

import fr.gouv.dgampa.rapportnav.config.MandatoryForStats
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.JdpTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReinforcementTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReportTypeEnum
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import java.util.*

data class MissionGeneralInfoEntity(
    var id: Int?,
    var missionId: Int? = null,
    var distanceInNauticalMiles: Float? = null,
    var consumedGOInLiters: Float? = null,
    var consumedFuelInLiters: Float? = null,
    var serviceId: Int? = null,
    @MandatoryForStats
    var nbrOfRecognizedVessel: Int? = null,
    var isWithInterMinisterialService: Boolean? = false,
    var isAllAgentsParticipating: Boolean? = false,
    var isMissionArmed: Boolean? = false,
    var resources: List<LegacyControlUnitResourceEntity>? = listOf(),
    var nbHourAtSea: Int? = null,
    var missionReportType: MissionReportTypeEnum? = null,
    var reinforcementType: MissionReinforcementTypeEnum? = null,
    var interMinisterialServices: List<InterMinisterialServiceEntity>? = listOf(),
    var jdpType: JdpTypeEnum? = null,
    var missionIdUUID: UUID? = null
) {

    companion object {
        fun fromMissionGeneralInfoModel(model: MissionGeneralInfoModel): MissionGeneralInfoEntity {
            return MissionGeneralInfoEntity(
                id = model.id,
                missionId = model.missionId,
                distanceInNauticalMiles = model.distanceInNauticalMiles,
                consumedGOInLiters = model.consumedGOInLiters,
                consumedFuelInLiters = model.consumedFuelInLiters,
                serviceId = model.serviceId,
                nbrOfRecognizedVessel = model.nbrOfRecognizedVessel,
                isWithInterMinisterialService = model.isWithInterMinisterialService,
                isMissionArmed = model.isMissionArmed,
                nbHourAtSea = model.nbHourAtSea,
                missionReportType = model.missionReportType,
                reinforcementType = model.reinforcementType,
                interMinisterialServices = model.interMinisterialServices?.map { InterMinisterialServiceEntity.fromInterMinisterialServiceModel(it) }?: listOf(),
                jdpType = model.jdpType,
                missionIdUUID = model.missionIdUUID
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
            serviceId = serviceId,
            nbrOfRecognizedVessel = nbrOfRecognizedVessel,
            isWithInterMinisterialService = isWithInterMinisterialService,
            isMissionArmed = isMissionArmed,
            nbHourAtSea = nbHourAtSea,
            missionReportType = missionReportType,
            reinforcementType = reinforcementType,
            interMinisterialServices = interMinisterialServices?.map { it.toInterMinisterialServiceModel() },
            jdpType = jdpType,
            missionIdUUID = missionIdUUID
        )
    }
}

