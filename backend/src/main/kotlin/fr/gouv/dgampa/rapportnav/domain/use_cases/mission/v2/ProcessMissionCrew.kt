package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.CrewEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.AddOrUpdateMissionCrew
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.DeleteMissionCrew
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId

@UseCase
class ProcessMissionCrew(
    getAgentsCrewByMissionId: GetAgentsCrewByMissionId,
    addOrUpdateMissionCrew: AddOrUpdateMissionCrew,
    deleteMissionCrew: DeleteMissionCrew,
) : BaseMissionListProcessor<CrewEntity, Int>(
    loadByInt = { getAgentsCrewByMissionId.execute(missionId = it) },
    loadByUUID = { getAgentsCrewByMissionId.execute(missionIdUUID = it) },
    saveItem = { addOrUpdateMissionCrew.addOrUpdateMissionCrew(it) },
    deleteItem = { deleteMissionCrew.execute(it) },
    getId = { it.id }
)
