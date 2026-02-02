package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import org.springframework.security.core.context.SecurityContextHolder

@UseCase
class GetUserFromToken {
    fun execute(): User? {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication?.principal as? User
    }
}
