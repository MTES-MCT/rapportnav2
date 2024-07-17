package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionNauticalEventEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionNauticalEventRepository

@UseCase
class AddOrUpdateActionNauticalEvent(private val actionNauticalEventRepository: INavActionNauticalEventRepository) {

    fun execute(nauticalEvent: ActionNauticalEventEntity): ActionNauticalEventEntity {
        return actionNauticalEventRepository.save(nauticalEvent).toActionNauticalEventEntity()
    }
}
