package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavActionData

@UseCase
class CreateNavAction(
    private val missionActionRepository: INavMissionActionRepository,
) {
    fun execute(input: MissionAction): MissionNavActionEntity {
        val action = MissionNavActionData.toMissionNavActionEntity(input)
        return MissionNavActionEntity.fromMissionActionModel(missionActionRepository.save(action.toMissionActionModel()))
    }
}
