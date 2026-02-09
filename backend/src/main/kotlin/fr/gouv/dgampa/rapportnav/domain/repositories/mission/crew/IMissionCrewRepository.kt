package fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.CrewEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.CrewModel
import java.util.*

interface IMissionCrewRepository {

    fun findByMissionId(missionId: Int): List<CrewModel>

    fun findByMissionIdUUID(missionIdUUID: UUID): List<CrewModel>

    fun deleteById(id: Int): Boolean

    fun save(crew: CrewEntity): CrewModel
}
