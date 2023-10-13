package fr.gouv.dgampa.rapportnav.domain.repositories.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurity


interface IControlSecurityRepository {
    fun findAllByMissionId(missionId: Int ): List<ControlSecurity>

    fun save(control: ControlSecurity): ControlSecurity
}
