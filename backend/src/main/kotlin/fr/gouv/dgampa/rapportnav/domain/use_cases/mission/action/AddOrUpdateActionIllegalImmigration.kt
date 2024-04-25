package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionIllegalImmigrationEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionIllegalImmigrationRepository

@UseCase
class AddOrUpdateActionIllegalImmigration(private val actionIllegalImmigrationRepository: INavActionIllegalImmigrationRepository) {
    fun execute(actionIllegalImmigrationEntity: ActionIllegalImmigrationEntity): ActionIllegalImmigrationEntity {
        return actionIllegalImmigrationRepository.save(actionIllegalImmigrationEntity).toActionIllegalImmigrationEntity()
    }
}
