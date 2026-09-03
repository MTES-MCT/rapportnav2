package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntityMapper
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.controlUnitResource.IEnvControlUnitResourceRepository

@UseCase
class ComputeSati(
    private val controlResourceRepo: IEnvControlUnitResourceRepository
) {
    fun execute(sati: SatiEntity, action: MissionAction): SatiEntity? {
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
}
