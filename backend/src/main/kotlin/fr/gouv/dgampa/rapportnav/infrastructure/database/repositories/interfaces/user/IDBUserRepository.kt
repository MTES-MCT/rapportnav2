package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.user

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.user.UserModel
import org.springframework.data.jpa.repository.JpaRepository

interface IDBUserRepository : JpaRepository<UserModel, Int> {
    fun findByEmail(email: String): UserModel?
}
