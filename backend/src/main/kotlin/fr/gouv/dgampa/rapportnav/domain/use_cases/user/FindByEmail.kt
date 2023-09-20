package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.user.UserEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository

@UseCase
class FindByEmail(private val userRepository: IUserRepository) {

    fun execute(email: String): UserEntity? {
        return userRepository.findByEmail(email)
    }

}
