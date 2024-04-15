package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import ActionRescueEntity
import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionRescueRepository

@UseCase
class AddOrUpdateActionRescue(private val actionRescueRepository: INavActionRescueRepository) {
    fun execute(actionRescue: ActionRescueEntity): ActionRescueEntity {
        return actionRescueRepository.save(actionRescue).toActionRescueEntity()
    }
}
