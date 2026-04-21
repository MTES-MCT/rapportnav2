package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntityMapper
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.sati.ISatiRepository

@UseCase
class GetComputeSati(
    private val satiRepo: ISatiRepository
) {
    fun execute(action: MissionAction): SatiEntity? {
        if (action.id == null) throw IllegalArgumentException()
        if (!action.actionType.toString().endsWith("_CONTROL")) return null
        val sati = satiRepo.findByActionId(actionId = action.id.toString())
        return SatiEntityMapper.merge(sati = sati, action = action)
    }
}
