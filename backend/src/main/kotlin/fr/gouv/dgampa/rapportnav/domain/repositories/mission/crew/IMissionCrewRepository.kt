package fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.MissionCrewModel
import java.util.*

interface IMissionCrewRepository {

    fun findByMissionId(missionId: Int): List<MissionCrewModel>

    fun findByMissionIdUUID(missionIdUUID: UUID): List<MissionCrewModel>

    fun deleteById(id: Int): Boolean

    fun save(crew: MissionCrewEntity): MissionCrewModel
}
