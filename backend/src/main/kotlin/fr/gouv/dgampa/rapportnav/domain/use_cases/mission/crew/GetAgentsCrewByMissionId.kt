package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionCrewRepository

@UseCase
class GetAgentsCrewByMissionId(private val agentCrewRepository: IMissionCrewRepository) {

    fun execute(missionId: Int): List<MissionCrewEntity> {
        return agentCrewRepository.findByMissionId(missionId = missionId).map { it.toMissionCrewEntity() }
            .sortedBy { it.id }
    }
}
