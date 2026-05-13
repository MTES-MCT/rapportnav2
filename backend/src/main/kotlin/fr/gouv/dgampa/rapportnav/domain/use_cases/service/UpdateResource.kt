package fr.gouv.dgampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.controlUnitResource.IEnvControlUnitResourceRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.ResourceInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env.ControlUnitResourceEnv
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.inputs.PatchResourceInput


@UseCase
class UpdateResource(
    private val repository: IEnvControlUnitResourceRepository
) {
    fun execute(input: ResourceInput): ControlUnitResourceEnv {
        val resource = repository.findAll().find { it.id == input.id }
        if (resource?.controlUnitId != input.controlUnitId) throw BackendUsageException(
            code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
            message = "Action not allowed for the user on the resource: ${input.id}"
        )
        return repository.patch(
            id = input.id,
            resource = PatchResourceInput(
                registrationId = input.registrationId,
                radioFrequency = input.radioFrequency
            )
        )
    }
}
