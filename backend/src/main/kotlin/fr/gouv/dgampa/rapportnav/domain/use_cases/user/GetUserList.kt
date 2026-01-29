package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository

@UseCase
class GetUserList(private val userRepository: IUserRepository) {
    fun execute(): List<User> {
        return try {
            userRepository.findAll().map { User.fromUserModel(it) }
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "GetUserList failed",
                originalException = e
            )
        }
    }
}
