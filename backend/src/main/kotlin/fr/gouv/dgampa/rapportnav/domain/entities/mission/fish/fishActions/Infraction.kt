package fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions

data class Infraction(
    val infractionType: InfractionType? = null,
    val natinf: Int? = null,
    val natinfDescription: String? = null,
    val threat: String? = null,
    val threatCharacterization: String? = null,
    val comments: String? = null,
)

typealias FishInfraction = Infraction
