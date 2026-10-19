package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.*
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.sati.ISatiRepository

@UseCase
class GetComputeSati(
    private val enableSati: EnableSati,
    private val satiRepo: ISatiRepository,
    private val computeSati: ComputeSati
) {
    fun execute(action: MissionAction): SatiEntity? {
        if (!enableSati.execute()) return null
        if (action.id == null) throw IllegalArgumentException()
        if (!action.actionType.toString().endsWith("_CONTROL")) return null
        var sati = satiRepo.findByActionId(actionId = action.id.toString())
        if (sati == null) sati = satiRepo.save(sati = getNewSati(action = action))

        return computeSati.execute(sati = sati, action = action)
    }

    private fun getNewSati(action: MissionAction): SatiEntity {
        return SatiEntity(
            vessel = SatiVesselEntity(),
            actionId = action.id.toString(),
            module = SatiModuleType.fromMissionActionType(action.actionType),
            inspectors = listOf(SatiInspectorEntity(isPrincipal = true, party = SatiPartyEntity()))
        )
    }
}
