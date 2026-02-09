package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.CrewModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBMissionCrewRepository : JpaRepository<CrewModel, Int> {
    fun findByMissionId(missionId: Int): List<CrewModel>

    fun findByMissionIdUUID(missionIdUUID: UUID): List<CrewModel>
}
