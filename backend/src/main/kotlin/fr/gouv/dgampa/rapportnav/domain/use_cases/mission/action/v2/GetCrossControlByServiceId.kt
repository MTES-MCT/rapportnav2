package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossControlStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ICrossControlRepository

@UseCase
class GetCrossControlByServiceId(
    private val processCrossControl: ProcessCrossControl,
    private val crossControlRepo: ICrossControlRepository
) {
    fun execute(serviceId: Int?): List<CrossControlEntity> {
        if(serviceId == null) return emptyList()
        return crossControlRepo
            .findByServiceId(serviceId = serviceId)
            .filter { it.status != CrossControlStatusType.CLOSED.toString() }
            .map { processCrossControl.execute(it) }
    }
}
