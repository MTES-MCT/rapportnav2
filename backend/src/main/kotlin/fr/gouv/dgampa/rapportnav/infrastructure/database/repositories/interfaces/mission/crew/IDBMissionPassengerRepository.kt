package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.passenger.PassengerModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBMissionPassengerRepository : JpaRepository<PassengerModel, Int> {
    fun findByMissionId(missionId: Int): List<PassengerModel>

    fun findByMissionIdUUID(missionIdUUID: UUID): List<PassengerModel>
}
