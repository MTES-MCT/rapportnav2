package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.AddOrUpdateMissionCrew
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.DeleteMissionCrew
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.crew.GetAgentsCrewByMissionIdString
import fr.gouv.dgampa.rapportnav.domain.utils.isValidUUID

@UseCase
class ProcessMissionCrew(
    private val getAgentsCrewByMissionId: GetAgentsCrewByMissionId,
    private val addOrUpdateMissionCrew: AddOrUpdateMissionCrew,
    private val deleteMissionCrew: DeleteMissionCrew,
    private val getAgentsCrewByMissionIdString: GetAgentsCrewByMissionIdString
) {
    fun execute(missionId: String, crew:  List<MissionCrewEntity>): List<MissionCrewEntity> {
        val crewIds = crew.map { it.id }

        var databaseMissionCrews: List<MissionCrewEntity>

        if (isValidUUID(missionId)) {
            databaseMissionCrews = getAgentsCrewByMissionIdString.execute(missionId)

        } else {
            databaseMissionCrews = getAgentsCrewByMissionId.execute(missionId.toInt())
        }

        val toDeleteCrews = databaseMissionCrews.filter { !crewIds.contains(it.id) }
        val toSaveCrews = crew.filter { !databaseMissionCrews.contains(it) }

        delete(toDeleteCrews)
        return save(toSaveCrews)
    }

    fun delete(toDelete: List<MissionCrewEntity>) {
        toDelete.map { it.id?.let { it1 -> deleteMissionCrew.execute(it1) } }
    }

    fun save(toSave: List<MissionCrewEntity>): List<MissionCrewEntity> {
        return toSave.map { addOrUpdateMissionCrew.addOrUpdateMissionCrew(it) }
    }
}
