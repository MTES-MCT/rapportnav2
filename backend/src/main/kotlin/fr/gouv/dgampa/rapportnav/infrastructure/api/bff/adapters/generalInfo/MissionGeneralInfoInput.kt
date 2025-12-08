package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.generalInfo

import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.generalInfo.MissionGeneralInfo

data class MissionGeneralInfoInput(
    var id: Int?,
    var missionId: Int,
    var distanceInNauticalMiles: Float? = null,
    var consumedGOInLiters: Float? = null,
    var consumedFuelInLiters: Float? = null,
    var serviceId: Int? = null,
    var nbrOfRecognizedVessel: Int? = null
) {
    fun toMissionGeneralInfo(): MissionGeneralInfo {
        return MissionGeneralInfo(
            id = id,
            missionId,
            distanceInNauticalMiles,
            consumedGOInLiters,
            consumedFuelInLiters,
            service = null,
            nbrOfRecognizedVessel
        )
    }

}
