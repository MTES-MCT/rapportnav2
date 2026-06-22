package fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions

class DiscardedSpeciesControl(
    var speciesCode: String,
    var rejectedWeight: Double? = null,
    var discardReason: DiscardReason? = null,
    var faoZones: List<String>? = null,
)
