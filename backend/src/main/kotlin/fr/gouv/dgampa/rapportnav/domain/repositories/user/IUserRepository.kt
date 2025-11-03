package fr.gouv.dgampa.rapportnav.domain.repositories.user

import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.user.UserModel

interface IUserRepository {

    fun findByEmail(email: String): User?

    fun findById(id: Int): User?

    fun save(user: User): User?

    fun findAll(): List<UserModel>
}
