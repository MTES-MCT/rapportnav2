package fr.gouv.dgampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class AuditorAwareImpl : AuditorAware<Int> {
    override fun getCurrentAuditor(): Optional<Int> {
        // From Spring Security
        val user: User? = SecurityContextHolder.getContext().authentication?.principal as User?

        return if (user != null) {
            Optional.ofNullable(user.id)
        } else {
            // Or a default value for data migrations for ex
            val systemValue = -1
            Optional.of(systemValue)
        }
    }
}
