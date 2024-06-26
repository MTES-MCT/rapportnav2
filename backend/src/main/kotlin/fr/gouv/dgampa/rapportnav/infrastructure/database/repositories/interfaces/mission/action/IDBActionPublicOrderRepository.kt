package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionPublicOrderEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionPublicOrderModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBActionPublicOrderRepository: JpaRepository<ActionPublicOrderModel, UUID> {

    fun findAllByMissionId(missionId: Int): List<ActionPublicOrderModel>

    override fun findById(id: UUID): Optional<ActionPublicOrderModel>

    fun save(publicOrderEntity: ActionPublicOrderEntity): ActionPublicOrderModel

    override fun deleteById(id: UUID)

    override fun existsById(id: UUID): Boolean
}
