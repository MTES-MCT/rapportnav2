package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionVigimerEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionVigimerRepository

@UseCase
class AddOrUpdateActionVigimer(private val actionVigimerRepository: INavActionVigimerRepository) {

    fun execute(vigimerEntity: ActionVigimerEntity): ActionVigimerEntity {
        return actionVigimerRepository.save(vigimerEntity).toActionVigimerEntity()
    }
}
