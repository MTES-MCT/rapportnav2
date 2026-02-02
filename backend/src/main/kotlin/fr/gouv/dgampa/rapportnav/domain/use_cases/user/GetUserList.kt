package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository

@UseCase
class GetUserList(private val userRepository: IUserRepository) {
    fun execute(): List<User> {
        return userRepository.findAll().map { User.fromUserModel(it) }
    }
}
