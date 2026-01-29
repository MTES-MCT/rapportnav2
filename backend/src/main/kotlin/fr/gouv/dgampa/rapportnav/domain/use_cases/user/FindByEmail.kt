package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository

@UseCase
class FindByEmail(private val userRepository: IUserRepository) {

    fun execute(email: String): User? {
        return try {
            userRepository.findByEmail(email)
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "FindByEmail failed for email=$email",
                originalException = e
            )
        }
    }
}
