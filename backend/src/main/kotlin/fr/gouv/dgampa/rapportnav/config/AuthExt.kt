package fr.gouv.dgampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import org.springframework.security.core.Authentication

/**
 * Shorthand for controllers accessing the authenticated user.
 */
fun Authentication.toUser(): User {
    return principal as User
}
