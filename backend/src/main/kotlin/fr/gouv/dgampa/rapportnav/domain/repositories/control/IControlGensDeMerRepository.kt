package fr.gouv.dgampa.rapportnav.domain.repositories.control

import fr.gouv.dgampa.rapportnav.domain.entities.control.ControlGensDeMer


interface IControlGensDeMerRepository {
    fun findAllByMissionId( missionId: Int): List<ControlGensDeMer>
}
