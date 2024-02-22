package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionFreeNoteEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionFreeNoteRepository

@UseCase
class AddOrUpdateActionFreeNote(private val actionFreeNoteRepository: INavActionFreeNoteRepository) {

    fun execute(freeNoteAction: ActionFreeNoteEntity): ActionFreeNoteEntity {
        return actionFreeNoteRepository.save(freeNoteAction).toActionFreeNoteEntity()
    }
}
