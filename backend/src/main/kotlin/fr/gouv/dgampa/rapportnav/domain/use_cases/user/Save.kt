package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.user.UserEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.IUserRepository

@UseCase
class Save(private val userRepository: IUserRepository) {
    fun execute(user: UserEntity): UserEntity? {
        return this.userRepository.save(user)
    }
}