package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.control

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.control.ControlGensDeMerModel
import org.springframework.data.jpa.repository.JpaRepository

interface IDBControlGensDeMerRepository: JpaRepository<ControlGensDeMerModel, Int> {
    fun findAllByMissionId(missionId: Int): List<ControlGensDeMerModel>
}
