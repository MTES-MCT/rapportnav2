package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionFreeNoteRepository
import java.util.*

@UseCase
class DeleteActionFreeNote(private val freeNoteRepository: INavActionFreeNoteRepository) {

    fun execute(id: UUID): Boolean {
        return try {
            if (this.freeNoteRepository.existsById(id)) {
                this.freeNoteRepository.deleteById(id = id)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            // TODO add log
            false
        }

    }
}
