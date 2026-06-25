package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.*
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.sati.ISatiRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.controlUnitResource.IEnvControlUnitResourceRepository

@UseCase
class GetComputeSati(
    private val satiRepo: ISatiRepository,
    private val controlResourceRepo: IEnvControlUnitResourceRepository
) {
    fun execute(action: MissionAction): SatiEntity? {
        if (action.id == null) throw IllegalArgumentException()
        if (!action.actionType.toString().endsWith("_CONTROL")) return null
        var sati = satiRepo.findByActionId(actionId = action.id.toString())
        if (sati == null) sati = satiRepo.save(getNewSati(action = action))

        sati.resource = getControlResource(sati.resource?.id)
        return SatiEntityMapper.merge(sati = sati, action = action)
    }

    private fun getControlResource(resourceId: Int?): ControlResourceEntity? {
        return controlResourceRepo.findAll().find { it.id == resourceId }
            ?.let {
                ControlResourceEntity(
                    id = it.id,
                    name = it.name,
                    type = it.type,
                    registrationId = it.registrationId,
                    radioFrequency = it.radioFrequency
                )
            }
    }

    private fun getNewSati(action: MissionAction): SatiEntity {
        return SatiEntity(
            vessel = SatiVesselEntity(),
            actionId = action.id.toString(),
            module = SatiModuleType.fromMissionActionType(action.actionType),
            inspectors = listOf(SatiInspectorEntity(party = SatiPartyEntity()))
        )
    }
}
