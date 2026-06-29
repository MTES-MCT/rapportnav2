package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
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

    private fun sortCrew(crew: List<MissionCrewEntity>): List<MissionCrewEntity> {
        val (withAgent, withoutAgent) = crew.partition { it.agent != null }
        return withAgent.sortedBy { rolePriority.indexOf(it.role?.title) } +
            withoutAgent.sortedBy { rolePriority.indexOf(it.role?.title) }
    }

    fun execute(missionId: UUID): List<MissionCrewEntity> {
        val crew = agentCrewRepository.findByMissionId(missionId = missionId)
        return sortCrew(crew)
    }
}
