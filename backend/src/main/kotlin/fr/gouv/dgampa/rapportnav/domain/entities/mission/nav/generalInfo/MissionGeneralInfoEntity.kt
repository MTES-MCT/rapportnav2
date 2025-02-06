package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo

import fr.gouv.dgampa.rapportnav.config.MandatoryForStats
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlResourceEntity

data class MissionGeneralInfoEntity(
    var id: Int,
    var missionId: Int,
    var distanceInNauticalMiles: Float? = null,
    var consumedGOInLiters: Float? = null,
    var consumedFuelInLiters: Float? = null,
    var serviceId: Int? = null,
    @MandatoryForStats
    var nbrOfRecognizedVessel: Int? = null,
    var isWithInterMinisterialService: Boolean? = false,
    var isAllAgentsParticipating: Boolean? = false,
    var isMissionArmed: Boolean? = false,
    var resources: List<ControlResourceEntity>? = null
)
