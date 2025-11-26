package fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionPassengerEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.MissionCrewModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.passenger.MissionPassengerModel
import java.util.*

interface IMissionPassengerRepository {

    fun findByMissionId(missionId: Int): List<MissionPassengerModel>

    fun findByMissionIdUUID(missionIdUUID: UUID): List<MissionPassengerModel>

    fun deleteById(id: Int): Boolean

    fun save(passenger: MissionPassengerEntity): MissionPassengerModel
}
