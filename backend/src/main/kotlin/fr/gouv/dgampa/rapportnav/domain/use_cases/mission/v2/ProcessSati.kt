package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntityMapper
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.sati.ISatiRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.EnableSati
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.sati.SatiMapper
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.sati.Sati

@UseCase
class ProcessSati(
    private val enableSati: EnableSati,
    private val satiRepo: ISatiRepository
) {

    fun execute(actionId: String, sati: Sati?): SatiEntity? {
        if (sati == null) return null
        if (!enableSati.execute()) return null
        if (sati.actionId != actionId) {
            throw BackendUsageException(BackendUsageErrorCode.DOES_NOT_MATCH_EXCEPTION, "${sati.actionId} != $actionId")
        }

        val existing = satiRepo.findByActionId(actionId)
        val incoming = SatiMapper.toEntity(sati)
        if (SatiEntityMapper.isEquals(existing, incoming)) return incoming

        val saved = satiRepo.save(incoming)
        return SatiEntityMapper.merge(saved, sati)
    }
}
