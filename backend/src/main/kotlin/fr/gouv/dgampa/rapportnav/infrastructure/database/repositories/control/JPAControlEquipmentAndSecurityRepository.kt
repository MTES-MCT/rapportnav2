package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.control

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.control.ControlEquipmentAndSecurity
import fr.gouv.dgampa.rapportnav.domain.repositories.control.IControlEquipmentAndSecurityRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.control.IDBControlEquipmentAndSecurityRepository
import org.springframework.stereotype.Repository


@Repository
class JPAControlEquipmentAndSecurityRepository(
    private val dbControlEquipmentAndSecurityRepository: IDBControlEquipmentAndSecurityRepository,
    private val mapper: ObjectMapper,
) : IControlEquipmentAndSecurityRepository {

    override fun findAllByMissionId(missionId: Int): List<ControlEquipmentAndSecurity> {
        // TODO call correct function filtering by mission id
        return dbControlEquipmentAndSecurityRepository.findAll().map { it.toControlEquipmentAndSecurity() }

    }

}
