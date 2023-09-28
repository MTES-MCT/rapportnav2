package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew

data class Crew(
    val id: Int? = null,
    val firstName: String,
    val lastName: String,
    val isArchived: Boolean,
)
