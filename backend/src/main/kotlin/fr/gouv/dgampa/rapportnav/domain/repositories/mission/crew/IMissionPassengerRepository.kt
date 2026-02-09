package fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.PassengerEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.passenger.PassengerModel
import java.util.*

interface IMissionPassengerRepository {

    fun findByMissionId(missionId: Int): List<PassengerModel>

    fun findByMissionIdUUID(missionIdUUID: UUID): List<PassengerModel>

    fun deleteById(id: Int): Boolean

    fun save(passenger: PassengerEntity): PassengerModel
}
