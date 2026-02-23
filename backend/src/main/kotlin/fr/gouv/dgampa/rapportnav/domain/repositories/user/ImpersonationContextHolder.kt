package fr.gouv.dgampa.rapportnav.domain.repositories.user

import fr.gouv.dgampa.rapportnav.domain.entities.user.ImpersonationContext

/**
 * Interface for holding impersonation context during a request.
 * This allows the impersonation state to be accessed throughout the request lifecycle.
 */
interface ImpersonationContextHolder {
    fun get(): ImpersonationContext
    fun set(context: ImpersonationContext)
    fun clear()
    fun isImpersonating(): Boolean
    fun getEffectiveServiceId(originalServiceId: Int?): Int?
}
