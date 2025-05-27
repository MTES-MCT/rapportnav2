package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossControlStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ICrossControlRepository
import java.util.*
import kotlin.jvm.optionals.getOrNull

@UseCase
class DeleteCrossControl(
    private val crossControlRepo: ICrossControlRepository
) {
    fun execute(crossControlId: UUID?, actionType: ActionType?) {
        if (crossControlId == null || actionType !== ActionType.CROSS_CONTROL) return
        val crossControl = crossControlRepo.findById(crossControlId).getOrNull()
        if (crossControl?.status != CrossControlStatusType.NEW.toString()) return
        return crossControlRepo.deleteById(crossControl.id)
    }
}
