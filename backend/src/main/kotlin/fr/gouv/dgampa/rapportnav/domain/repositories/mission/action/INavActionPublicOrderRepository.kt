package fr.gouv.dgampa.rapportnav.domain.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionPublicOrderEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionPublicOrderModel
import java.util.*

interface INavActionPublicOrderRepository {
    fun findAllByMissionId(missionId: String): List<ActionPublicOrderModel>
    fun findById(id: UUID): Optional<ActionPublicOrderModel>

    fun save(publicOrderEntity: ActionPublicOrderEntity): ActionPublicOrderModel

    fun deleteById(id: UUID)

    fun existsById(id: UUID): Boolean
}
