package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossControlStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionCrossControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ICrossControlRepository
import java.time.Instant
import kotlin.jvm.optionals.getOrNull

@UseCase
class ProcessActionCrossControl(
    private val crossControlRepo: ICrossControlRepository
) {

    fun execute(action: MissionNavActionEntity): MissionActionCrossControlEntity? {

        val crossControl = action.crossControl
        if (action.actionType != ActionType.CROSS_CONTROL || crossControl == null)  return null

        val crossControlId = crossControl.id
        val crossControlStatus = crossControl.status

        if (crossControlId == null && crossControlStatus == CrossControlStatusType.FOLLOW_UP) {
            return action.crossControl
        }

        val entity = crossControl.toCrossControlEntity()
        var model = crossControlRepo.findById(crossControlId!!).getOrNull()

        if (model?.status != entity.status?.toString()) {
            entity.endDateTimeUtc = if (entity.status === CrossControlStatusType.CLOSED) Instant.now() else null
        }
        model = crossControlRepo.save(entity.toCrossControlModel())
        return MissionActionCrossControlEntity
            .fromNavActionEntityAndCrossControlEntity(action, CrossControlEntity.fromCrossControlModel(model))
    }
}
