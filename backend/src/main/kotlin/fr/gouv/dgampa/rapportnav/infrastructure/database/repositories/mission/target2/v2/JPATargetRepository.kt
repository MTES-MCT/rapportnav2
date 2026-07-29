package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.target2.v2

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.target2.v2.TargetModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.target2.v2.IDBTargetRepository
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.orm.ObjectOptimisticLockingFailureException
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
        } catch (e: DataIntegrityViolationException) {
            if (target.externalId != null) {
                logger.warn("JPATargetRepository - duplicate external_id='{}', returning existing target", target.externalId)
                return findByExternalId(target.externalId!!)
                    ?: throw BackendInternalException(message = "Concurrent duplicate for external_id='${target.externalId}' but no existing row found", originalException = e)
            }
            throw BackendInternalException(message = "Unable to save target id='${target.id}'", originalException = e)
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

    /**
     * Deliberately NOT @Transactional: the delegate delete runs in its own (Spring Data) transaction, so a
     * commit-time failure surfaces inside this try/catch — a @Transactional method cannot catch its own commit.
     *
     * A concurrent/duplicate action update can remove part of this target's cascade first, so Hibernate's orphan
     * delete finds 0 rows where it expects 1 (ObjectOptimisticLockingFailureException). The end state we want —
     * the target and its children gone — is already being applied by the other write, so a stale delete is not an
     * error here: log and move on instead of failing the whole action update with a 500.
     */
    override fun deleteById(id: UUID) {
        try {
            dbTargetRepository.deleteById(id)
        } catch (_: ObjectOptimisticLockingFailureException) {
            logger.warn("JPATargetRepository - target id={} was already (partially) removed concurrently; skipping stale delete", id)
        }
    }
}
