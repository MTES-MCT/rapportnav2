package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.PasswordValidator
import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode.PASSWORD_TOO_WEAK_EXCEPTION
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.HashService
import fr.gouv.dgampa.rapportnav.infrastructure.api.auth.adapters.inputs.UpdateUserPasswordInput

@UseCase
class UpdateUserPassword(
    private val hashService: HashService,
    private val userRepository: IUserRepository
) {

    fun execute(id: Int, input: UpdateUserPasswordInput): User? {
        if (!PasswordValidator.isStrong(input.password)) {
            throw BackendUsageException(
                code = PASSWORD_TOO_WEAK_EXCEPTION,
                message = "Password must be at least 10 characters long and include upper, lower, number, and special character"
            )
        }
        return try {
            val fromDb = userRepository.findById(id)
                ?: throw BackendUsageException(
                    code = COULD_NOT_FIND_EXCEPTION,
                    message = "User not found with id=$id"
                )

            val user = User(
                id = fromDb.id,
                email = fromDb.email.trim(),
                serviceId = fromDb.serviceId,
                firstName = fromDb.firstName.lowercase().trim(),
                lastName = fromDb.lastName.lowercase().trim(),
                password = hashService.hashBcrypt(input.password),
                roles = fromDb.roles
            )
            userRepository.save(user)
        } catch (e: BackendUsageException) {
            throw e
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "UpdateUserPassword failed for userId=$id",
                originalException = e
            )
        }
    }
}
