package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionBAAEMPermanenceEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionBAAEMRepository

@UseCase
class AddOrUpdateActionBAAEMPermanence(private val actionBAAEMRepository: INavActionBAAEMRepository) {
    fun execute(baaemPermanence: ActionBAAEMPermanenceEntity): ActionBAAEMPermanenceEntity {
        return actionBAAEMRepository.save(baaemPermanence).toActionBAAEMPermanenceEntity()
    }
}
