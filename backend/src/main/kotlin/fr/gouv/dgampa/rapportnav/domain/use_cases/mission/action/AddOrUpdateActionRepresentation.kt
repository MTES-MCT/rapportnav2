package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRepresentationEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionRepresentationRepository

@UseCase
class AddOrUpdateActionRepresentation(private val actionRepresentationRepository: INavActionRepresentationRepository) {
    fun execute(actionRepresentationEntity: ActionRepresentationEntity): ActionRepresentationEntity {
        return actionRepresentationRepository.save(actionRepresentationEntity).toRepresentationEntity()
    }
}
