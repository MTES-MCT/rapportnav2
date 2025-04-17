package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionAntiPollutionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionAntiPollutionModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBActionAntiPollutionRepository: JpaRepository<ActionAntiPollutionModel, UUID> {

    fun findAllByMissionId(missionId: String): List<ActionAntiPollutionModel>

    override fun findById(id: UUID): Optional<ActionAntiPollutionModel>

    fun save(antiPollutionEntity: ActionAntiPollutionEntity): ActionAntiPollutionModel

    override fun deleteById(id: UUID)

    override fun existsById(id: UUID): Boolean
}
