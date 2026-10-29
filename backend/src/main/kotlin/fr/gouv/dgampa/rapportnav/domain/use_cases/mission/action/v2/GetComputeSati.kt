package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.sati.ISatiRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.controlUnitResource.IEnvControlUnitResourceRepository

@UseCase
class GetComputeSati(
    private val enableSati: EnableSati,
    private val satiRepo: ISatiRepository,
    private val controlResourceRepo: IEnvControlUnitResourceRepository
) {
    fun execute(action: MissionAction): SatiEntity? {
        if (!enableSati.execute()) return null
        if (action.id == null) throw IllegalArgumentException()
        val module = SatiModuleType.fromMissionActionType(action.actionType) ?: return null

        val sati = execute(actionId = action.id.toString(), module = module)
        return SatiEntityMapper.merge(sati = sati, action = action)
    }

    fun execute(action: MissionNavActionEntity): SatiEntity? {
        if (!enableSati.execute()) return null
        val module = SatiModuleType.fromActionType(action.actionType) ?: return null
        val sati = execute(actionId = action.getActionId(), module = module)
        return sati.copy(
            endDatetimeUtc = action.endDateTimeUtc,
            startDatetimeUtc = action.startDateTimeUtc,
            establishment = sati.establishment?.copy(data = action.establishment)
        )
    }

    private fun execute(actionId: String, module: SatiModuleType): SatiEntity {
        var sati = satiRepo.findByActionId(actionId = actionId)
        if (sati == null) sati = satiRepo.save(sati = getNewSati(actionId = actionId, module = module))
        sati.resource = getControlResource(sati.resource?.id)
        return sati
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

    private fun getNewSati(actionId: String, module: SatiModuleType): SatiEntity {
        return SatiEntity(
            module = module,
            actionId = actionId,
            vessel =  if (module.hasVessel()) SatiVesselEntity() else null,
            establishment = if (module.hasEstablishment()) SatiEstablishmentEntity() else null,
            inspectors = listOf(SatiInspectorEntity(isPrincipal = true, party = SatiPartyEntity()))
        )
    }
}
