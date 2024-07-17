package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.control

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlSecurityRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlSecurityModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionControlRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control.IDBControlSecurityRepository
import org.hibernate.NonUniqueResultException
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Repository
class JPAControlSecurityRepository(
    private val dbControlSecurityRepository: IDBControlSecurityRepository,
    private val actionControlRepository: IDBActionControlRepository,
    private val mapper: ObjectMapper,
) : IControlSecurityRepository {

    override fun existsById(id: UUID): Boolean {
        return dbControlSecurityRepository.existsById(id)
    }

    override fun findById(id: UUID): Optional<ControlSecurityModel> {
        val control = dbControlSecurityRepository.findById(id)
        return control
    }

    override fun existsByActionControlId(actionControlId: String): Boolean {
        return dbControlSecurityRepository.existsByActionControlId(actionControlId)
    }

    override fun findByActionControlId(actionControlId: String): ControlSecurityModel {
        try {
            val control = dbControlSecurityRepository.findByActionControlId(actionControlId)
            return control
        } catch (e: NonUniqueResultException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.TOO_MANY_ROWS_EXCEPTION,
                message = "Too many ControlSecurity for ActionControl='$actionControlId'",
                e,
            )
        }
    }

    @Transactional
    override fun save(control: ControlSecurityEntity): ControlSecurityModel {
        return try {
            val controlSecurityModel = ControlSecurityModel.fromControlSecurityEntity(control)
            dbControlSecurityRepository.save(controlSecurityModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "Unable to save ControlSecurity='${control.id}'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Unable to prepare data before saving ControlSecurity",
                originalException = e
            )
        }
    }

    @Transactional
    override fun deleteByActionControlId(actionControlId: String) {
        return dbControlSecurityRepository.deleteByActionControlId(actionControlId)
    }


}
