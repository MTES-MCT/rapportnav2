package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.user

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.user.UserModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.user.IDBUserRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class JPAUserRepository(
    private val dbUserRepository: IDBUserRepository,
    private val mapper: ObjectMapper
) : IUserRepository {

    override fun findById(id: Int): User? {
        return dbUserRepository.findById(id).get().toUser(mapper)
    }

    override fun findByEmail(email: String): User? {
        val userModel = dbUserRepository.findByEmail(email)
        return userModel?.toUser(mapper)
    }

    @Transactional
    override fun save(user: User): User? {
        return try {
            val userModel = UserModel.fromUser(user, mapper)
            dbUserRepository.save(userModel).toUser(mapper)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw Exception("Error adding user", e)
        }
    }

}
