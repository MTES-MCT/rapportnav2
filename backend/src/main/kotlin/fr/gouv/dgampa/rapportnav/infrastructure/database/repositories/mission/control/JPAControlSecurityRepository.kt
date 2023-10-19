package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.control

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurity
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

    @Transactional
    override fun save(control: ControlSecurity): ControlSecurityModel {
        return try {
            val actionControl = actionControlRepository.findById(control.actionControlId)
            val controlSecurityModel = ControlSecurityModel.fromControlSecurity(control, actionControl)
            dbControlSecurityRepository.save(controlSecurityModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw Exception("Error saving or updating Security Control", e)
        }
    }


}
