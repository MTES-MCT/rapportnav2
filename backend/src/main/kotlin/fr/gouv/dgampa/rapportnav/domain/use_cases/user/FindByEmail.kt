package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository

@UseCase
class FindByEmail(private val userRepository: IUserRepository) {

    fun execute(email: String): User? {
        return userRepository.findByEmail(email)
    }

}
