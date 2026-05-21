package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew

data class ResourceInput(
    val id: Int,
    val name: String,
    val controlUnitId: Int,
    val registrationId: String? = null,
    val radioFrequency: String? = null
)
