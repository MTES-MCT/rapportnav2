package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlSecurityModel
import org.springframework.data.jpa.repository.JpaRepository

interface IDBControlSecurityRepository: JpaRepository<ControlSecurityModel, Int> {
    fun findAllByMissionId(missionId: Int): List<ControlSecurityModel>
}
