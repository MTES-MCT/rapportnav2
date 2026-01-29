package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository

@UseCase
class Save(private val userRepository: IUserRepository) {
    fun execute(user: User): User? {
        return try {
            userRepository.save(user)
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Save user failed for userId=${user.id}",
                originalException = e
            )
        }
    }
}
