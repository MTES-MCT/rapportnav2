package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMer
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlGensDeMerModel
import org.springframework.data.jpa.repository.JpaRepository

interface IDBControlGensDeMerRepository: JpaRepository<ControlGensDeMerModel, Int> {
    fun findAllByMissionId(missionId: Int): List<ControlGensDeMerModel>

    fun save(control: ControlGensDeMer): ControlGensDeMer
}
