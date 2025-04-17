package fr.gouv.dgampa.rapportnav.domain.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionBAAEMPermanenceEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionBAAEMPermanenceModel
import java.util.*

interface INavActionBAAEMRepository {
    fun findAllByMissionId(missionId: String): List<ActionBAAEMPermanenceModel>
    fun findById(id: UUID): Optional<ActionBAAEMPermanenceModel>

    fun save(permanenceBAAEM: ActionBAAEMPermanenceEntity): ActionBAAEMPermanenceModel

    fun deleteById(id: UUID)

    fun existsById(id: UUID): Boolean
}
