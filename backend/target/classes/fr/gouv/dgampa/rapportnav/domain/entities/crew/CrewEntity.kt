package fr.gouv.dgampa.rapportnav.domain.entities.crew

data class CrewEntity(
    val id: Int? = null,
    val firstName: String,
    val lastName: String,
    val isArchived: Boolean,
)

