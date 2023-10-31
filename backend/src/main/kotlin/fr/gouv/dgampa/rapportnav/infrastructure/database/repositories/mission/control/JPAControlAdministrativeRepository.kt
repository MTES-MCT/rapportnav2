package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.control

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlAdministrativeRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlAdministrativeModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionControlRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control.IDBControlAdministrativeRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional


@Repository
class JPAControlAdministrativeRepository(
    private val dbControlAdministrativeRepository: IDBControlAdministrativeRepository,
    private val actionControlRepository: IDBActionControlRepository,
    private val mapper: ObjectMapper,
) : IControlAdministrativeRepository {

    override fun existsByActionControlId(actionControlId: String): Boolean {
        return dbControlAdministrativeRepository.existsByActionControlId(actionControlId)
    }

    override fun findByActionControlId(actionControlId: String): ControlAdministrativeModel {
        val control = dbControlAdministrativeRepository.findByActionControlId(actionControlId)
        return control
    }

    @Transactional
    override fun save(control: ControlAdministrativeEntity): ControlAdministrativeModel {
         return try {
             val controlAdministrativeModel = ControlAdministrativeModel.fromControlAdministrativeEntity(control)
             dbControlAdministrativeRepository.save(controlAdministrativeModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw Exception("Error saving or updating administrative Control", e)
        }
    }

}
