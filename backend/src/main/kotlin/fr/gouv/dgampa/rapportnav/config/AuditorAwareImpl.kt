package fr.gouv.dgampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class AuditorAwareImpl : AuditorAware<Int> {
    override fun getCurrentAuditor(): Optional<Int> {
        val principal = SecurityContextHolder.getContext().authentication?.principal

        return when (principal) {
            is User -> Optional.ofNullable(principal.id)
            else -> Optional.of(-1) // default for anonymous/system
        }
    }
}
