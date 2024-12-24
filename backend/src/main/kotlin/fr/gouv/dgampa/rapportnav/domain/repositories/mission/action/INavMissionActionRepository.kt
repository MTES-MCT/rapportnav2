package fr.gouv.dgampa.rapportnav.domain.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import java.util.*

interface INavMissionActionRepository {
    fun findByMissionId(missionId: Int): List<MissionActionModel>

    fun findById(id: UUID): Optional<MissionActionModel>

    fun save(action: MissionNavActionEntity): MissionActionModel

    fun deleteById(id: UUID)

    fun existsById(id: UUID): Boolean
}