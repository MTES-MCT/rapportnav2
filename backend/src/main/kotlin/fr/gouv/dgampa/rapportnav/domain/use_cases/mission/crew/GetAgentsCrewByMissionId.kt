package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionCrewRepository
import java.util.*

@UseCase
class GetAgentsCrewByMissionId(private val missionCrewRepository: IMissionCrewRepository) {
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

    fun execute(missionId: Int, commentDefaultsToString: Boolean? = false): List<MissionCrewEntity> {
        return missionCrewRepository.findByMissionId(missionId = missionId)
            .map { MissionCrewEntity.fromMissionCrewModel(it) }
            .sortedBy { rolePriority.indexOf(it.role?.title) } //TODO replace by it.role.prority
    }

    fun execute(missionIdUUID: UUID, commentDefaultsToString: Boolean? = false): List<MissionCrewEntity> {
        return missionCrewRepository.findByMissionIdUUID(missionIdUUID = missionIdUUID)
            .map { MissionCrewEntity.fromMissionCrewModel(it) }
            .sortedBy { rolePriority.indexOf(it.role?.title) } //TODO replace by it.role.prority
    }
}
