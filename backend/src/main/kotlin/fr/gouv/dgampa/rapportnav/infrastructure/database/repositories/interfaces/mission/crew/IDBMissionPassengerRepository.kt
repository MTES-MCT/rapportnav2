package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.passenger.MissionPassengerModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBMissionPassengerRepository : JpaRepository<MissionPassengerModel, Int> {
    fun findByMissionId(missionId: UUID): List<MissionPassengerModel>
}
