package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.auth.adapters.inputs.UpdateUserInput
import org.springframework.data.crossstore.ChangeSetPersister

@UseCase
class UpdateUser (
    private val userRepository: IUserRepository
){

    fun execute(input: UpdateUserInput): User? {
       val fromDb = this.userRepository.findById(input.id) ?: throw ChangeSetPersister.NotFoundException()

        val user = User(
            id = fromDb.id,
            email = input.email.trim(),
            serviceId = input.serviceId,
            firstName = input.firstName.lowercase().trim(),
            lastName = input.lastName.lowercase().trim(),
            password = fromDb.password,
            roles = input.roles ?: listOf(RoleTypeEnum.USER_PAM)
        )

        return this.userRepository.save(user)
    }
}
