package fr.gouv.dgampa.rapportnav.domain.entities.user

data class ImpersonationContext(
    val isActive: Boolean = false,
    val originalServiceId: Int? = null,
    val impersonatedServiceId: Int? = null,
    val impersonatedServiceName: String? = null
) {
    companion object {
        val NONE = ImpersonationContext(isActive = false)
    }
}
