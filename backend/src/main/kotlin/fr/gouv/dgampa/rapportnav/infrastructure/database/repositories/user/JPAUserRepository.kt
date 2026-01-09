package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.user

import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.user.UserModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.user.IDBUserRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.json.JsonMapper

@Repository
class JPAUserRepository(
    private val dbUserRepository: IDBUserRepository,
    private val mapper: JsonMapper
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

    override fun findAll(): List<UserModel> {
       return  dbUserRepository.findAll()
    }

}
