package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.PasswordValidator
import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode.ALREADY_EXISTS_EXCEPTION
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode.PASSWORD_TOO_WEAK_EXCEPTION
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.HashService
import fr.gouv.dgampa.rapportnav.infrastructure.api.auth.adapters.inputs.AuthRegisterDataInput
import fr.gouv.dgampa.rapportnav.infrastructure.exceptions.BackendRequestErrorCode
import fr.gouv.dgampa.rapportnav.infrastructure.exceptions.BackendRequestException

@UseCase
class CreateUser (
    private val hashService: HashService,
    private val userRepository: IUserRepository
){

    fun execute(input: AuthRegisterDataInput): User? {
        val requiredFields = listOf(input.email, input.password, input.firstName, input.lastName)
        if (requiredFields.any { it.isEmpty() }) throw BackendRequestException(
            code = BackendRequestErrorCode.BODY_MISSING_DATA,
            message = "input does not contain all the required data"
        )

        if (!PasswordValidator.isStrong(input.password)) {
            throw BackendUsageException(
                code = PASSWORD_TOO_WEAK_EXCEPTION,
                message = "Password must be at least 10 characters long and include upper, lower, number, and special character"
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

        if (userRepository.findByEmail(input.email) != null) {
            throw BackendUsageException(
                code = ALREADY_EXISTS_EXCEPTION,
                message = "User already exists"
            )
        }
        return this.userRepository.save(user)
    }
}
