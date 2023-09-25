package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlEquipmentAndSecurityModel
import org.springframework.data.jpa.repository.JpaRepository

interface IDBControlEquipmentAndSecurityRepository: JpaRepository<ControlEquipmentAndSecurityModel, Int> {
    fun findAllByMissionId(missionId: Int): List<ControlEquipmentAndSecurityModel>
}