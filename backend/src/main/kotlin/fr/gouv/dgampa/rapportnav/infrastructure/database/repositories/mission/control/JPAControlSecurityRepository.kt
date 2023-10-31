package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.control

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlSecurityRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlSecurityModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionControlRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control.IDBControlSecurityRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional


@Repository
class JPAControlSecurityRepository(
    private val dbControlSecurityRepository: IDBControlSecurityRepository,
    private val actionControlRepository: IDBActionControlRepository,
    private val mapper: ObjectMapper,
) : IControlSecurityRepository {

    override fun existsByActionControlId(actionControlId: String): Boolean {
        return dbControlSecurityRepository.existsByActionControlId(actionControlId)
    }

    override fun findByActionControlId(actionControlId: String): ControlSecurityModel {
        val control = dbControlSecurityRepository.findByActionControlId(actionControlId)
        return control
    }
    @Transactional
    override fun save(control: ControlSecurityEntity): ControlSecurityModel {
        return try {
            val controlSecurityModel = ControlSecurityModel.fromControlSecurityEntity(control)
            dbControlSecurityRepository.save(controlSecurityModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw Exception("Error saving or updating Security Control", e)
        }
    }


}
