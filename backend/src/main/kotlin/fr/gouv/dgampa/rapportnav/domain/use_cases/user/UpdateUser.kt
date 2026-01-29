package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.auth.adapters.inputs.UpdateUserInput

@UseCase
class UpdateUser(
    private val userRepository: IUserRepository
) {

    fun execute(input: UpdateUserInput): User? {
        return try {
            val fromDb = userRepository.findById(input.id)
                ?: throw BackendUsageException(
                    code = COULD_NOT_FIND_EXCEPTION,
                    message = "User not found with id=${input.id}"
                )

            val user = User(
                id = fromDb.id,
                email = input.email.trim(),
                serviceId = input.serviceId,
                firstName = input.firstName.lowercase().trim(),
                lastName = input.lastName.lowercase().trim(),
                password = fromDb.password,
                roles = input.roles ?: listOf(RoleTypeEnum.USER_PAM)
            )

            userRepository.save(user)
        } catch (e: BackendUsageException) {
            throw e
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "UpdateUser failed for userId=${input.id}",
                originalException = e
            )
        }
    }
}
