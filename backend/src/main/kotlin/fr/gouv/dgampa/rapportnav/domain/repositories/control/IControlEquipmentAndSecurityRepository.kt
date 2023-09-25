package fr.gouv.dgampa.rapportnav.domain.repositories.control

import fr.gouv.dgampa.rapportnav.domain.entities.control.ControlEquipmentAndSecurity


interface IControlEquipmentAndSecurityRepository {
    fun findAllByMissionId(missionId: Int ): List<ControlEquipmentAndSecurity>
}
