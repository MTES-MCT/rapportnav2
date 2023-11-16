package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.generalInfo

import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.generalInfo.MissionGeneralInfo

data class MissionGeneralInfoInput(
    var id: Int?,
    var missionId: Int,
    var distanceInNauticalMiles: Float? = null,
    var consumedGOInLiters: Float? = null,
    var consumedFuelInLiters: Float? = null,
) {
    fun toMissionGeneralInfo(): MissionGeneralInfo {
        return MissionGeneralInfo(
            id = id ?: missionId,
            missionId,
            distanceInNauticalMiles,
            consumedGOInLiters,
            consumedFuelInLiters
        )
    }

}
