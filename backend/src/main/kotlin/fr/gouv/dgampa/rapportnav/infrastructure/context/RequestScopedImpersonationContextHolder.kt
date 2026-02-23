package fr.gouv.dgampa.rapportnav.infrastructure.context

import fr.gouv.dgampa.rapportnav.domain.entities.user.ImpersonationContext
import fr.gouv.dgampa.rapportnav.domain.repositories.user.ImpersonationContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

/**
 * Request-scoped implementation of ImpersonationContextHolder.
 * Each HTTP request gets its own instance, ensuring isolation between requests.
 */
@Component
@RequestScope
class RequestScopedImpersonationContextHolder : ImpersonationContextHolder {
    private var context: ImpersonationContext = ImpersonationContext.NONE

    override fun get(): ImpersonationContext = context

    override fun set(context: ImpersonationContext) {
        this.context = context
    }

    override fun clear() {
        context = ImpersonationContext.NONE
    }

    override fun isImpersonating(): Boolean = context.isActive

    override fun getEffectiveServiceId(originalServiceId: Int?): Int? =
        if (context.isActive) context.impersonatedServiceId else originalServiceId
}
