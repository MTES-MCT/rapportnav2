package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.user

import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.user.UserModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.user.IDBUserRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.json.JsonMapper
import kotlin.jvm.optionals.getOrNull

@Repository
class JPAUserRepository(
    private val dbUserRepository: IDBUserRepository,
    private val mapper: JsonMapper
) : IUserRepository {

    override fun findById(id: Int): User? {
        return try {
            dbUserRepository.findById(id).getOrNull()?.toUser(mapper)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAUserRepository.findById failed for id=$id",
                originalException = e
            )
        }
    }

    override fun findByEmail(email: String): User? {
        return try {
            dbUserRepository.findByEmail(email)?.toUser(mapper)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAUserRepository.findByEmail failed for email=$email",
                originalException = e
            )
        }
    }

    @Transactional
    override fun save(user: User): User? {
        return try {
            val userModel = UserModel.fromUser(user, mapper)
            dbUserRepository.save(userModel).toUser(mapper)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "JPAUserRepository.save: invalid data for user=${user.id}"
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAUserRepository.save failed for user=${user.id}",
                originalException = e
            )
        }
    }

    override fun findAll(): List<UserModel> {
        return try {
            dbUserRepository.findAll()
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAUserRepository.findAll failed",
                originalException = e
            )
        }
    }
}
