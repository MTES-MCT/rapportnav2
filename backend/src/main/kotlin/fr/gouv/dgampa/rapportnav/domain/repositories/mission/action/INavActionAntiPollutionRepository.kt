package fr.gouv.dgampa.rapportnav.domain.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionAntiPollutionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionAntiPollutionModel
import java.util.*

interface INavActionAntiPollutionRepository {
    fun findAllByMissionId(missionId: String): List<ActionAntiPollutionModel>
    fun findById(id: UUID): Optional<ActionAntiPollutionModel>

    fun save(antiPollutionEntity: ActionAntiPollutionEntity): ActionAntiPollutionModel

    fun deleteById(id: UUID)

    fun existsById(id: UUID): Boolean
}
