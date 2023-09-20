package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.user

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.user.UserEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.user.UserModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.user.IDBUserRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception

@Repository
class JPAUserRepository (
    private val dbUserRepository: IDBUserRepository,
    private val mapper: ObjectMapper
) : IUserRepository {

    override fun findById(id: Int): UserEntity? {
        return dbUserRepository.findById(id).get().toUserEntity(mapper)
    }

    override fun findByEmail(email: String): UserEntity? {
        val userModel = dbUserRepository.findByEmail(email)
        return userModel?.toUserEntity(mapper)
    }

    @Transactional
    override fun save(user: UserEntity): UserEntity? {
        return try {
//            user.setPassword(user.password)
            val userModel = UserModel.fromUserEntity(user, mapper)
            dbUserRepository.save(userModel).toUserEntity(mapper)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw Exception("Error adding user", e)
        }
    }

}
