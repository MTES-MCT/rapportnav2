package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.HashService

@UseCase
class GetUserList(private val hashService: HashService, private val userRepository: IUserRepository) {
    fun execute(): List<User> {
        return userRepository.findAll().map { User.fromUserModel(it) }
    }
}
