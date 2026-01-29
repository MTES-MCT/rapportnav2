package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository

@UseCase
class FindById(private val userRepository: IUserRepository) {
    fun execute(id: Int): User? {
        return try {
            userRepository.findById(id)
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "FindById failed for userId=$id",
                originalException = e
            )
        }
    }
}
