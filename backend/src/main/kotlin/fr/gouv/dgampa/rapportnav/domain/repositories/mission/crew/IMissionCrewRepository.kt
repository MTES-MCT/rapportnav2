package fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.MissionCrewModel

interface IMissionCrewRepository {

  fun findByMissionId(missionId: Int): List<MissionCrewModel>

  fun deleteById(agentCrewId: Int): Boolean

  fun save(crew: MissionCrewEntity): MissionCrewModel
}
