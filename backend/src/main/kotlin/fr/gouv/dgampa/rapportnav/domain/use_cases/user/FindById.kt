package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository

@UseCase
class FindById(private val userRepository: IUserRepository) {
    fun execute(id: Int): User? {
        return this.userRepository.findById(id)
    }
}
