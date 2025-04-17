package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity

data class MissionGeneralInfo(
    var id: Int?,
    var missionId: String,
    var distanceInNauticalMiles: Float? = null,
    var consumedGOInLiters: Float? = null,
    var consumedFuelInLiters: Float? = null,
    var serviceId: Int? = null,
    var nbrOfRecognizedVessel: Int? = null,
    var nbHourAtSea: Int? = null,
    var interMinisterialServices: List<InterMinisterialService>? = listOf(),
    var resources: List<LegacyControlUnitResourceEntity>? = listOf(),
) {
    fun toMissionGeneralInfoEntity(): MissionGeneralInfoEntity {
        return MissionGeneralInfoEntity(
            id = id,
            missionId = missionId,
            distanceInNauticalMiles = distanceInNauticalMiles,
            consumedGOInLiters = consumedGOInLiters,
            consumedFuelInLiters = consumedFuelInLiters,
            serviceId = serviceId,
            nbrOfRecognizedVessel = nbrOfRecognizedVessel,
            nbHourAtSea = nbHourAtSea,
            interMinisterialServices = interMinisterialServices?.map { it.toInterMinisterialServiceEntity() },
            resources = resources,
        )
    }

    companion object {
        fun fromMissionGeneralInfoEntity(info: MissionGeneralInfoEntity?) = info?.let { missionGeneralInfoEntity ->
            MissionGeneralInfo(
                id = missionGeneralInfoEntity.id,
                missionId = missionGeneralInfoEntity.missionId,
                distanceInNauticalMiles = missionGeneralInfoEntity.distanceInNauticalMiles,
                consumedGOInLiters = missionGeneralInfoEntity.consumedGOInLiters,
                consumedFuelInLiters = missionGeneralInfoEntity.consumedFuelInLiters,
                serviceId = missionGeneralInfoEntity.serviceId,
                nbrOfRecognizedVessel = missionGeneralInfoEntity.nbrOfRecognizedVessel,
                nbHourAtSea = missionGeneralInfoEntity.nbHourAtSea,
                interMinisterialServices = missionGeneralInfoEntity.interMinisterialServices?.map { interMinisterialServiceEntity ->
                    InterMinisterialService.fromInterMinisterialServiceEntity(interMinisterialServiceEntity)
                },
                resources = info.resources,
            )
        }

    }
}
