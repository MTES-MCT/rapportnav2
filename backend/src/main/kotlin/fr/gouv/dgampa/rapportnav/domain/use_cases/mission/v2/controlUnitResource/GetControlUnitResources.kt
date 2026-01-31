package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.controlUnitResource

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.controlUnitResource.IEnvControlUnitResourceRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env.ControlUnitResourceEnv

@UseCase
class GetControlUnitResources(
    private val repository: IEnvControlUnitResourceRepository
) {

    fun execute(): List<ControlUnitResourceEnv> {
        return repository.findAll()
    }
}
