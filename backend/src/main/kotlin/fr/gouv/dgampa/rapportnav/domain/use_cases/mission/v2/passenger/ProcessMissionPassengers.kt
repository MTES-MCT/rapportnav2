package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionPassengerEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.BaseMissionListProcessor

@UseCase
class ProcessMissionPassengers(
    getMissionPassengers: GetMissionPassengers,
    updateMissionPassenger: UpdateMissionPassenger,
    deleteMissionPassenger: DeleteMissionPassenger,
) : BaseMissionListProcessor<MissionPassengerEntity, Int>(
    loadById = { getMissionPassengers.execute(missionId = it) },
    saveItem = { updateMissionPassenger.execute(it) },
    deleteItem = { deleteMissionPassenger.execute(it) },
    getId = { it.id }
)
