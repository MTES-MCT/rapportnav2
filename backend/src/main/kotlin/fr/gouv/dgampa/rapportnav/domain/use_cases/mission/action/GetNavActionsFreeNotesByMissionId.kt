package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionFreeNoteEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionFreeNoteRepository

@UseCase
class GetNavActionsFreeNotesByMissionId(private val freeNoteRepository: INavActionFreeNoteRepository) {

    fun execute(missionId: Int): List<ActionFreeNoteEntity> {
        return freeNoteRepository.findAllByMissionId(missionId).map { it.toActionFreeNoteEntity() }
    }
}
