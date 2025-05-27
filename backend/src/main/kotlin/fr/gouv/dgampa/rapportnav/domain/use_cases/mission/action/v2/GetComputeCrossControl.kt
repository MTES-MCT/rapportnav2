package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossControlEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ICrossControlRepository
import java.util.*
import kotlin.jvm.optionals.getOrNull

@UseCase
class GetComputeCrossControl(
    private val crossControlRepo: ICrossControlRepository,
    private val missionActionRepository: INavMissionActionRepository
) {
    fun execute(crossControlId: UUID? ): CrossControlEntity? {
        if(crossControlId == null) return null;
        val model = crossControlRepo.findById(crossControlId).getOrNull()?: return null
        return CrossControlEntity.fromCrossControlModel(model)
    }
}

