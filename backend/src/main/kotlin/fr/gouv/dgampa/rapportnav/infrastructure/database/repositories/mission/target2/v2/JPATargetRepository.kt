package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.target2.v2

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.target2.v2.TargetModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.target2.v2.IDBTargetRepository
import org.slf4j.LoggerFactory
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class JPATargetRepository(
    private val dbTargetRepository: IDBTargetRepository
) : ITargetRepository {

    private val logger = LoggerFactory.getLogger(JPATargetRepository::class.java)


    override fun findById(id: UUID): Optional<TargetModel> {
        return dbTargetRepository.findById(id)
    }

    override fun findByActionId(actionId: String): List<TargetModel> {
        return dbTargetRepository.findByActionId(actionId)
    }

    override fun findByExternalId(externalId: String): TargetModel? {
        return dbTargetRepository.findByExternalId(externalId)
    }

    @Transactional
    override fun save(target: TargetModel): TargetModel {
        return try {
            logger.info("JPATargetRepository - preparing to save Target: {}", target)
            val saved = dbTargetRepository.save(target)
            logger.info("JPATargetRepository - Target saved successfully with id={}", target.id)
            saved
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "Unable to save ='${target.id}'",
                e,
            )
        } catch (e: Exception) {
            logger.error("JPATargetRepository - error saving Target", e)
            throw BackendInternalException(
                message = "Unable to prepare data before saving",
                originalException = e
            )
        }
    }

    @Transactional
    override fun deleteByActionId(actionId: String) {
        return dbTargetRepository.deleteByActionId(actionId)
    }

    @Transactional
    override fun deleteById(id: UUID) {
        return dbTargetRepository.deleteById(id)
    }
}
