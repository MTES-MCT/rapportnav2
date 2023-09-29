package fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions

data class LogbookInfraction(
    var infractionType: InfractionType? = null,
    var natinf: Int? = null,
    var comments: String? = null,
)
