package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.control

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlGensDeMerRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlGensDeMerModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionControlRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control.IDBControlGensDeMerRepository
import org.hibernate.NonUniqueResultException
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Repository
class JPAControlGensDeMerRepository(
    private val dbControlGensDeMerRepository: IDBControlGensDeMerRepository,
    private val actionControlRepository: IDBActionControlRepository,
    private val mapper: ObjectMapper,
) : IControlGensDeMerRepository {

    override fun existsById(id: UUID): Boolean {
        return dbControlGensDeMerRepository.existsById(id)
    }

    override fun findById(id: UUID): Optional<ControlGensDeMerModel> {
        val control = dbControlGensDeMerRepository.findById(id)
        return control
    }

    override fun existsByActionControlId(actionControlId: String): Boolean {
        return dbControlGensDeMerRepository.existsByActionControlId(actionControlId)
    }

    override fun findByActionControlId(actionControlId: String): ControlGensDeMerModel {
        try {
            val control = dbControlGensDeMerRepository.findByActionControlId(actionControlId)
            return control
        } catch (e: NonUniqueResultException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.TOO_MANY_ROWS_EXCEPTION,
                message = "Too many ControlGensDeMer for ActionControl='$actionControlId'",
                e,
            )
        }
    }

    @Transactional
    override fun save(control: ControlGensDeMerEntity): ControlGensDeMerModel {
        return try {
            val controlGensDeMerModel = ControlGensDeMerModel.fromControlGensDeMerEntity(control)
            dbControlGensDeMerRepository.save(controlGensDeMerModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "Unable to save ControlGensDeMer='${control.id}'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Unable to prepare data before saving ControlGensDeMer",
                originalException = e
            )
        }
    }

    @Transactional
    override fun deleteByActionControlId(actionControlId: String) {
        return dbControlGensDeMerRepository.deleteByActionControlId(actionControlId)
    }

}
