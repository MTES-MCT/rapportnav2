package fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions

data class Infraction(
    var natinfCode: Int,
    var regulation: String? = null,
    var infractionCategory: InfractionCategory? = null,
    var infraction: String? = null,
)
