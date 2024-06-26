package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.control

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlAdministrativeRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlAdministrativeModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionControlRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control.IDBControlAdministrativeRepository
import org.hibernate.NonUniqueResultException
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Repository
class JPAControlAdministrativeRepository(
    private val dbControlAdministrativeRepository: IDBControlAdministrativeRepository,
    private val actionControlRepository: IDBActionControlRepository,
    private val mapper: ObjectMapper,
) : IControlAdministrativeRepository {

    override fun existsById(id: UUID): Boolean {
        return dbControlAdministrativeRepository.existsById(id)
    }

    override fun findById(id: UUID): Optional<ControlAdministrativeModel> {
        val control = dbControlAdministrativeRepository.findById(id)
        return control
    }

    override fun existsByActionControlId(actionControlId: String): Boolean {
        return dbControlAdministrativeRepository.existsByActionControlId(actionControlId)
    }

    override fun findByActionControlId(actionControlId: String): ControlAdministrativeModel {
        try {
            val control = dbControlAdministrativeRepository.findByActionControlId(actionControlId)
            return control
        } catch (e: NonUniqueResultException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.TOO_MANY_ROWS_EXCEPTION,
                message = "Too many ControlAdministrative for ActionControl='$actionControlId'",
                e,
            )
        }
    }

    @Transactional
    override fun save(control: ControlAdministrativeEntity): ControlAdministrativeModel {
        return try {
            val controlAdministrativeModel = ControlAdministrativeModel.fromControlAdministrativeEntity(control)
            dbControlAdministrativeRepository.save(controlAdministrativeModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "Unable to save ControlAdministrative='${control.id}'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Unable to prepare data before saving ControlAdministrative",
                originalException = e
            )
        }
    }

    @Transactional
    override fun deleteByActionControlId(actionControlId: String) {
        return dbControlAdministrativeRepository.deleteByActionControlId(actionControlId)
    }

}
