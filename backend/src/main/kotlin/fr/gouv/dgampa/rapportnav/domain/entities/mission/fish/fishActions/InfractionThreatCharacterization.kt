package fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions

data class InfractionThreatCharacterization(
    val natinfCode: Int,
    val infraction: String,
    val threat: String,
    val threatCharacterization: String,
)
