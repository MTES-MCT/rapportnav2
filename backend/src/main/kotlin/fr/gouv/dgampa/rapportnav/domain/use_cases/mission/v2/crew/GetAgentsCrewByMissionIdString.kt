package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.crew

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionCrewRepository

@UseCase
class GetAgentsCrewByMissionIdString(private val agentCrewRepository: IMissionCrewRepository) {

    fun execute(missionIdString: String, commentDefaultsToString: Boolean? = false): List<MissionCrewEntity> {
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

        return agentCrewRepository.findByMissionIdString(missionIdString = missionIdString)
            .map { it.toMissionCrewEntity(commentDefaultsToString) }
            .sortedBy { rolePriority.indexOf(it.role?.title) }
    }
}
