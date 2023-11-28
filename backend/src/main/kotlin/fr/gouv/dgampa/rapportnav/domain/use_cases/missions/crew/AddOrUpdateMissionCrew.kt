package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.crew

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionCrewRepository

@UseCase
class AddOrUpdateMissionCrew(private val crewRepository: IMissionCrewRepository) {

  fun addOrUpdateMissionCrew(crew: MissionCrewEntity): MissionCrewEntity {



    // GEt USer

    // get service for user

    // add service to agentCre

  val a = crewRepository.save(crew)
    return crewRepository.save(crew).toMissionCrewEntity()
  }
}
