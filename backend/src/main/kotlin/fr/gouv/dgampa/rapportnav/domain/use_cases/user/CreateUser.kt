package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.PasswordValidator
import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode.ALREADY_EXISTS_EXCEPTION
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode.PASSWORD_TOO_WEAK_EXCEPTION
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.HashService
import fr.gouv.dgampa.rapportnav.infrastructure.api.auth.adapters.inputs.AuthRegisterDataInput

@UseCase
class CreateUser(
    private val hashService: HashService,
    private val userRepository: IUserRepository
) {

    fun execute(input: AuthRegisterDataInput): User? {
        val requiredFields = listOf(input.email, input.password, input.firstName, input.lastName)
        if (requiredFields.any { it.isEmpty() }) {
            throw BackendUsageException(
                code = INVALID_PARAMETERS_EXCEPTION,
                message = "CreateUser: input does not contain all the required data"
            )
        }

        if (!PasswordValidator.isStrong(input.password)) {
            throw BackendUsageException(
                code = PASSWORD_TOO_WEAK_EXCEPTION,
                message = "CreateUser: password must be at least 10 characters long and include upper, lower, number, and special character"
            )
        }

        if (userRepository.findByEmail(input.email) != null) {
            throw BackendUsageException(
                code = ALREADY_EXISTS_EXCEPTION,
                message = "CreateUser: user already exists with email=${input.email}"
            )
        }

        val user = User(
            id = input.id,
            email = input.email.trim(),
            serviceId = input.serviceId,
            firstName = input.firstName.lowercase().trim(),
            lastName = input.lastName.lowercase().trim(),
            password = hashService.hashBcrypt(input.password),
            roles = input.roles ?: listOf(RoleTypeEnum.USER_PAM)
        )

        return userRepository.save(user)
    }
}
