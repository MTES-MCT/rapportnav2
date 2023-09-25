package fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorFish.fishActions

data class Controller(
    var id: Int,
    var controller: String? = null,
    var controllerType: String? = null,
    var administration: String? = null,
)
