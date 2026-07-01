package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntityMapper
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.sati.ISatiRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.EnableSati
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.sati.SatiMapper
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati.Sati
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati.SatiModelMapper

@UseCase
class ProcessSati(
    private val enableSati: EnableSati,
    private val satiRepo: ISatiRepository
) {

    fun execute(actionId: String, sati: Sati?): SatiEntity? {
        if (sati == null) return null
        if (!enableSati.execute()) return null

        sati.actionId = actionId
        val entity = SatiMapper.toEntity(sati)

        val existing = satiRepo.findByActionId(actionId)
        if(SatiEntityMapper.isEquals(existing,entity )) return entity

        val saved = satiRepo.save(entity)
        return SatiEntityMapper.merge(saved, sati)
    }
}
