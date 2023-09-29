package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository

@UseCase
class Save(private val userRepository: IUserRepository) {
    fun execute(user: User): User? {
        return this.userRepository.save(user)
    }
}
