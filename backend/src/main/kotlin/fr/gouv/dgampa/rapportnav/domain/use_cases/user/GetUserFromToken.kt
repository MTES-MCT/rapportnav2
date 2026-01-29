package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import org.springframework.security.core.context.SecurityContextHolder

@UseCase
class GetUserFromToken {
    fun execute(): User? {
        return try {
            val authentication = SecurityContextHolder.getContext().authentication
            authentication?.principal as? User
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "GetUserFromToken failed",
                originalException = e
            )
        }
    }
}
