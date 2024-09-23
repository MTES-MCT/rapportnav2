package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo

import fr.gouv.dgampa.rapportnav.config.MandatoryForStats

data class MissionGeneralInfoEntity(
    var id: Int,
    var missionId: Int,
    var distanceInNauticalMiles: Float? = null,
    var consumedGOInLiters: Float? = null,
    var consumedFuelInLiters: Float? = null,
    var serviceId: Int? = null,
    @MandatoryForStats
    var nbrOfRecognizedVessel: Int? = null,
)
