package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControlEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionControlModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBActionControlRepository: JpaRepository<ActionControlModel, UUID> {
    fun findAllByMissionId(missionId: Int): List<ActionControlModel>

    override fun existsById(id: UUID): Boolean

    fun save(controlAction: ActionControlEntity): ActionControlModel

    override fun deleteById(id: UUID)

    fun findById(id: UUID?): Optional<ActionControlModel>

}
