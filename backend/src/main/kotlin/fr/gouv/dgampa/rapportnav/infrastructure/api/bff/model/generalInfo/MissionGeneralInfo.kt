package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.GeneralInfoEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.Service

data class MissionGeneralInfo(
    var id: Int?,
    var missionId: Int,
    var distanceInNauticalMiles: Float? = null,
    var consumedGOInLiters: Float? = null,
    var consumedFuelInLiters: Float? = null,
    var service: Service? = null,
    var nbrOfRecognizedVessel: Int? = null,
    var nbHourAtSea: Int? = null,
    var interMinisterialServices: List<InterMinisterialService>? = listOf(),
    var resources: List<LegacyControlUnitResourceEntity>? = listOf(),
) {
    fun toMissionGeneralInfoEntity(): GeneralInfoEntity {
        return GeneralInfoEntity(
            id = id,
            missionId = missionId,
            distanceInNauticalMiles = distanceInNauticalMiles,
            consumedGOInLiters = consumedGOInLiters,
            consumedFuelInLiters = consumedFuelInLiters,
            service = service?.toServiceEntity(),
            nbrOfRecognizedVessel = nbrOfRecognizedVessel,
            nbHourAtSea = nbHourAtSea,
            interMinisterialServices = interMinisterialServices?.map { it.toInterMinisterialServiceEntity() },
            resources = resources,
        )
    }

    companion object {
        fun fromMissionGeneralInfoEntity(info: GeneralInfoEntity?) = info?.let { missionGeneralInfoEntity ->
            MissionGeneralInfo(
                id = missionGeneralInfoEntity.id,
                missionId = missionGeneralInfoEntity.missionId ?: 0, // TODO TO CHECK AS SOON AS POSSIBLE (STEP3 REFACTO)
                distanceInNauticalMiles = missionGeneralInfoEntity.distanceInNauticalMiles,
                consumedGOInLiters = missionGeneralInfoEntity.consumedGOInLiters,
                consumedFuelInLiters = missionGeneralInfoEntity.consumedFuelInLiters,
                service = missionGeneralInfoEntity.service?.let { Service.fromServiceEntity(it) },
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
