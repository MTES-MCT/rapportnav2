package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.CrewEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionCrewRepository
import java.util.*

@UseCase
class GetAgentsCrewByMissionId(private val agentCrewRepository: IMissionCrewRepository) {
    val rolePriority = listOf(
        "Commandant",
        "Second capitaine",
        "Second",
        "Chef mécanicien",
        "Second mécanicien",
        "Mécanicien électricien",
        "Mécanicien",
        "Chef de quart",
        "Maître d’équipage",
        "Agent pont",
        "Agent machine",
        "Agent mécanicien",
        "Électricien",
        "Cuisinier",
    )

    fun execute(missionId: Int, commentDefaultsToString: Boolean? = false): List<CrewEntity> {
        return agentCrewRepository.findByMissionId(missionId = missionId)
            .filter { it.agent != null}
            .map { CrewEntity.fromCrewModel(it) }
            .sortedBy { rolePriority.indexOf(it.role?.title) } //TODO replace by it.role.prority
    }

    fun execute(missionIdUUID: UUID, commentDefaultsToString: Boolean? = false): List<CrewEntity> {
        return agentCrewRepository.findByMissionIdUUID(missionIdUUID = missionIdUUID)
            .filter { it.agent != null}
            .map { CrewEntity.fromCrewModel(it) }
            .sortedBy { rolePriority.indexOf(it.role?.title) } //TODO replace by it.role.prority
    }
}
