package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.AddOrUpdateMissionCrew
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.DeleteMissionCrew
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId
import java.util.*

@UseCase
class ProcessMissionCrew(
    private val getAgentsCrewByMissionId: GetAgentsCrewByMissionId,
    private val addOrUpdateMissionCrew: AddOrUpdateMissionCrew,
    private val deleteMissionCrew: DeleteMissionCrew,
) {
    fun execute(missionId: Int, crew:  List<MissionCrewEntity>): List<MissionCrewEntity> {
        val databaseMissionCrews = getAgentsCrewByMissionId.execute(missionId = missionId)
        process(crew = crew, databaseMissionCrews = databaseMissionCrews)
        // returned fresh version of crew
        return getAgentsCrewByMissionId.execute(missionId = missionId)
    }

    fun execute(missionIdUUID: UUID, crew: List<MissionCrewEntity>): List<MissionCrewEntity> {
        val databaseMissionCrews = getAgentsCrewByMissionId.execute(missionIdUUID = missionIdUUID)
        process(crew = crew, databaseMissionCrews = databaseMissionCrews)
        // returned fresh version of crew
        return getAgentsCrewByMissionId.execute(missionIdUUID = missionIdUUID)
    }

    fun process(crew: List<MissionCrewEntity>, databaseMissionCrews: List<MissionCrewEntity>) {
        val crewIds = crew.map { it.id }
        val toDeleteCrews = databaseMissionCrews.filter { !crewIds.contains(it.id) }
        val toSaveCrews = crew.filter { !databaseMissionCrews.contains(it) }

        delete(toDeleteCrews)
        save(toSaveCrews)
    }

    fun delete(toDelete: List<MissionCrewEntity>) {
        toDelete.map { it.id?.let { it1 -> deleteMissionCrew.execute(it1) } }
    }

    fun save(toSave: List<MissionCrewEntity>): List<MissionCrewEntity> {
        return toSave.map { addOrUpdateMissionCrew.addOrUpdateMissionCrew(it) }
    }
}
