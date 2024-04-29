package fr.gouv.dgampa.rapportnav.domain.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionVigimerEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionVigimerModel
import java.util.*

interface INavActionVigimerRepository {
    fun findAllByMissionId(missionId: Int): List<ActionVigimerModel>
    fun findById(id: UUID): Optional<ActionVigimerModel>

    fun save(vigimerEntity: ActionVigimerEntity): ActionVigimerModel

    fun deleteById(id: UUID)

    fun existsById(id: UUID): Boolean
}
