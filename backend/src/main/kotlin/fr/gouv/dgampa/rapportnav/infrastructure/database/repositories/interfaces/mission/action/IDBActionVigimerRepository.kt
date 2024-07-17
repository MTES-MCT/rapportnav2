package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionVigimerEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionVigimerModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBActionVigimerRepository: JpaRepository<ActionVigimerModel, UUID> {

    fun findAllByMissionId(missionId: Int): List<ActionVigimerModel>

    override fun findById(id: UUID): Optional<ActionVigimerModel>

    fun save(rescueModel: ActionVigimerEntity): ActionVigimerModel

    override fun deleteById(id: UUID)

    override fun existsById(id: UUID): Boolean
}
