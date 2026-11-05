package fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions

class DiscardedSpeciesControl(
    var speciesCode: String,
    // filled in by the MonitorFish public API when the species is known
    var speciesName: String? = null,
    var rejectedWeight: Double? = null,
    var discardReason: DiscardReason? = null,
    var faoZones: List<String>? = null,
)
