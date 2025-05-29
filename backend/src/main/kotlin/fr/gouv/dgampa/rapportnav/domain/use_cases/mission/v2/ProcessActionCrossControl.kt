package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossControlStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ICrossControlRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.CrossControlModel
import java.util.*

@UseCase
class ProcessActionCrossControl(
    private val crossControlRepo: ICrossControlRepository
) {
    fun execute(entity: CrossControlEntity?): CrossControlEntity? {
        if (entity == null) return null
        val model = process(entity) ?: return entity
        return CrossControlEntity.fromCrossControlModel(model)
    }

    fun process(fromAction: CrossControlEntity): CrossControlModel? {
        val fromDb = getFromDb(fromAction.id)
            ?: return crossControlRepo.save(fromAction.toCrossControlModel())

        val model = if (fromAction.status == CrossControlStatusType.NEW)
            fromDb.toModelSetData(fromAction)
        else fromDb.toModelSetConclusion(fromAction)
        return crossControlRepo.save(model)
    }


    fun getFromDb(id: UUID?): CrossControlEntity? {
        if (id == null) return null
        return CrossControlEntity.fromCrossControlModel(crossControlRepo.findById(id).get())
    }
}
