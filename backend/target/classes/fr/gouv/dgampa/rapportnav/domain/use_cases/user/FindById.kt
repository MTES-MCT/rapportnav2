package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.user.UserEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.IUserRepository

@UseCase
class FindById(private val userRepository: IUserRepository) {
    fun execute(id: Int): UserEntity? {
        return this.userRepository.findById(id)
    }
}