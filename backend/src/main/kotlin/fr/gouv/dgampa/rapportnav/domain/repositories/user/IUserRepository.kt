package fr.gouv.dgampa.rapportnav.domain.repositories.user

import fr.gouv.dgampa.rapportnav.domain.entities.user.UserEntity

interface IUserRepository {

    fun findByEmail(email:String): UserEntity?

    fun findById(id: Int): UserEntity?

    fun save(user: UserEntity): UserEntity?
    
}
