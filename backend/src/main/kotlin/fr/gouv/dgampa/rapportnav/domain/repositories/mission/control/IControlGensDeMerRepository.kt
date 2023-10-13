package fr.gouv.dgampa.rapportnav.domain.repositories.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMer


interface IControlGensDeMerRepository {
    fun findAllByMissionId( missionId: Int): List<ControlGensDeMer>

    fun save(control: ControlGensDeMer): ControlGensDeMer
}
