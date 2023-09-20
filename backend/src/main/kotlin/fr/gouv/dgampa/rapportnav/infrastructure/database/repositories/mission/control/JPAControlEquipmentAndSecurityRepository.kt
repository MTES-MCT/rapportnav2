package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.control

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlEquipmentAndSecurity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlEquipmentAndSecurityRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control.IDBControlEquipmentAndSecurityRepository
import org.springframework.stereotype.Repository


@Repository
class JPAControlEquipmentAndSecurityRepository(
    private val dbControlEquipmentAndSecurityRepository: IDBControlEquipmentAndSecurityRepository,
    private val mapper: ObjectMapper,
) : IControlEquipmentAndSecurityRepository {

        override fun findAllByMissionId(missionId: Int): List<ControlEquipmentAndSecurity> {
        // TODO call correct function filtering by mission id
            TODO("Not yet implemented")
//        return dbControlEquipmentAndSecurityRepository.findAll().map { it.toControlEquipmentAndSecurity() }

    }


}
