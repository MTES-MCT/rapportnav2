package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity

data class MissionGeneralInfo(
    var id: Int,
    var missionId: Int,
    var distanceInNauticalMiles: Float? = null,
    var consumedGOInLiters: Float? = null,
    var consumedFuelInLiters: Float? = null,
    var serviceId: Int? = null,
    var nbrOfRecognizedVessel: Int? = null,
) {
    fun toMissionGeneralInfoEntity(): MissionGeneralInfoEntity {
        return MissionGeneralInfoEntity(
            id,
            missionId,
            distanceInNauticalMiles,
            consumedGOInLiters,
            consumedFuelInLiters,
            serviceId,
            nbrOfRecognizedVessel
        )
    }

    companion object {
        fun fromMissionGeneralInfoEntity(info: MissionGeneralInfoEntity?) = info?.let {
            MissionGeneralInfo(
                id = it.id,
                missionId = info.missionId,
                distanceInNauticalMiles = info.distanceInNauticalMiles,
                consumedGOInLiters = info.consumedGOInLiters,
                consumedFuelInLiters = info.consumedFuelInLiters,
                serviceId = info.serviceId,
                nbrOfRecognizedVessel = info.nbrOfRecognizedVessel
            )
        }
    }
}
