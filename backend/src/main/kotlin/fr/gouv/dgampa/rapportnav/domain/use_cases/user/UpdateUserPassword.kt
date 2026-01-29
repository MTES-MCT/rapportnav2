package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.PasswordValidator
import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
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
                message = "UpdateUserPassword: password must be at least 10 characters long and include upper, lower, number, and special character"
            )
        }

        val fromDb = userRepository.findById(id)
            ?: throw BackendUsageException(
                code = COULD_NOT_FIND_EXCEPTION,
                message = "UpdateUserPassword: user not found with id=$id"
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

        return userRepository.save(user)
    }
}
