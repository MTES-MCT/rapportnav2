package fr.gouv.dgampa.rapportnav.domain.repositories.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlEquipmentAndSecurity


interface IControlEquipmentAndSecurityRepository {
    fun findAllByMissionId(missionId: Int ): List<ControlEquipmentAndSecurity>
}
