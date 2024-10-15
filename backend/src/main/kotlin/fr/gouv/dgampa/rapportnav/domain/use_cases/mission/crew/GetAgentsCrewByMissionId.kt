package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionCrewRepository

@UseCase
class GetAgentsCrewByMissionId(private val agentCrewRepository: IMissionCrewRepository) {

    fun execute(missionId: Int, commentDefaultsToString: Boolean? = false): List<MissionCrewEntity> {
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

        return agentCrewRepository.findByMissionId(missionId = missionId)
            .map { it.toMissionCrewEntity(commentDefaultsToString) }
            .sortedBy { rolePriority.indexOf(it.role.title) }
    }
}
