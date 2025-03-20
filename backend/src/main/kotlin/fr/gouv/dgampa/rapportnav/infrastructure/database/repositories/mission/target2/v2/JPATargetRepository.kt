package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.target2.v2

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.target2.v2.TargetModel2
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.target2.v2.IDBTargetRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class JPATargetRepository(
    private val dbControlRepository: IDBTargetRepository
) : ITargetRepository {

    override fun findById(id: UUID): Optional<TargetModel2> {
        return dbControlRepository.findById(id)
    }

    override fun findByActionId(actionId: UUID): List<TargetModel2> {
        return dbControlRepository.findByActionId(actionId)
    }

    @Transactional
    override fun save(target: TargetModel2): TargetModel2 {
        return try {
            dbControlRepository.save(target)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "Unable to save ='${target.id}'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Unable to prepare data before saving",
                originalException = e
            )
        }
    }

    @Transactional
    override fun deleteByActionId(actionId: UUID) {
        return dbControlRepository.deleteByActionId(actionId)
    }
}
