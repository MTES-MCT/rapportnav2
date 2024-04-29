package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionPublicOrderEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionPublicOrderRepository

@UseCase
class AddOrUpdateActionPublicOrder(private val actionPublicOrderRepository: INavActionPublicOrderRepository) {
    fun execute(actionPublicOrderEntity: ActionPublicOrderEntity): ActionPublicOrderEntity {
        return actionPublicOrderRepository.save(actionPublicOrderEntity).toPublicOrderEntity()
    }
}
