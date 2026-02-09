package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.CrewEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionCrewRepository

@UseCase
class AddOrUpdateMissionCrew(private val crewRepository: IMissionCrewRepository) {
    fun addOrUpdateMissionCrew(crew: CrewEntity): CrewEntity {
        return crewRepository.save(crew).let { CrewEntity.fromCrewModel(it) }
    }
}
