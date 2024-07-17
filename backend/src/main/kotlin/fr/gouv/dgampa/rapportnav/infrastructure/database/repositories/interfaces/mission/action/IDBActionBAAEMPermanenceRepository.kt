package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionBAAEMPermanenceEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionBAAEMPermanenceModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBActionBAAEMPermanenceRepository: JpaRepository<ActionBAAEMPermanenceModel, UUID> {

    fun findAllByMissionId(missionId: Int): List<ActionBAAEMPermanenceModel>

    override fun findById(id: UUID): Optional<ActionBAAEMPermanenceModel>

    fun save(permanenceBAAEM: ActionBAAEMPermanenceEntity): ActionBAAEMPermanenceModel

    override fun deleteById(id: UUID)

    override fun existsById(id: UUID): Boolean
}
