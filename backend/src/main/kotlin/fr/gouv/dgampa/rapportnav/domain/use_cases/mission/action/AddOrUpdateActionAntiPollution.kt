package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionAntiPollutionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionAntiPollutionRepository

@UseCase
class AddOrUpdateActionAntiPollution(private val actionAntiPollutionRepository: INavActionAntiPollutionRepository) {
    fun execute(actionAntiPollutionEntity: ActionAntiPollutionEntity): ActionAntiPollutionEntity {
        return actionAntiPollutionRepository.save(actionAntiPollutionEntity).toAntiPollutionEntity()
    }
}
