package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntityMapper
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.sati.ISatiRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.sati.SatiMapper
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati.Sati

@UseCase
class ProcessSati(
    private val satiRepo: ISatiRepository
) {

    fun execute(actionId: String, sati: Sati?): SatiEntity? {
        if (sati == null) return null
        val entity = SatiMapper.toEntity(sati)
        val fromDb = satiRepo.findByActionId(actionId)

        if (SatiEntityMapper.isEquals(fromDb, entity)) return entity
        return SatiEntityMapper.merge(satiRepo.save(entity), sati)
    }
}
