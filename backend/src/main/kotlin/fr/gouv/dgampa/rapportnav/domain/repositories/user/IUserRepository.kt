package fr.gouv.dgampa.rapportnav.domain.repositories.user

import fr.gouv.dgampa.rapportnav.domain.entities.user.User

interface IUserRepository {

    fun findByEmail(email:String): User?

    fun findById(id: Int): User?

    fun save(user: User): User?
    
}
